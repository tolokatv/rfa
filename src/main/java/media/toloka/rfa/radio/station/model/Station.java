package media.toloka.rfa.radio.station.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.contract.model.Contract;
import media.toloka.rfa.security.model.Users;

import java.time.LocalDateTime;

@Data
@Entity
@Table
public class Station {

    @Id
    @GeneratedValue
    private Long radio_id;
    private EServerState serverState;
    private String name;
    private String dbname;
    private boolean enable = false;
    private LocalDateTime startdate;
    private LocalDateTime createdate;
    private LocalDateTime lastpaydate;
    public Integer guiport;
    public String guiserver;
    public Integer main;
    public Integer show;
    private String uuid;
    private String icecastname;
    private String icecastdescription;
    private String icecastsite;
    private String icecastgenre;

//    @Expose
    @ManyToOne(cascade = CascadeType.ALL)
    private transient Contract contract;


    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;

}

