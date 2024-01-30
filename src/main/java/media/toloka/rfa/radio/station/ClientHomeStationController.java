package media.toloka.rfa.radio.station;


import com.google.gson.Gson;
import lombok.Data;
import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.contract.service.ContractService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Contract;
import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.rpc.model.RPCJob;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import static media.toloka.rfa.radio.model.enumerate.EHistoryType.History_StatiionChange;
import static media.toloka.rfa.rpc.model.ERPCJobType.*;

@Controller
public class ClientHomeStationController {

    @Data
    private class FormStr {
        private String name;
        private Long id;
    }

    @Value("${rabbitmq.queue}")
    private String queueNameRabbitMQ;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private StationService stationService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private GsonService gsonService;

    @Autowired
    private MessageService messageService;

    @Autowired
    RabbitTemplate template;

    final Logger logger = LoggerFactory.getLogger(ClientHomeStationController.class);


    @GetMapping(value = "/user/stations")
    public String userHomeStation(
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            logger.warn("User not found. Redirect to main page");
            return "redirect:/";
        }
        // TODO додати в меню інформацію по повідомленням - всі/нові
//        messageService.setNavQuantityMessage(model, clientService.getClientDetail(user)); // встановили кількість повідомлень для меню.
        model.addAttribute("stations",  stationService.GetListStationByUser(user));
        return "/user/stations";
    }


    @GetMapping(value = "/user/createstation")
    public String userCreateStation(
            Model model ) {

//        logger.info("Create New station.");
        Users user = clientService.GetCurrentUser();
        Clientdetail clientdetail = clientService.GetClientDetailByUser(user);
        if (user == null) {
//            logger.warn("User not found. Redirect to main page");
            return "/user/createstation";
        }
        if (stationService.CreateCheckConfirminfo(clientdetail) == false) {
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
            // TODO Вісвітити повідомлення для користувача з причинами неможливості створити станцію
            model.addAttribute("warning", "Неможливо створити станцію! Ви не надали згоду з правилами користування сервісом. " +
                    "У Профайлі відмітте поле \"З умовами надання сервісу погоджуюся\".");
            return "/user/stations";
        }
        if (stationService.CreateCheckAddress(clientdetail) == false) {
            model.addAttribute("warning", "Неможливо створити станцію! У Профайлі відсутня поштова адреса.");
            return "/user/stations";
        }

        if (clientdetail.getStationList().size() > 0) {
            if (stationService.HavePayContract(clientdetail) == false) {
                // перевіряємо наявності безкоштовної станції
                model.addAttribute("warning", "Неможливо створити станцію! У Вас немає комерційного контракту і вже є тестова станція");
                return "/user/stations";
            }
        }
//
//        if (stationService.HavePayContract(clientdetail) == false) {
//            // перевіряємо наявності безкоштовної станції
//            model.addAttribute("warning", "Неможливо створити станцію! У Вас немає комерційного контракту і вже є тестова станція");
//            return "/user/stations";
//        }
//        else {
//            if (clientdetail.getStationList().size() > 0) {
//                model.addAttribute("warning", "Неможливо створити станцію! У Вас вже є безкоштовна станція .");
//                return "/user/stations";
//            }
//        }


        Station station = stationService.CreateStation(clientdetail);
        if (station == null) {
            logger.info("Не можемо створити станцію для користувача {}. ", user.getEmail());
            model.addAttribute("warning", "Не можемо створити станцію. Повідомте про це службі підтримки.");
            return "/user/stations";
        }
//        historyService.saveHistory(History_StatiionCreate, " Нова станція: "
//                        +station.getUuid()
//                        +"для користувача " + clientdetail.getUser().getEmail(),
//                clientdetail.getUser());
        RPCJob rjob = new RPCJob();
        rjob.getJobchain().add(JOB_STATION_CREATE);
        rjob.getJobchain().add(JOB_STATION_ALLOCATE);
        rjob.getJobchain().add(JOB_STATION_PREPARE_NGINX);
        rjob.getJobchain().add(JOB_STATION_LIBRETIME_MIGRATE);
        rjob.getJobchain().add(JOB_STATION_START);
        rjob.getJobchain().add(JOB_STATION_STOP);
//        rjob.setRJobType(JOB_STATION_CREATE); // Створюємо необхідну інформацію в базі для станції
        rjob.setUser(user);
        // Додаємо станцію і передаємо на виконання на віддалений сервіс

        Gson gstation = gsonService.CreateGson();

        String strStation = gstation.toJson(station).toString();
        rjob.setRjobdata(strStation);
        // https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
        logger.info("Rjob : {}",strStation);
        Gson gson = gsonService.CreateGson();
        String strgson = gson.toJson(rjob).toString();
        logger.info("Str to rabbit {}",strgson);
        template.convertAndSend(queueNameRabbitMQ,gson.toJson(rjob).toString());
        return "redirect:/user/stations";
    }



    @GetMapping(value = "/user/controlstation")
    public String userControltStation(
            @RequestParam(value = "id", required = true) Long id,
//            @ModelAttribute Station station,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Station mstation;
        mstation = stationService.GetStationById(id);
        if (mstation == null) {
            // Станцію створити не можемо. Показуємо про це повідомлення.
            logger.info("ClientHomeStationController:  Не можемо запустити станцію для користувача {}", user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
            return "redirect:/user/stations";
        }
        // користувач та танція знайдені. Працюємо зі станцією.
//        FormStr fstr = new FormStr();
//        fstr.setId(mstation.getId());
//        fstr.setName(mstation.getName());
        model.addAttribute("contracts",  contractService.ListContractByUser(user));
        model.addAttribute("linkstation",  stationService.GetURLStation(mstation));
        model.addAttribute("station",  mstation);
        return "/user/controlstation";
    }

    @PostMapping(value = "/user/stationsave")
    public String userHomeStationSave(
            @ModelAttribute Station station,
            Model model ) {
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            logger.warn("userHomeStationSave: User not found. Redirect to main page");
            return "redirect:/";
        }

        if (station == null) {
            // Станцію створити не можемо. Показуємо про це повідомлення.
            logger.info("userHomeStationSave: Не можемо зберегти станцію id={} для користувача {}", station.getId(), user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
            return "redirect:/user/stations";
        }

        Station nstation;
        if (station.getId() != null) {
            nstation = stationService.GetStationById(station.getId());
        } else {
            logger.info("userHomeStationSave:  Не можемо зберегти станцію id={} для користувача {}", station.getId(), user.getEmail());
            return "redirect:/user/stations";
        }

        nstation.setName(station.getName());
        nstation.setIcecastdescription(station.getIcecastdescription());
        nstation.setIcecastname(station.getIcecastname());
        nstation.setIcecastgenre(station.getIcecastgenre());
        nstation.setIcecastsite(station.getIcecastsite());
        nstation.setLastchangedate(new Date());
        stationService.saveStation(nstation);
        // TODO додати запис в журнал
        historyService.saveHistory(History_StatiionChange,
                nstation.getUuid().toString() + ": " +nstation.getLastchangedate().toString() + " Збережено станцію " + nstation.getUuid().toString(),
                user
        );
        // TODO відправити повідомлення на сторінку
        model.addAttribute("success",  "Вашу станцію збережено в базу.");
        return "redirect:/user/stations";
    }



    @GetMapping(value = "/user/startstation")
    public String userStartStation(
            @RequestParam(value = "id", required = true) Long id,
            Model model ) {

        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Station station = stationService.GetStationById(id);
        if (station == null) {
            // Станцію створити не можемо. Показуємо про це повідомлення.
            logger.info("userStartStation: Не можемо запустити станцію для користувача {}", user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість запуску станції та кинути клієнту месседж
            return "redirect:/user/stations";
        }
        Contract contract = station.getContract();
        if (contract == null) {
            logger.info("Не можемо запустити станцію " + station.getUuid() + " для користувача " + user.getEmail() + " Перевірте, чи приєднана вона до контракту.");
            model.addAttribute("warning", "Не можемо запустити станцію " + station.getUuid() + " для користувача " + user.getEmail() + " Перевірте, чи приєднана вона до контракту.");
            return "redirect:/user/stations";
        }
        if (station.getContract().getClientdetail().getAccount() < 0 ) {
            // todo перевірити гроші на рахунку і проплачений термін
            logger.info("Не можемо запустити станцію " + station.getUuid() + " для користувача " + user.getEmail() + " Перевірте, чи достатньо коштів на рахунку");
            model.addAttribute("warning", "Не можемо запустити станцію " + station.getUuid() + " для користувача " + user.getEmail() + " Перевірте, чи достатньо коштів на рахунку");
            return "redirect:/user/stations";
        }
        RPCJob rjob = new RPCJob();
        rjob.getJobchain().add(JOB_STATION_START);
        rjob.setUser(user);
        Gson gstation = gsonService.CreateGson();
        rjob.setRjobdata(gstation.toJson(station).toString());
        // https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
        Gson gson = gsonService.CreateGson();
        String strgson = gson.toJson(rjob).toString();
        template.convertAndSend(queueNameRabbitMQ,gson.toJson(rjob).toString());
        return "redirect:/user/stations";
    }

    @GetMapping(value = "/user/stopstation")
    public String userStopStation(
            @RequestParam(value = "id", required = true) Long id,

            Model model ) {

        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Station station = stationService.GetStationById(id);
        if (station == null) {
            return "redirect:/user/stations";
        }
        RPCJob rjob = new RPCJob();
        rjob.getJobchain().add(JOB_STATION_STOP);
        rjob.setUser(user);
        Gson gstation = gsonService.CreateGson();
        rjob.setRjobdata(gstation.toJson(station).toString());
        // https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
        Gson gson = gsonService.CreateGson();
        String strgson = gson.toJson(rjob).toString();
        template.convertAndSend(queueNameRabbitMQ,gson.toJson(rjob).toString());
        return "redirect:/user/stations";
    }


    @GetMapping(value = "/user/psstation")
    public String userPSStation(
            @RequestParam(value = "id", required = true) Long id,
            Model model ) {

//        logger.info("Create New station.");
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            return "redirect:/";
        }
        Station station = stationService.GetStationById(id);
        if (station == null) {
            // Станцію створити не можемо. Показуємо про це повідомлення.
            logger.info("userPSStation:  Не можемо перевірити стан станції для користувача {}", user.getEmail());
            return "redirect:/user/stations";
        }
        RPCJob rjob = new RPCJob();
        rjob.getJobchain().add(JOB_STATION_GET_PS);
        rjob.setUser(user);
        Gson gstation = gsonService.CreateGson();
        rjob.setRjobdata(gstation.toJson(station).toString());
        // https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
        Gson gson = gsonService.CreateGson();
        String strgson = gson.toJson(rjob).toString();
        template.convertAndSend(queueNameRabbitMQ,gson.toJson(rjob).toString());
        return "redirect:/user/stations";
    }
}
