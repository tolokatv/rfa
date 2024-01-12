package media.toloka.rfa.radio.root.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name="site_message")
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
    private LocalDateTime contact_datetime;

}