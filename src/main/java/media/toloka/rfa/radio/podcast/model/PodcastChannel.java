package media.toloka.rfa.radio.podcast.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.store.model.Store;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(indexes = @Index(columnList = "uuid"))
public class PodcastChannel {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private String uuid = UUID.randomUUID().toString();
    @Expose
    private String title;
    @Expose
    private String link;
    @Expose
    @Column(columnDefinition = "TEXT")
    private String description;
    @Expose
    @DateTimeFormat(pattern = "dd-MM-yy")
    private Date lastbuilddate = new Date();
    @Expose
    private String language;
    @Expose
    private String copyright;
    @Expose
    @DateTimeFormat(pattern = "dd-MM-yy")
    private Date date = new Date();

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "podcast_image_id")
    private PodcastImage image;

    @ElementCollection
    @OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<PodcastItem> item = new ArrayList<>();

//    @Expose
//    @OneToOne(cascade = {CascadeType.ALL})
//    @JoinColumn(name = "store_id")
//    private Store imagestoreitem;

//    @Expose
//    private String imagestoreuuid;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;
}
