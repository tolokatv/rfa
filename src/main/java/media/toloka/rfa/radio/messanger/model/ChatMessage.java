package media.toloka.rfa.radio.messanger.model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import media.toloka.rfa.radio.model.Clientdetail;

import java.util.Date;
import java.util.UUID;

@Data
public class ChatMessage {
    @Expose
    private String uuid= UUID.randomUUID().toString();
    @Expose
    private Date send;
    @Expose
    private String fromname;
    @Expose
    private String fromuuid;
    @Expose
    private String toname;
    @Expose
    private String touuid;
//    private Clientdetail to; // to user
//    private Clientdetail from; // from user
    @Expose
    private String body;
    @Expose
    private String roomuuid;

}
