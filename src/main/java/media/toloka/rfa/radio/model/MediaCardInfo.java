package media.toloka.rfa.radio.model;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.ECardInfoType;
import media.toloka.rfa.security.model.Users;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(indexes = @Index(columnList = "uuid"))
@ToString
public class MediaCardInfo {

    @Expose
    @Id
    @GeneratedValue
    private Long id;
    @Expose
    private String uuid= UUID.randomUUID().toString();
    @Expose
    private ECardInfoType cardtype;
    @Expose
    private String image;
    @Expose
    private String header;
    @Expose
    private String link;
    @Column(columnDefinition = "TEXT")
    @Expose
    private String shorttext = " ";
    @Column(columnDefinition = "TEXT")
    @Expose
    private String body = " ";
    @Expose
    private Long nlike;
    @Expose
    private Long vview;
    @Expose
    private Date createdate = new Date();
    @Expose
    private Date startdate= new Date();
    @Expose
    private Date enddate;
//    @Expose
//    private Clientdetail clientdetail;
//    @Expose
//    private boolean apruve;
//    @Expose
//    private Clientdetail apruvercd;

//    @ToString.Exclude
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "clientdetail_id")
//    private Clientdetail clientdetail;

}
