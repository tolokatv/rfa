package media.toloka.rfa.radio.model;

import jakarta.persistence.*;
import lombok.Data;
//import media.toloka.rfa.security.model.Users;


import java.util.ArrayList;
import java.util.List;

@Data
@Entity
//@Table
public class Rooms {
    @Id
    @GeneratedValue
    @Column
    private Long id;
//    @OneToMany
//    private List<Users> usersList ;

    private  String name;

//    @OneToMany(mappedBy = "room")
//    private List<Messages> messages = new ArrayList<>();
}
