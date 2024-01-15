package media.toloka.rfa.radio.document.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.security.model.Users;


import java.time.LocalDateTime;

@Data
@Entity
//@Table(name="user_documents")
public class IDDocuments {

    @Id
    @GeneratedValue
//    @Column(name="id")
//    @ManyToOne(cascade = CascadeType.ALL)
    private Long id;
//    @ManyToOne(cascade = CascadeType.ALL)
    private LocalDateTime loadDate;
    private String pathToDocument;
    private EDocumentStatus status; // 0-завантажено, 1-завантажено, 2-на розгляді, 3 схвалено, 4 відхилено
    private String documentType;
    private String userComment;
    private String adminComment;
//    @ElementCollection
    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Users user;
}
