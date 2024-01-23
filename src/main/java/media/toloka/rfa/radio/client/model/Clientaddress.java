package media.toloka.rfa.radio.client.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
//@Table(name="user-address")
public class Clientaddress {

// назва вулиці,
// номер будинку,
// номер корпусу чи офісу (за потреби),
// назва населеного пункту,
// району,
// області,
// поштовий індекс.
// Окрім того, передбачено можливість зазначення двох адрес – юридичної (зазначеної в ЄДРПОУ) та фактичної (для листування)
// у разі, якщо місцезнаходження юридичної особи відрізняється від фактичного здійснення діяльності чи розміщення офісу.

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;
    private EClientAddressType userAddressType;
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

    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;
//    @OneToOne(cascade = CascadeType.ALL)
//    @Column(nullable = false)
//    @ElementCollection
//    @ManyToOne
//    private Clientdetail clientdetail;
//    @OneToOne
//    private Users user;

}
