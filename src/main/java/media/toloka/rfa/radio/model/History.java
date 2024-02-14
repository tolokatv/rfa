package media.toloka.rfa.radio.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.EHistoryType;


import java.util.Date;
import java.util.UUID;

@Data
@Entity
//@Table(name="history")
@ToString
public class History {

    @Expose
    @Id
    @GeneratedValue
//    @Column(name="id")
    private Long            id;
    @Expose
    private String uuid= UUID.randomUUID().toString();
    @Expose
    private EHistoryType historyType;
    @Expose
    private Date dateAction;
    @Expose
    private String          action;
    @Expose
    private String          comment;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

    public History() {
        this.dateAction = new Date();
    }

}
