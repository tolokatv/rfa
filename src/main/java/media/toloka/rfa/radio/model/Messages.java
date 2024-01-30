package media.toloka.rfa.radio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


import java.util.Date;

@Data
@Entity
@Table
public class Messages {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @ManyToOne(optional = false, cascade = {CascadeType.ALL}) //, cascade = {CascadeType.ALL}
    @JoinColumn(name = "from_id")
    private Clientdetail from; // from user
    @ManyToOne(optional = false, cascade = {CascadeType.ALL}) //, cascade = {CascadeType.ALL}
    @JoinColumn(name = "to_id")
    private Clientdetail to; // to user
    private boolean reading;
    private Date send;
    private Date read;
    @Column(columnDefinition = "TEXT")
    private String body;

//    @ManyToOne(cascade = {CascadeType.ALL}) //, cascade = {CascadeType.ALL}
//    @JoinColumn(name = "room_id")
//    private Rooms room;

//    @ToString.Exclude
//    @ManyToOne(optional = false, cascade = {CascadeType.ALL}) //, cascade = {CascadeType.ALL}
//    @JoinColumn(name = "clientdetail_id")
//    private Clientdetail clientdetail;
}