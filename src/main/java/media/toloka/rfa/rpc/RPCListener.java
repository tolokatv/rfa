package media.toloka.rfa.rpc;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import media.toloka.rfa.config.gson.LocalDateDeserializer;
import media.toloka.rfa.config.gson.LocalDateSerializer;
import media.toloka.rfa.config.gson.LocalDateTimeDeserializer;
import media.toloka.rfa.config.gson.LocalDateTimeSerializer;
import media.toloka.rfa.rpc.model.RPCJob;
import media.toloka.rfa.rpc.service.RPCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class RPCListener {

    @Autowired
    private RPCService serviceRPC;
Logger logger = LoggerFactory.getLogger(RPCListener.class);

//    @RabbitListener(queues = "rfajob")
//    public String worker1(String message) throws InterruptedException {
//        logger.info("Received on worker : " + message);
//        // TODO тут обробляємо завдання з фронтенда.
//        // створення та керування радіостанціями, обробка файлів музикантів тощо.
////        Thread.sleep(20000);
//        return "Received on worker : " + message + "  uuid=" + UUID.randomUUID().toString();
//    }

    @RabbitListener(queues = "rfajob")
    public void processedFromFront(String message) {
        logger.info("Received from queue 1: " + message);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class,        new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class,        new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class,    new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class,    new LocalDateTimeDeserializer());

        Gson gson = gsonBuilder.setPrettyPrinting().create();
        RPCJob rjob = gson.fromJson(message, RPCJob.class);
        // TODO тут обробляємо завдання з фронтенда.
        logger.info("+++++++++++++++++  Recive message from QUEUES.");
        switch (rjob.getRJobType()) {
            case JOB_RADIO_CREATE:
//                logger.info("======= RADIO CREATE  {}    {}", rjob.getRJobType().label, rjob.getRjobdata());
                serviceRPC.JobCreateStation(rjob);
//                serviceRPC.SendMessageToUser(user,null,msg);
                break;
            case JOB_CONTRACT_CREATE:
                logger.info("======= {}    {}", rjob.getRJobType().label, rjob.getRjobdata());
                break;
            case JOB_CONTRACT_ADD_NAME:
                logger.info("======= {}    {}", rjob.getRJobType().label, rjob.getRjobdata());
                break;
            case JOB_CONTRACT_ADD_STATION:
                logger.info("======= {}    {}", rjob.getRJobType().label, rjob.getRjobdata());
                break;
            default:
                logger.info("======= {}    {}", rjob.getRJobType().label, rjob.getRjobdata());
                break;
        }

    }
}