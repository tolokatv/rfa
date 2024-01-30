package media.toloka.rfa.radio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;


@Data
@Entity
@Table(name="rootsitemessage")
public class MessageFromSite {

// приймаємо повідомлення з форми на сайті
// додати можливість завантаження декількох файлів

    @Id
    @GeneratedValue
    private Long contact_id;
    private String name;
    private String email;
    private String phone;
    private String message;
    private Date contact_datetime;

}