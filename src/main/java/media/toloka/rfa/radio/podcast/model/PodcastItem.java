package media.toloka.rfa.radio.podcast.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(indexes = @Index(columnList = "uuid"))
public class PodcastItem {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    private String uuid = UUID.randomUUID().toString();
    private String title;
    private String link;
    private String pubDate;
    private String comments;
    private String category;
    private String description;
    private String enclosure;

}
