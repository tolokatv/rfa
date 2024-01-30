package media.toloka.rfa.security.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.model.Clientdetail;

import java.util.ArrayList;
import java.util.List;


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
//    @OneToMany(mappedBy="user", FetchType.EAGER, cascade = CascadeType.ALL)
    @OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<Roles> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    private Clientdetail clientdetail = new Clientdetail();
}
