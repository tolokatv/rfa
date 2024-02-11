package media.toloka.rfa.radio.store.model;

public enum EStoreFileType {

    STORE_FILE("STORE_FILE"),
    STORE_DOCUMENT("STORE_DOCUMENT"),
    STORE_TRACK("STORE_TRACK"),
    STORE_ALBUMCOVER("STATION_ALBUMCOVER");

    public final String label;

    private EStoreFileType(String label) {
        this.label = label;
    }
}
