package media.toloka.rfa.rpc;


import com.google.gson.Gson;
import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.rpc.model.ERPCJobType;
import media.toloka.rfa.rpc.model.RPCJob;
import media.toloka.rfa.rpc.model.ResultJob;
import media.toloka.rfa.rpc.service.RPCService;
import media.toloka.rfa.rpc.service.ServerRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RPCListener {

    @Autowired
    private RPCService serviceRPC;

    @Autowired
    private ServerRunnerService serverRunnerService;

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
        Long rc = 0L;
        Gson gson = gsonService.CreateGson();
        RPCJob rjob = gson.fromJson(message, RPCJob.class);

        // TODO тут обробляємо завдання з фронтенда.
//        logger.info("+++++++++++++++++  Recive message from QUEUES.");
        ERPCJobType curJob = rjob.getJobchain().poll();
        switch (curJob) {
//        switch (rjob.getRJobType()) {
            case JOB_STATION_CREATE:  // Заповнюємо базу необхідною інформацією
                logger.info("+++++++++++++++++ START JOB_STATION_CREATE");
                rc = serviceRPC.JobCreateStation(rjob); // from Client Page. Next step
                rjob.getJobresilt().add(new ResultJob(rc, curJob));
                logger.info("+++++++++++++++++ END JOB_STATION_CREATE");
                break;
            case JOB_STATION_ALLOCATE: // розміщуємо каталоги на сервері, створюємо базу, користувачів у Postgresql та Rabbit.
                logger.info("+++++++++++++++++ START JOB_STATION_ALLOCATE");
                serverRunnerService.AllocateStationOnServer(rjob);
                logger.info("+++++++++++++++++ END JOB_STATION_ALLOCATE");
                break;
            case JOB_STATION_LIBRETIME_MIGRATE: // Після розміщення станції запускаємо першу процедуру міграції
                serverRunnerService.StationMigrateLibretimeOnInstall(rjob);
                logger.info("+++++++++++++++++  JOB_STATION_LIBRETIME_MIGRATE");
                break;
            case JOB_STATION_PREPARE_NGINX:
                serverRunnerService.StationPrepareNginx(rjob);
                logger.info("+++++++++++++++++  JOB_STATION_PREPARE_NGINX");
                break;
            case JOB_STATION_START:
                serverRunnerService.StationStart(rjob);
                logger.info("+++++++++++++++++  JOB_STATION_START");
                break;
            case JOB_STATION_STOP:
                serverRunnerService.StationStop(rjob);
                logger.info("+++++++++++++++++  JOB_STATION_STOP");
                break;

            case JOB_STATION_GET_PS:
                serverRunnerService.StationGetPS(rjob);
                logger.info("+++++++++++++++++  JOB_STATION_GET_PS");
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
        serverRunnerService.CompletedPartRPCJob(rjob);

    }
}