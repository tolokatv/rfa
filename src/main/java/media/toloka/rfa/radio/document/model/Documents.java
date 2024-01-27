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
    private LocalDateTime loaddate;
    private String pathToDocument;
    private EDocumentStatus status;
    private String documenttype;
    @Column(columnDefinition="TEXT")
    private String userComment;
    @Column(columnDefinition="TEXT")
    private String adminComment;
//    @ElementCollection
    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;

    public Documents() {
        this.loaddate = LocalDateTime.now();
        this.status = EDocumentStatus.STATUS_LOADED;
    }
}
