package media.toloka.rfa.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
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
    private Long user;

    public Clientdetail() {
        this.uuid               = UUID.randomUUID().toString();
        this.createdate         = new Date();
        this.confirminfo        = false;
    }
//    @Expose
//    @ElementCollection
//    @Column(unique=true)
}

