package media.toloka.rfa.radio.model.enumerate;

public enum EPostStatus {
    POSTSTATUS_REDY("POSTSTATUS_REDY"),
    POSTSTATUS_PUBLICATE("POSTSTATUS_PUBLICATE"),
    POSTSTATUS_REJECT("POSTSTATUS_REJECT"),
    POSTSTATUS_DELETE("POSTSTATUS_DELETE"),
    POSTSTATUS_REQUEST("POSTSTATUS_REQUEST");

    public final String label;

    private EPostStatus(String label) {
        this.label = label;
    }
}
