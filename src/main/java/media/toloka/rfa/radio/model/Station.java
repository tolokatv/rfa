package media.toloka.rfa.radio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.EServerState;


import java.util.ArrayList;
import java.util.Date;
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
    private Date startdate;
    private Date createdate;
    private Date lastpaydate;
    private Date lastchangedate;
    public Integer guiport;
    public String guiserver;
    public Integer main;
    public Integer show;
    private String uuid;
    private String icecastname;
    private String icecastdescription;
    private String icecastsite;
    private String icecastgenre;
//    private transient Contract contract;


    @ToString.Exclude
    @ManyToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

    @ToString.Exclude
    @ManyToOne(optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @OneToMany(mappedBy = "station", fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<Poolport> ports = new ArrayList<>();


    public Station() {
        this.uuid= UUID.randomUUID().toString();
        this.name=this.uuid;
        this.createdate=new Date();
        this.lastchangedate=new Date();
    }
}

