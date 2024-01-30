package media.toloka.rfa.radio.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Data
@Entity
@Table
public class Rooms {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @OneToMany
    private List<Messages> messages;
    private  String name;
}
