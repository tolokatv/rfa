package media.toloka.rfa.rpc.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class ResultJob {
    @Expose
    private Long rc;
    @Expose
    private ERPCJobType job;

    public ResultJob (Long rc, ERPCJobType job) {
        this.rc = rc;
        this.job = job;
    }
}
