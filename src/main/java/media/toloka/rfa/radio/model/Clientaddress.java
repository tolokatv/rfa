package media.toloka.rfa.radio.model;


//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.EClientAddressType;
//import javax.persistence.Id;

@Data
@Entity
//@Table
public class Clientaddress {

    @Id
    @GeneratedValue
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

    @ToString.Exclude
    @ManyToOne(optional = false, cascade = {CascadeType.ALL}) //, cascade = {CascadeType.ALL}
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

}
