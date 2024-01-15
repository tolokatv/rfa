package media.toloka.rfa.radio.message.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.security.model.Users;


import java.util.List;

@Data
@Entity
@Table
public class Rooms {
    @Id
    @GeneratedValue
    @Column
    private Long id;
//    @OneToMany
//    private List<Users> usersList ;
    @OneToMany
    private List<Messages> messages;
    private  String name;
}
