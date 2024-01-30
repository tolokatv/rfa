package media.toloka.rfa.radio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.EHistoryType;


import java.util.Date;

@Data
@Entity
//@Table(name="history")
@ToString
public class History {

    @Id
    @GeneratedValue
//    @Column(name="id")
    private Long            id;
    private EHistoryType historyType;
    private Date dateAction;
    private String          action;
    private String          comment;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

    public History() {
        this.dateAction = new Date();
    }

}
