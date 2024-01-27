package media.toloka.rfa.radio.station.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.contract.model.Contract;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table
public class Station {

    @Id
    @GeneratedValue
    private Long id;
    private EServerState serverState;
    private String name;
    private String dbname;
    private boolean enable = false;
    private LocalDateTime startdate;
    private LocalDateTime createdate;
    private LocalDateTime lastpaydate;
    private LocalDateTime lastchangedate;
    public Integer guiport;
    public String guiserver;
    public Integer main;
    public Integer show;
    private String uuid;
    private String icecastname;
    private String icecastdescription;
    private String icecastsite;
    private String icecastgenre;

    @ManyToOne(cascade = CascadeType.ALL)
    private transient Contract contract;

    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;

    @OneToMany
    private List<Poolport> ports;


    public Station() {
        this.uuid= UUID.randomUUID().toString();
        this.name=this.uuid;
        this.createdate=LocalDateTime.now();
        this.lastchangedate=LocalDateTime.now();
    }
}

