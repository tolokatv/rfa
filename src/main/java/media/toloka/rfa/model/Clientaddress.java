package media.toloka.rfa.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.model.enumerate.EClientAddressType;

@Data
@Entity
@Table
public class Clientaddress {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;
    private String shortaddress; // коротке найменування адреси
    private EClientAddressType userAddressType; // поштовий, офіційний, домашній
    private String street;
    private String buildnumber;
    private String korpus;
    private String appartment;
    private String cityname;
    private String area;
    private String region;
    private String country;
    private String zip;
    private String phone;
    private String comment;
    private boolean mainaddress;
    private boolean cheked;
//    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;

}
