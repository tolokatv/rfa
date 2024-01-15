package media.toloka.rfa.radio.document.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.security.model.Users;


import java.time.LocalDateTime;

@Data
@Entity
//@Table(name="user_documents")
public class Documents {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime loadDate;
    private String pathToDocument;
    private EDocumentStatus status; // 0-завантажено, 1-завантажено, 2-на розгляді, 3 схвалено, 4 відхилено
    private String documentType;
    private String userComment;
    private String adminComment;
//    @ElementCollection
    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Clientdetail clientdetail;
}
