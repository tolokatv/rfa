package media.toloka.rfa.media.messanger.model.enumerate;

public enum EChatRecordType {
    RECORD_TYPE_MEDIA("RECORD_TYPE_MEDIA"),
    RECORD_TYPE_TEXT("RECORD_TYPE_TEXT");

    public final String label;

    private EChatRecordType(String label) {
        this.label = label;
    }
}
