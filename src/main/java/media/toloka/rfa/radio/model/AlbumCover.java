package media.toloka.rfa.radio.model;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class AlbumCover {
    @Id
    @GeneratedValue
    @Expose
    private Long id;

    @Expose
    private String albumcoverfile;

    @Expose
    private String patch;

    @Expose
    private String albumcoverdateupload = new Date().toString();

    @Expose
    @Column(columnDefinition = "TEXT")
    private String description;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

}
