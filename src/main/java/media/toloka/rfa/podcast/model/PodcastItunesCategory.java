package media.toloka.rfa.podcast.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table
public class PodcastItunesCategory {
    @Id
    @GeneratedValue
    @Expose
    private Long id;

    @Expose
    private String uuid = UUID.randomUUID().toString();

    @Expose
    private String firstlevel;

    @Expose
    private String secondlevel;

//    @ElementCollection
//    @ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.ALL})
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "podcast_channel_id")
    private PodcastChannel chanel;

}
