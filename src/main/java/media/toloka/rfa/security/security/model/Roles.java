package media.toloka.rfa.security.security.model;

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

}