package media.toloka.rfa.radio.station.onlinelist.Model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

// клас для висвітлення на сайті станцій, які зараз онлайн з назвами станція, пісня, автор
@Data
public class ListOnlineFront {
    @Expose
    private String uuid;
    @Expose
    private String stationname;
    @Expose
    private String dbname;
    @Expose
    private String group;
    @Expose
    private String track;
    @Expose
    private Date curdate;
}


