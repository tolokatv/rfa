package media.toloka.rfa.rpc.model;

import lombok.Data;

@Data
public class ResultJob {
    private Long rc;
    private ERPCJobType job;

    public ResultJob (Long rc, ERPCJobType job) {
        this.rc = rc;
        this.job = job;
    }
}
