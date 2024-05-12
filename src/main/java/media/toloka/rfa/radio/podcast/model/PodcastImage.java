package media.toloka.rfa.radio.podcast.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import media.toloka.rfa.radio.store.model.Store;

import java.util.UUID;

@Data
@Entity
@Table(indexes = @Index(columnList = "uuid"))
public class PodcastImage {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    private String uuid = UUID.randomUUID().toString();
    private String title;
    private String url;
    private String link;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "store_id")
    private Store storeid;
}
