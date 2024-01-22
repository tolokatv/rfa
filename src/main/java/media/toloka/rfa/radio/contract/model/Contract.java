package media.toloka.rfa.radio.contract.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.security.model.Users;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
//@Table(name="user_contracts")
public class Contract {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;
    private EContractStatus contractStatus;
    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;
    private String number;
    private String uuid;
    private LocalDateTime createDate;
    private LocalDateTime lastPayDate;
    private String usercomment;
    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL)
    private List<Station> listStation;
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Radio> radios;
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<History> history;

}
