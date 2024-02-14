package media.toloka.rfa.radio.store.model;
// https://paulcwarren.github.io/spring-content/refs/release/1.2.4/fs-index.html


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.Clientdetail;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Store {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private String uuid = UUID.randomUUID().toString();
    @Expose
    private EStoreFileType storefiletype;
    @Expose
    private String filename;
    @Expose
    private String filepatch;
    @Expose
    private Long filelength;
    @Expose
    private String contentMimeType;
    @Expose
    private String description;
    @Expose
    private Date createdate = new Date();

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

}
