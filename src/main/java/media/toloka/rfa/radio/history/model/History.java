package media.toloka.rfa.radio.history.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.history.model.EHistoryType;
import media.toloka.rfa.security.security.model.Users;

import java.time.LocalDateTime;

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
    private LocalDateTime   dateAction;
    private String          action;
    private String          comment;

    @ManyToOne(cascade = CascadeType.ALL)
    private Users user;

}
