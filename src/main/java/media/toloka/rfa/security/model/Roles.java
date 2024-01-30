package media.toloka.rfa.security.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table //(name="roles")
public class Roles {
    @Id
    @GeneratedValue
    private Long id;
    private ERole role;
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Users user;


}