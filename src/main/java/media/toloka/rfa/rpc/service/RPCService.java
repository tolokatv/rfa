package media.toloka.rfa.rpc.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import media.toloka.rfa.config.gson.LocalDateDeserializer;
import media.toloka.rfa.config.gson.LocalDateSerializer;
import media.toloka.rfa.config.gson.LocalDateTimeDeserializer;
import media.toloka.rfa.config.gson.LocalDateTimeSerializer;
import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.radio.history.service.HistoryService;
import media.toloka.rfa.radio.message.service.MessageService;
import media.toloka.rfa.radio.station.model.EServerPortType;
import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.radio.station.service.PoolPortsService;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.rpc.model.RPCJob;
import media.toloka.rfa.security.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static media.toloka.rfa.radio.history.model.EHistoryType.History_StatiionCreate;

@Service
public class RPCService {

    @Autowired
    private MessageService messageService;

    @Autowired
    private PoolPortsService poolPortsService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private StationService stationService;

    @Autowired
    private GsonService gsonService;

    final Logger logger = LoggerFactory.getLogger(RPCService.class);

    public void JobCreateStation (RPCJob rjob) {
//        logger.info(rjob);
        // витягли користувача
        Users user = rjob.getUser();
        // Витягнути станцію з переданого gson
        String sStation = rjob.getRjobdata();
        Gson gStation = gsonService.CreateGson();
        Station newRadio = gStation.fromJson(sStation, Station.class);

        // саме тут створюємо новий обʼєкт - радіостанція
        newRadio = poolPortsService.AttachPort(user,newRadio, EServerPortType.PORT_MAIN);
//        newRadio.setUuid(UUID.randomUUID().toString()); //
        // Сохраніть станцію с портом (Занять порт в пуле портов)
        stationService.saveStation(newRadio);
        newRadio = poolPortsService.AttachPort(user,newRadio, EServerPortType.PORT_SHOW);
        stationService.saveStation(newRadio);
        newRadio = poolPortsService.AttachPort(user,newRadio, EServerPortType.PORT_WEB);
        stationService.saveStation( newRadio);
        // записуємо подію в журнал.
        String sCreateDate = newRadio.getCreatedate().toString();
        String suuid = newRadio.getUuid().toString();
        historyService.saveHistory(History_StatiionCreate,
                newRadio.getCreatedate().toString() + " Create " + newRadio.getUuid().toString(),
                user
                );

//        String rjobdata = rjob.getRjobdata();
//        GsonBuilder builder = new GsonBuilder();
////        builder.setPrettyPrinting();
//
//        Gson gson = builder.create();
//        Users student = gson.fromJson(rjobdata, Users.class);
//        System.out.println(student);
//
//        jsonString = gson.toJson(student);
//        System.out.println(jsonString);
        // подивилися, чи є у нього угода. Якщо немає контракту то створили безкоштовний
            // відправили повідомлення про створення контракту
        // подивилися, чи є в угоді станція. Якщо немає то створили радіостанцію на безкоштовному пакеті
            // відправили повідомлення про створення радіостанції
            // якщо в безкоштовній угоді є радіостанція, то повідомлення, що вже є безкоштовна станція
        // створили станцію
            // відправили повідомлення про створення станції
//        serviceMessage.SendMessageToUser(null, user, msg);
    }
}
