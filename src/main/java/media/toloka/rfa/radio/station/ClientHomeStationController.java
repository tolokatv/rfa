package media.toloka.rfa.radio.station;


import com.google.gson.Gson;
import lombok.Data;
import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.contract.service.ContractService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.station.model.Station;
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
        messageService.setNavQuantityMessage(model, clientService.getClientDetail(user)); // встановили кількість повідомлень для меню.
        model.addAttribute("stations",  stationService.GetListStationByUser(user));
        return "/user/stations";
    }


    @GetMapping(value = "/user/createstation")
    public String userCreateStation(
            Model model ) {

//        logger.info("Create New station.");
        Users user = clientService.GetCurrentUser();
        if (user == null) {
//            logger.warn("User not found. Redirect to main page");
            return "redirect:/";
        }
        Station station = stationService.CreateStation(model);
        if (station == null) {
            // Станцію створити не можемо. Показуємо про це повідомлення.
            logger.info("ClientHomeStationController: Не можемо створити станцію для користувача {}", user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
            return "redirect:/user/stations";
        }
//        clientService.getClientDetail(user).getStationList().add(station);
        // відправляємо завдання на створення радіостанції.
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
        rjob.setRjobdata(gstation.toJson(station).toString());
        // https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
        Gson gson = gsonService.CreateGson();
        String strgson = gson.toJson(rjob).toString();
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
            logger.warn("ClientHomeStationController: User not found. Redirect to main page");
            return "redirect:/";
        }

        if (station == null) {
            // Станцію створити не можемо. Показуємо про це повідомлення.
            logger.info("userControltStation: Не можемо зберегти станцію id={} для користувача {}", station.getId(), user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
            return "redirect:/user/stations";
        }

        Station nstation;
        if (station.getId() != null) {
            nstation = stationService.GetStationById(station.getId());
        } else {
            logger.info("ClientHomeStationController:  Не можемо зберегти станцію id={} для користувача {}", station.getId(), user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
            return "redirect:/user/stations";
        }
        // TODO додати запис в журнал
        nstation.setName(station.getName());
        nstation.setIcecastdescription(station.getIcecastdescription());
        nstation.setIcecastname(station.getIcecastname());
        nstation.setIcecastgenre(station.getIcecastgenre());
        nstation.setIcecastsite(station.getIcecastsite());
        stationService.saveStation(nstation);
        return "redirect:/user/stations";
    }



    @GetMapping(value = "/user/startstation")
    public String userStartStation(
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
            logger.info("ClientHomeStationController: Не можемо запустити станцію для користувача {}", user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
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

//        logger.info("Create New station.");
        Users user = clientService.GetCurrentUser();
        if (user == null) {
//            logger.warn("User not found. Redirect to main page");
            return "redirect:/";
        }
        Station station = stationService.GetStationById(id);
        if (station == null) {
            // Станцію створити не можемо. Показуємо про це повідомлення.
            logger.info("ClientHomeStationController:  Не можемо створити станцію для користувача {}", user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
            return "redirect:/user/stations";
        }
//        clientService.getClientDetail(user).getStationList().add(station);
        // відправляємо завдання на створення радіостанції.
        RPCJob rjob = new RPCJob();
        rjob.getJobchain().add(JOB_STATION_STOP);
//        rjob.setRJobType(JOB_STATION_CREATE); // Створюємо необхідну інформацію в базі для станції
        rjob.setUser(user);
        // Додаємо станцію і передаємо на виконання на віддалений сервіс

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
            logger.info("ClientHomeStationController:  Не можемо запустити станцію для користувача {}", user.getEmail());
            // TODO Відправити у форму повідомлення про неможливість створення станції та кинути клієнту месседж
            // TODO зробити запис в журнал
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
