package media.toloka.rfa.radio.client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import media.toloka.rfa.config.gson.LocalDateDeserializer;
import media.toloka.rfa.config.gson.LocalDateSerializer;
import media.toloka.rfa.config.gson.LocalDateTimeDeserializer;
import media.toloka.rfa.config.gson.LocalDateTimeSerializer;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.rpc.model.ERPCJobType;
import media.toloka.rfa.rpc.model.RPCJob;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class ClientHomeStationController {

//    @Autowired
//    private UserRepository userRepo;
//    @Autowired
//    private RepoRadio repoRadio;

    @Autowired
    private ClientService clientService;

    @Autowired
    private StationService stationService;

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
        rjob.setRJobType(ERPCJobType.JOB_RADIO_CREATE); // set job type
        rjob.setUser(user);
        rjob.setRjobdata("Job for remote program call");
        // https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class,        new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class,        new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class,    new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class,    new LocalDateTimeDeserializer());

        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String strgson = gson.toJson(rjob).toString();
        logger.info("======== Send to RPC: "+strgson);
        template.convertAndSend("rfajob",gson.toJson(rjob).toString());


//        Radio radio = new Radio();
//
//        if (user.getStations() == null) {
//                user.setStations(new ArrayList());
//                user.setStations(user.getStations().add(radio));
//            } else { user.setStations(user.getStations().add(radio)); }
//
//        serviceUser.saveUser(user);
        return "redirect:/user/stations";
    }
}
