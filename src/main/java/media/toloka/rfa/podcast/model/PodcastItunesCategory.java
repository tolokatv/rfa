package media.toloka.rfa.podcast.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class PodcastItunesCategory {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private String firstlevel;
    @Expose
    private String secondlevel;




}
