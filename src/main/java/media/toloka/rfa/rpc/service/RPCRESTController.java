package media.toloka.rfa.rpc.service;



import com.google.gson.Gson;
import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.rpc.RPCController;
import media.toloka.rfa.rpc.model.RPCJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static media.toloka.rfa.rpc.model.ERPCJobType.JOB_STATION_START;

@RestController
public class RPCRESTController {

    Logger logger = LoggerFactory.getLogger(RPCRESTController.class);

    @Autowired
    private StationService stationService;

    @Value("${rabbitmq.queue}")
    private String queueName;

    @Value("${media.toloka.rfa.server.client_dir}")
    private String clientdir;
    @Autowired
    RabbitTemplate template;

    @Autowired
    private GsonService gsonService;

    @Autowired
    ServerRunnerService serverRunnerService;

    @GetMapping("/api/1.0/ps/{id}")
    Map<String, String> GetStateStationREST(@PathVariable Long id) {

        Station station = stationService.GetStationById(id);
        if (station == null){
            // не знайшли станцію
            logger.info("GetStateStationREST: Йой! не знайшли станцію id={}",id);
            return null;
        }

        ProcessBuilder pb = new ProcessBuilder("bash", "-c", "docker ps --format \"{{.State}} {{.CreatedAt}} {{.Names}}\"|grep "+station.getUuid());
        Map<String, String> env = pb.environment();
        serverRunnerService.SetEnvironmentForProcessBuilder(env, station);
        String server_workdir;
        server_workdir = env.get("HOME")+ clientdir + "/" + env.get("CLIENT_UUID") + "/" +env.get("STATION_UUID");
        pb.directory(new File(server_workdir));
        int exitcode;
        List<String> resultStringList = new ArrayList<>();
        try {
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            // виводимо на консоль
            String line;
            while ((line = reader.readLine()) != null) {
                resultStringList.add(line);
                logger.info(line);
            }
//            try {
            exitcode = p.waitFor();
        } catch (IOException e) {
            logger.warn(" Щось пішло не так при виконанні завдання в операційній системі");

            e.printStackTrace();
        } catch (InterruptedException e){
            logger.warn(" Щось пішло не так при виконанні завдання (p.waitFor) InterruptedException");
            e.printStackTrace();
        }

        // обробляємо строки зі статусом сервісу
        final List<String> serviseName = List.of("nginx","liquidsoap","legacy","playout","api","analyzer");
        Map<String, String> result = new HashMap<String,String>();
        for (String key : serviseName) {
            System.out.println(key);
            for (String resultString : resultStringList) {
                int index = resultString.indexOf(key);
                if (index != -1) {
                // Знайшли строку сервісу
                    // витягуемо status контейнера
                    String value = resultString.substring(0,resultString.indexOf(" "));
                    result.put(key,value);
                    logger.info("Статус сервісу {} = {} для станції {}",key,value,station.getUuid());
                }
            }
        }
        return result;
    }
}
