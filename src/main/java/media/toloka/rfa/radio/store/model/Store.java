package media.toloka.rfa.radio.store.model;
// https://paulcwarren.github.io/spring-content/refs/release/1.2.4/fs-index.html


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.radio.model.Clientdetail;

import java.util.Date;

@Data
@Entity
public class Store {
        @Id
        @GeneratedValue
        @Expose
        private Long id;
        private EStoreFileType storefiletype;
        private String filename;
        private String filepatch;
        private String description;
        private Date createdate = new Date();

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "clientdetail_id")
    private Clientdetail clientdetail;

}
