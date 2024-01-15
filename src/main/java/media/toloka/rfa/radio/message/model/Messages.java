package media.toloka.rfa.radio.message.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.security.model.Users;


import java.time.LocalDateTime;

@Data
@Entity
@Table
public class Messages {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @ManyToOne
    private Users userf; // from user
    @ManyToOne
    private Users usert; // to user
    private boolean reading;
    private LocalDateTime send;
    private LocalDateTime read;
    @Column(columnDefinition="TEXT")
    private  String body;
}
