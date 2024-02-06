package media.toloka.rfa.radio.model;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Album {
    @Id
    @GeneratedValue
    @Expose
    private Long id;

    @Expose
    private String name;

    @Expose
    private Date createdate;

    @Expose
    private Date albumrelisedate;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

    @ToString.Exclude
    @OneToMany(mappedBy = "album", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Track> track;


}
