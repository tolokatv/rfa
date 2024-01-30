package media.toloka.rfa.radio.model;

import com.google.gson.annotations.Expose;
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
    @Expose
    private Long id;
    @Expose
    private Date loaddate;
    @Expose
    private String pathToDocument;
    @Expose
    private EDocumentStatus status;
    @Expose
    private String documenttype;
    @Expose
    @Column(columnDefinition="TEXT")
    private String userComment;
    @Expose
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
