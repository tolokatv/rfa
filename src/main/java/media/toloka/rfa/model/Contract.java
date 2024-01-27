package media.toloka.rfa.radio.contract.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.security.model.Users;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static media.toloka.rfa.radio.contract.model.EContractStatus.CONTRACT_FREE;

@Data
@Entity
//@Table(name="user_contracts")
public class Contract {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;
    private EContractStatus contractStatus;
    private String number;
    private String uuid;
    private Date createDate;
    private Date lastPayDate;
    private String contractname;
    private String usercomment;

    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;

//    @Expose
    @OneToMany
    private transient List<Station> stationList;

    //    @ElementCollection
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Station> listStation;

//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Radio> radios;

//    @OneToMany(cascade = CascadeType.ALL)
//    private List<History> history;


    public Contract() {
        this.contractStatus         = CONTRACT_FREE;
        this.number                 = UUID.randomUUID().toString();
        this.uuid                   = UUID.randomUUID().toString();
        this.createDate             = new Date();
        this.lastPayDate            = null;
        this.usercomment            = "";
//        this.listStation            = new ArrayList<Station>();
    }

}
