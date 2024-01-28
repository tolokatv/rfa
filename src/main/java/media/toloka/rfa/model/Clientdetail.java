package media.toloka.rfa.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.security.model.Users;


import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table
public class Clientdetail {
    @Id
    @GeneratedValue
    @Column
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
    private Date confirmDate;
    @Column
    private Date createdate;
    @Column
    private String comments;

//    @Expose
//    @ElementCollection
//    @OneToMany(cascade = CascadeType.ALL)
//    private transient List<Clientaddress> clientaddressList;

//    @Expose
//    @ElementCollection
//    @OneToMany(cascade = CascadeType.ALL)
//    private transient List<Contract> contractList;

//    @Expose
//    @ElementCollection
//    @OneToMany(cascade = CascadeType.ALL)
//    private transient List<History> historyList;

//    @Expose
//    @ElementCollection
//    @OneToMany(cascade = CascadeType.ALL)
//    private transient List<Documents> documentslist;

//    @Expose
//    @ElementCollection
//    @OneToMany(cascade = CascadeType.ALL)
//    private transient List<Station> stationList;

//    @Expose
//    @ElementCollection
//    @Column(unique=true)


//    @PrimaryKeyJoinColumn
//    @OneToOne(cascade = CascadeType.ALL)
    private  Long user;

//    @ElementCollection
//    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Station> stationList;

    public Clientdetail() {
//        this.clientaddressList  = new ArrayList<Clientaddress>();
//        this.contractList       = new ArrayList<Contract>();
//        this.historyList        = new ArrayList<History>();
//        this.documentslist      = new ArrayList<Documents>();
//        this.stationList        = new ArrayList<Station>();
        this.uuid               = UUID.randomUUID().toString();
        this.createdate         = new Date();
        this.confirminfo        = false;
//        this.comments           = "";
//        this.custname           = "";
//        this.custsurname        = "";
//        this.firmname           = "";
    }

}

