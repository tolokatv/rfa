package media.toloka.rfa.radio.station.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
//    @ManyToOne
//    private Radio station;
}

