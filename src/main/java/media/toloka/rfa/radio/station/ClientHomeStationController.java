package media.toloka.rfa.radio.station;


import com.google.gson.Gson;
import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.rpc.model.ERPCJobType;
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

import static media.toloka.rfa.rpc.model.ERPCJobType.*;

@Controller
public class ClientHomeStationController {

    @Value("${rabbitmq.queue}")
    private String queueNameRabbitMQ;

    @Autowired
    private ClientService clientService;

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
            logger.info("Не можемо створити станцію для користувача {}", user.getEmail());
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
}
