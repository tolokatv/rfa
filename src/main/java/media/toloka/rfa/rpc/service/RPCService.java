package media.toloka.rfa.rpc.service;

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

import java.util.UUID;

@Service
public class RPCService {

    @Autowired
    private MessageService messageService;

    @Autowired
    private PoolPortsService poolPortsService;

    @Autowired
    private StationService stationService;

    final Logger logger = LoggerFactory.getLogger(RPCService.class);

    public void JobCreateStation (RPCJob rjob) {
//        logger.info(rjob);
        // витягли користувача
        Users user = rjob.getUser();
        // саме тут створюємо новий обʼєкт - радіостанція
        Station newRadio = poolPortsService.AttachPort(user,new Station(), EServerPortType.PORT_MAIN);
        newRadio.setUuid(UUID.randomUUID().toString()); // TODO перенесті в конструктор
        // Сохраніть станцію с портом (Занять порт в пуле портов)
        stationService.AddAndSaveRadio(user, newRadio);
        newRadio = poolPortsService.AttachPort(user,newRadio, EServerPortType.PORT_SHOW);
        stationService.AddAndSaveRadio(user, newRadio);
        newRadio = poolPortsService.AttachPort(user,newRadio, EServerPortType.PORT_WEB);
        stationService.AddAndSaveRadio(user, newRadio);
        // записуємо подію в журнал.

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
