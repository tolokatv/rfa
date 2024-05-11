package media.toloka.rfa.radio.podcast.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.store.model.Store;

import java.util.ArrayList;
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
    private String uuid = UUID.randomUUID().toString();
    private String title;
    private String link;
    private String description;
    private String lastBuildDate;
    private String language;
    private String copyright;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "podcast_image_id")
    private PodcastImage image;

    @ElementCollection
    @OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<PodcastItem> item = new ArrayList<>();

    @Expose
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "store_id")
    private Store imagestoreitem;

    @Expose
    private String imagestoreuuid;
}
