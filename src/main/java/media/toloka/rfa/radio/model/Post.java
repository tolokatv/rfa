package media.toloka.rfa.radio.model;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.EPostStatus;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private String uuid= UUID.randomUUID().toString();
    @Expose
    private String posttitle;
    @Expose
    @Column(columnDefinition = "TEXT")
    private String postbody;
    @Expose
    private Date createdate = new Date();
    @Expose
    private Date publishdate;
    @Expose
    private EPostStatus postStatus;
    @Expose
    private Date enddate;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

    @Expose
    private Boolean apruve = false;
    @Expose
    private Date apruvedate;
//    @Expose
//    private Clientdetail apruveuser;

}
