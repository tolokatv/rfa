package media.toloka.rfa.radio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.EDocumentStatus;

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
    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

    public Documents() {
        this.loaddate = new Date();
        this.status = EDocumentStatus.STATUS_LOADED;
    }
}
