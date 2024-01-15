package media.toloka.rfa.radio.client.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.contract.model.Contract;
import media.toloka.rfa.radio.history.model.History;
import media.toloka.rfa.security.model.Users;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
//@Table(name="user-detail")
public class Clientdetail {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;
    @Column
    private String custname;
    @Column
    private String custsurname;
    @Column
    private String firmname;
    @Column
    private String uuid;
    @Column
    private Boolean confirminfo;
    @Column
    private LocalDateTime confirmDate;
    @Column
    private String comments;
//    @Column
    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL)
    private List<Clientaddress> clientaddressList;

    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL)
    private List<Contract> contractList;

    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL)
    private List<History> historyList;
//    @ElementCollection
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Contract> listcontract;
//    @Column
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Track> listTracks;
//
//    @Column
//    private List<Album> listAlbums;

//    @Column(unique = true, nullable = false)
//    @JoinColumn(name = "id")
//@OneToOne(cascade = CascadeType.ALL)
//    @OneToOne(mappedBy = "clientdetail")
    @ElementCollection
    @OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Users user;
}

