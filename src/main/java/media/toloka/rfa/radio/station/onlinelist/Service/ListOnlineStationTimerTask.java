package media.toloka.rfa.radio.station.onlinelist.Service;

//import lombok.var;
import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.station.onlinelist.Model.ListOnlineFront;
import media.toloka.rfa.radio.station.service.StationOnlineList;
import media.toloka.rfa.radio.station.service.StationService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.json.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.TimerTask;

@Service
public class ListOnlineStationTimerTask extends TimerTask {

    @Autowired
    private StationService stationService;
/*
Клас, який періодично оновлює список онлайн станцій та ефірну інформацію (назва треку, автор)
Ініціалізація відбувається при старті застосунку.
 */

    @Override
    public void run() {
        // беремо станції, які знаходяться онлайн
        List<Station> stationOnlineList = stationService.GetListStationByStatus(true);
        // беремо перелік станцій для фронту
        List<ListOnlineFront> listOnlineFronts = StationOnlineList.getInstance().GetOnlineList();

        listOnlineFronts.clear();

        for (Station stationOnline : stationOnlineList) {
            // беремо зі станції поточний трек
            URL url;
            try {
                url = new URL("https://" + stationOnline.getUuid() + ".rfa.toloka.media/api/live-info/?callback");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            try {
                String json = IOUtils.toString(url, Charset.forName("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // json string to JSON

            // Заповнюємо екземпляр станції
            ListOnlineFront lof = new ListOnlineFront();
            lof.setUuid(stationOnline.getUuid());
            lof.setBdname(stationOnline.getDbname());
            lof.setStationname(stationOnline.getName());

            listOnlineFronts.add(lof);

            // return new JSONObject(json);
        }


    }
}
