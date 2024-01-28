package media.toloka.rfa.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.model.enumerate.EServerPortType;


@Data
@Entity
@Table
public class Poolport {
    @Id
    @GeneratedValue
    private Long id;
    private Integer port;
    private EServerPortType porttype;

//    @ManyToOne
    private Station station;
}

