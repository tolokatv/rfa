package media.toloka.rfa.radio.model;


import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.ECardInfoType;
import media.toloka.rfa.security.model.Users;

import java.util.Date;

@Data
@Entity
//@Table(name="history")
@ToString
public class MediaCardInfo {

    @Expose
    @Id
    @GeneratedValue
    private Long id;
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
    private Date createdate;
    @Expose
    private Date startdate;
    @Expose
    private Date enddate;
    @Expose
    private Clientdetail clientdetail;
    @Expose
    private boolean apruve;
    @Expose
    private Clientdetail apruvercd;

}
