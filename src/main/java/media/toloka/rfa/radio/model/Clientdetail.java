package media.toloka.rfa.radio.model;

//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.security.model.Users;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
//@Table
public class Clientdetail {
    @Id
    @GeneratedValue
    private Long id;
    private String custname;
    private String custsurname;
    private String firmname;
    private String uuid;
    private Boolean confirminfo;
    private Date confirmDate;
    private Date createdate;
    private String comments;

//    @Expose
//    @ElementCollection
    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = {CascadeType.ALL}) //,cascade = {CascadeType.ALL},fetch= FetchType.EAGER)
    private  List<Clientaddress> clientaddressList = new ArrayList<>();

    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = {CascadeType.ALL}) //,cascade = {CascadeType.ALL},fetch= FetchType.EAGER)
    private  List<Contract> contractList = new ArrayList<>();

    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<History> historyList = new ArrayList<>();

    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private  List<Documents> documentslist = new ArrayList<>();

    @OneToMany(mappedBy = "clientdetail", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private  List<Station> stationList = new ArrayList<>();

//    @Expose
//    @ElementCollection
//    @Column(unique=true)


//    @PrimaryKeyJoinColumn
@OneToOne(cascade = {CascadeType.ALL})
private  Users user;
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

