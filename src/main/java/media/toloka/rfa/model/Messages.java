package media.toloka.rfa.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.model.Clientdetail;


import java.util.Date;

@Data
@Entity
@Table
public class Messages {
    @Id
    @GeneratedValue
    @Column
    private Long id;
//    @ManyToOne
    private Clientdetail from; // from user
//    @ManyToOne
    private Clientdetail tom; // to user
    private boolean reading;
    private Date send;
    private Date read;
    @Column(columnDefinition = "TEXT")
    private String body;
//    @ManyToOne
    private Rooms room;
}