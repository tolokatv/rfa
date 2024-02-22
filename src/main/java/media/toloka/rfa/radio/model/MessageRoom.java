package media.toloka.rfa.radio.model;


import com.google.gson.annotations.Expose;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table
public class MessageRoom {
    @Id
    @GeneratedValue
    @Expose
    private Long id;
    @Expose
    private String uuid= UUID.randomUUID().toString();
    @Expose
    private String roomname;

}
