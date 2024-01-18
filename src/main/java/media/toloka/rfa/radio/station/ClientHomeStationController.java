package media.toloka.rfa.radio.station;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import media.toloka.rfa.config.gson.LocalDateDeserializer;
import media.toloka.rfa.config.gson.LocalDateSerializer;
import media.toloka.rfa.config.gson.LocalDateTimeDeserializer;
import media.toloka.rfa.config.gson.LocalDateTimeSerializer;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class ClientHomeStationController {

//    @Autowired
//    private UserRepository userRepo;
//    @Autowired
//    private RepoRadio repoRadio;


    @Value("${rabbitmq.queue}")
    private String queueName;

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
        messageService.setQuantityMessage(model, clientService.getClientDetail(user));
        // дивимося його групи
        // відповідним чином виводимо пункти меню
        // Заповнюємо поля для форми
//        model.addAttribute("userID",    user.getId());
//        model.addAttribute("userName",  user.getName());
        model.addAttribute("stations",  stationService.GetListStationByUser(user));
        return "/user/stations";
    }


    @GetMapping(value = "/user/createstation")
    public String userCreateStation(
            Model model ) {

        logger.info("Create New station.");
        Users user = clientService.GetCurrentUser();
        if (user == null) {
            logger.warn("User not found. Redirect to main page");
            return "redirect:/";
        }
        // відправляємо завдання на створення радіостанції.
        RPCJob rjob = new RPCJob();
        rjob.setRJobType(ERPCJobType.JOB_STATION_CREATE); // set job type
        rjob.setUser(user);
        // Додаємо станцію і передаємо на виконання на віддалений сервіс
        Station station = stationService.CreateStation();
        if (station == null) {
            // Станцію створити не можемо. Показуємо про це повідомлення.
            logger.info("Не можемо створити станцію для користувача {}", user.getEmail());
            // TODO Відправити користувачу повідомлення про неможливість створення станції
            return "redirect:/user/stations";
        }

        Gson gstation = gsonService.CreateGson();
        rjob.setRjobdata(gstation.toJson(station).toString());
        // https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
        Gson gson = gsonService.CreateGson();
        String strgson = gson.toJson(rjob).toString();
        template.convertAndSend(queueName,gson.toJson(rjob).toString());
        return "redirect:/user/stations";
    }
}
