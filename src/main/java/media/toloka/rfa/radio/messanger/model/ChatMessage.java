package media.toloka.rfa.radio.messanger.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class ChatMessage {

    @Id
    @GeneratedValue
    @Expose
    private String uuid= UUID.randomUUID().toString();
    @Expose
    private Date send = new Date();
    @Expose
    private String fromname;
    @Expose
    private String fromuuid;
    @Expose
    private String toname;
    @Expose
    private String touuid;
    @Expose
    @Column(columnDefinition = "TEXT")
    private String body;
    @Expose
    private String roomuuid;

}
