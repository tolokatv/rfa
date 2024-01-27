package media.toloka.rfa.radio.station.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table
public class Poolport {
    @Id
    @GeneratedValue
    private Long id;
    private Integer port;
    private EServerPortType porttype;

    @ManyToOne
    private Station station;
}

