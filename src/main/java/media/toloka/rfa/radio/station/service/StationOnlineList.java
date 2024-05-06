package media.toloka.rfa.radio.station.service;

import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.station.onlinelist.Model.ListOnlineFront;

import java.util.List;

public class StationOnlineList {
/*
Сінглтон, який тримає List для висвітлення на Front списку онлайн станцій, треків та авторів
*/

    private static StationOnlineList stationOnlineList; // екземпляр класу

    private static List<ListOnlineFront> listOnline; // екземпляр списку

    /**
     * Private constructor - can not be instantiated.
     */
    private StationOnlineList() {}

    /**
     * Method for getting current / new instance of Logging Singleton.
     *
     * @return single instance of logging class.
     */
    public static synchronized StationOnlineList getInstance() {
        if (stationOnlineList == null) {
            stationOnlineList = new StationOnlineList();
        }
        return stationOnlineList;
    }

    public static List<ListOnlineFront> GetOnlineList() {
        return listOnline;
    }

    public void ClearListOnline() { listOnline.clear(); }

}
