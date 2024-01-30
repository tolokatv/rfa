package media.toloka.rfa.rpc.model;

//import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.Expose;
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
    @Expose
    private ERPCJobType rJobType;
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Expose
    private Date rjobdate;
    @Expose
    private Users user;
    @Expose
    private Queue<ERPCJobType> jobchain;
    @Expose
    private String rjobdata;
    @Expose
    private String jobresilt;

    public RPCJob() {
        this.rjobdate = new Date();
        this.jobchain = new LinkedList<ERPCJobType>();
    }
}
