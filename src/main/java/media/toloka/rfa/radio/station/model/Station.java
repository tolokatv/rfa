package media.toloka.rfa.radio.station.model;

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


    private String name;
    private boolean enable = false;
    private LocalDateTime startdate;
    private LocalDateTime createdate;
//    private Date enddate;
    private LocalDateTime lastpaydate;
//    @OneToOne(cascade = CascadeType.ALL)

    public Integer guiport;
//    @OneToOne(cascade = CascadeType.ALL)
    public Integer main;
//    @OneToOne(cascade = CascadeType.ALL)
    public Integer show;

//    @OneToMany(cascade = CascadeType.ALL)
//    public List<ServerPort> ports;
    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;
//    @ManyToOne(cascade = CascadeType.ALL)
//    private Contract contract;
    private String uuid;
//
//    @OneToMany
//    private List<History> history;

}

