package media.toloka.rfa.radio.model;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Album {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private String uuid= UUID.randomUUID().toString();

    @Expose
    private String name;

    @Expose
    private String autor;

    @Expose
    private Date createdate = new Date();

    @Expose
    private String albumrelisedate = new Date().toString();

    @Expose
//    private AlbumCover albumcover;
    private Long albumcoverid;

    @Expose
    private String albumcoverdateupload = new Date().toString();

    @Expose
    private String style;

    @Expose
    @Column(columnDefinition = "TEXT")
    private String description;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

    @ToString.Exclude
    @OneToMany(mappedBy = "album", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Track> track;


}
