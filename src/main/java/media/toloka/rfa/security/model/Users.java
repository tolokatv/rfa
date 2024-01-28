package media.toloka.rfa.security.model;

import jakarta.persistence.*;
import lombok.Data;

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
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Roles> roles;

}
