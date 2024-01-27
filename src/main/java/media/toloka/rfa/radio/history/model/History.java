package media.toloka.rfa.radio.history.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.security.model.Users;

import java.time.LocalDateTime;
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

    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;

    public History() {
        this.dateAction = new Date();
    }

}
