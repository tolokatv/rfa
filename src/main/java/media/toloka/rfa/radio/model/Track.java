package media.toloka.rfa.radio.model;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.enumerate.EDocumentStatus;

import java.util.Date;

@Data
@Entity
public class Track {
    @Id
    @GeneratedValue
    @Expose
    private Long id;

    @Expose
    private Date uploaddate = new Date();

    @Expose
    private String name;

    @Expose
    private String autor;

    @Expose
    private String patch;

    @Expose
    private String filename;

    @Expose
    @Column(columnDefinition = "TEXT")
    private String description;

    @Expose
    private String style;

    @Expose
    private Boolean notnormalvocabulary = false;

    @Expose
    private EDocumentStatus status;

    @ToString.Exclude
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "album_id")
    private Album album;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;
}
