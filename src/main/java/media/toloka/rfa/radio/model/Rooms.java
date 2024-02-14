package media.toloka.rfa.radio.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
//import media.toloka.rfa.security.model.Users;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Expose
    private String uuid= UUID.randomUUID().toString();

    @Expose
    private  String name;

//    @OneToMany(mappedBy = "room")
//    private List<Messages> messages = new ArrayList<>();
}
