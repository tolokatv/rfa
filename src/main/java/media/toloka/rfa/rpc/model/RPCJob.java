package media.toloka.rfa.rpc.model;

//import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import media.toloka.rfa.security.model.Users;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
@ToString
public class RPCJob {
    private ERPCJobType rJobType;
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date rjobdate;
    private Users user;
    private Queue<ERPCJobType> jobchain;
    private String rjobdata;
    private String jobresilt;

    public RPCJob() {
        this.setJobchain(new LinkedList<ERPCJobType>());
    }
}
