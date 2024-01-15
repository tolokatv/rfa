package media.toloka.rfa.rpc.model;

//import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.security.model.Users;

import java.time.LocalDateTime;

@Data
@ToString
public class RPCJob {
    private ERPCJobType rJobType;
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rjobdate;
    private Users user;
    private String rjobdata;
    private String jobresilt;
}
