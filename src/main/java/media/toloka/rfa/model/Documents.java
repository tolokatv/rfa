package media.toloka.rfa.model;

import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.model.enumerate.EDocumentStatus;

import java.util.Date;

@Data
@Entity
//@Table(name="user_documents")
public class Documents {

    @Id
    @GeneratedValue
    private Long id;
    private Date loaddate;
    private String pathToDocument;
    private EDocumentStatus status;
    private String documenttype;
    @Column(columnDefinition="TEXT")
    private String userComment;
    @Column(columnDefinition="TEXT")
    private String adminComment;
//    @ElementCollection
//    @ManyToOne(cascade = CascadeType.ALL)
    private Clientdetail clientdetail;

    public Documents() {
        this.loaddate = new Date();
        this.status = EDocumentStatus.STATUS_LOADED;
    }
}
