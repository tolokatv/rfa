package media.toloka.rfa.rpc;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import media.toloka.rfa.config.gson.LocalDateDeserializer;
import media.toloka.rfa.config.gson.LocalDateSerializer;
import media.toloka.rfa.config.gson.LocalDateTimeDeserializer;
import media.toloka.rfa.config.gson.LocalDateTimeSerializer;
import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.rpc.model.RPCJob;
import media.toloka.rfa.rpc.service.RPCService;
import media.toloka.rfa.rpc.service.ServerRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static media.toloka.rfa.rpc.model.ERPCJobType.JOB_STATION_PREPARE_NGINX;

@Component
public class RPCListener {

    @Autowired
    private RPCService serviceRPC;

    @Autowired
    private ServerRunnerService serverRunner;

    @Autowired
    RabbitTemplate template;

    @Autowired
    private GsonService gsonService;

Logger logger = LoggerFactory.getLogger(RPCListener.class);

//    @RabbitListener(queues = "rfajob")
//    public String worker1(String message) throws InterruptedException {
//        logger.info("Received on worker : " + message);
//        // TODO тут обробляємо завдання з фронтенда.
//        // створення та керування радіостанціями, обробка файлів музикантів тощо.
////        Thread.sleep(20000);
//        return "Received on worker : " + message + "  uuid=" + UUID.randomUUID().toString();
//    }

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void processedFromFront(String message) {

        Gson gson = gsonService.CreateGson();
        RPCJob rjob = gson.fromJson(message, RPCJob.class);
        // TODO тут обробляємо завдання з фронтенда.
//        logger.info("+++++++++++++++++  Recive message from QUEUES.");
        switch (rjob.getRJobType()) {
            case JOB_STATION_CREATE:
//                logger.info("======= RADIO CREATE  {}    {}", rjob.getRJobType().label, rjob.getRjobdata());
                serviceRPC.JobCreateStation(rjob);
                logger.info("+++++++++++++++++  JOB_STATION_CREATE");
//                serviceRPC.SendMessageToUser(user,null,msg);
                break;
            case JOB_STATION_ALLOCATE:
                logger.info("+++++++++++++++++  JOB_STATION_ALLOCATE");
                serverRunner.AllocateStationOnServer(rjob);
                break;
            case JOB_STATION_LIBRETIME_MIGRATE:
                logger.info("+++++++++++++++++  JOB_STATION_LIBRETIME_MIGRATE");
                serverRunner.StationMigrateLibretimeOnInstall(rjob);
                break;
            case JOB_STATION_PREPARE_NGINX:
                serverRunner.StationPrepareNginx(rjob);
                break;
            case JOB_STATION_START:
                logger.info("+++++++++++++++++  JOB_STATION_START");
                serverRunner.StationStart(rjob);
                break;
            case JOB_STATION_STOP:
                logger.info("+++++++++++++++++  JOB_STATION_STOP");
                serverRunner.StationStop(rjob);
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