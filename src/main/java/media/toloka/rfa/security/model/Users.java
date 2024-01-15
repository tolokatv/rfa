package media.toloka.rfa.security.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.client.model.Clientdetail;

import java.util.List;
//import jakarta.persistence.*;
//import lombok.Data;


@Data
@Entity
//@Table
public class Users {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column( nullable = false)
    private String password;
    @Column
    @ElementCollection
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Roles> roles;

}
