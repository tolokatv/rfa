package media.toloka.rfa.rpc;



import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.creater.service.CreaterService;
import media.toloka.rfa.radio.model.Album;
import media.toloka.rfa.radio.model.Albumсover;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.station.onlinelist.Model.ListOnlineFront;
import media.toloka.rfa.radio.station.service.StationOnlineList;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.rpc.service.ServerRunnerService;
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
import java.time.ZoneId;
import java.util.*;

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

    @Autowired
    private CreaterService createrService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/api/1.0/stationonline/")
    List<ListOnlineFront> GetStateStationREST() {

        List<ListOnlineFront> listOnline = StationOnlineList.getInstance().GetOnlineList();
        return listOnline;
    }

    @GetMapping("/api/1.0/ps/{id}")
    Map<String, String> GetStateStationREST(@PathVariable Long id) {
        // для сайту - запит та асінхронна обробка. https://www.cat-in-web.ru/fetch-async-await/

        Station station = stationService.GetStationById(id);
        if (station == null){
            // не знайшли станцію
            logger.info("GetStateStationREST: Йой! не знайшли станцію id={}",id);
            return null;
        }
        // todo передбачити маршрутизацію на сервер, на якому виконується докер
        // application.properties: media.toloka.rfa.server.libretime.guiserver=localhost
        // сервер при завантаженні створює відповідну чергу в яку для нього надсилаються повідомлення

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
            }
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
        int count = 0;
        for (String key : serviseName) {
//            System.out.println(key);
            for (String resultString : resultStringList) {
                int index = resultString.indexOf(key);
                if (index != -1) {
                    // Знайшли строку сервісу
                    // витягуемо status контейнера
                    String value = resultString.substring(0, resultString.indexOf(" "));
                    if (value.indexOf("runn") != -1) {
                        count = count + 1;
                    }
                    result.put(key,value);
                }
            }
        }
        result.put("status",String.valueOf(count) );
        return result;
    }

    @GetMapping("/api/1.0/test")
    Map<String, String> GetTest() {
        Map<String, String> result = new HashMap<String,String>();
        String getstring = "running 2024-02-03 11:22:01 +0200 EET b843ec36-51af-4416-8f90-24471d53dd05-analyzer-1";
        int sindex = getstring.indexOf(" ")+1;
        int eindex = getstring.lastIndexOf(" ");
        String sDate = getstring.substring(sindex,eindex);
        String[] strParts = getstring.split(" ");
        String[] strPartsm = getstring.split("-");


//        Date mydate;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z z");
//        mydate = Date.(sDate); // parse(sDate, formatter);
//        logger.info("Date = {}",mydate);

        ZoneId defaultZoneId = ZoneId.systemDefault();

        result.put("length",String.valueOf(strPartsm.length));
        result.put("service",strPartsm[strPartsm.length - 2]);
        result.put("date",strParts[1]);
        result.put("time",strParts[2]);
        result.put("TZ",defaultZoneId.toString());
        result.put("sindex",String.valueOf(sindex));
        result.put("eindex",String.valueOf(eindex));
//        result.put("defaultZoneId",defaultZoneId.toString());

        return result;
    }

    @GetMapping("/api/1.0/setalbumcover/{alcoid}/{albumid}/{cdid}")
//    Map<String, String> GetAlbumSetCover(
    public void GetAlbumSetCover(
            @PathVariable Map<String, String> pathVarsMap
//            @PathVariable("alcoid") Long alcoid,
//            @PathVariable("albumid") Long albumid,
//            @PathVariable("cdid") Long cdid
    ) {
        // для сайту - запит та асінхронна обробка. https://www.cat-in-web.ru/fetch-async-await/
        Long alcoid = Long.parseLong(pathVarsMap.get("alcoid"));
        Long albumid = Long.parseLong(pathVarsMap.get("albumid"));
        Long cdid = Long.parseLong(pathVarsMap.get("cdid"));

        Clientdetail cd = clientService.GetClientDetailById(cdid);
        Album album = createrService.GetAlbumById(albumid);
        // todo переробити роботу зі сховищем на uuid
        Albumсover albumсover = createrService.GetAlbumCoverById(alcoid);
        album.setAlbumcover(albumсover);
//        albumсover.setStoreuuid();
        createrService.SaveAlbum(album);


//        Map<String,String> result;
//        return result;
    }


}
