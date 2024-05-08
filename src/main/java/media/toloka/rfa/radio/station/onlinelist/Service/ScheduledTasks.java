package media.toloka.rfa.radio.station.onlinelist.Service;

//import lombok.var;
import com.google.gson.GsonBuilder;
import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.station.ClientHomeStationController;
import media.toloka.rfa.radio.station.onlinelist.Model.ListOnlineFront;
import media.toloka.rfa.radio.station.onlinelist.Model.StationReport;
import media.toloka.rfa.radio.station.service.StationOnlineList;
import media.toloka.rfa.radio.station.service.StationService;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Service
@Component
public class ScheduledTasks {
//public class ListOnlineStationTimerTask extends TimerTask {

    @Autowired
    private StationService stationService;
/*
Клас, який періодично оновлює список онлайн станцій та ефірну інформацію (назва треку, автор)
Ініціалізація відбувається при старті застосунку.
 */
//private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        logger.info("The time is now {}", dateFormat.format(new Date()));
//    }


    @Scheduled(fixedRate = 5000)
    public void GetStationOnlineList() {
//        logger.info("GetStationOnlineList: Start The time is now {}", dateFormat.format(new Date()));

        // беремо станції, які знаходяться онлайн
        List<Station> stationOnlineList = stationService.GetListStationByStatus(true);
        // беремо перелік станцій для фронту
        List<ListOnlineFront> listOnlineFronts = StationOnlineList.getInstance().GetOnlineList();

        Date currentUpdate = new Date();
        listOnlineFronts.clear();

        for (Station stationOnline : stationOnlineList) {
            // беремо зі станції поточний трек
            URL url;
            try {
                url = new URL("https://" + stationOnline.getUuid() + ".rfa.toloka.media/api/live-info/?callback");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            String json;
            try {
                json = IOUtils.toString(url, Charset.forName("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // json string to JSON
//            JSONArray jsonArray;
//            try {
//                jsonArray = new JSONArray(json);
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
            GsonBuilder builder = new GsonBuilder();
//            Object
            Object o = builder.create().fromJson(json, Object.class);
            Map<String, Object> Report = (Map<String, Object>)o;
//            Map<String, Object> previous = (Map<String, Object>) Report.get("previous");

            Map<String, Object> current = (Map<String, Object>) Report.get("current");
            if (current != null ) {
                Map<String, Object> currentmetadata = (Map<String, Object>) current.get("metadata");
                String currentmetadataTrackTitle = (String) currentmetadata.get("track_title");
                String currentmetadataArtistName = (String) currentmetadata.get("artist_name");

//            Map<String, Object> next = (Map<String, Object>) Report.get("next");
                // Заповнюємо екземпляр станції
                ListOnlineFront lof = new ListOnlineFront();
                lof.setUuid(stationOnline.getUuid());
                lof.setDbname(stationOnline.getDbname());
                lof.setStationname(stationOnline.getName());
                lof.setTrack(currentmetadataTrackTitle);
                lof.setGroup(currentmetadataArtistName);
                lof.setCurdate(currentUpdate);

                listOnlineFronts.add(lof);
            }
            // return new JSONObject(json);

        }
        for (ListOnlineFront tlof : listOnlineFronts) {
            if (tlof.getCurdate() != currentUpdate) {
                // видаляємо застарілі елементи
            }
        }
//        logger.info("GetStationOnlineList: END The time is now {}", dateFormat.format(new Date()));


    }
}
