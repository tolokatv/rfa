package media.toloka.rfa.media.store.model;

public enum EStoreFileType {

    STORE_FILE("Файл"),
    STORE_DOCUMENT("Документ"),
    STORE_TRACK("Трек"),
    STORE_ALBUMCOVER("Обкладинка альбому"),
    STORE_POSTCOVER("Обкладинка посту");

    public final String label;

    private EStoreFileType(String label) {
        this.label = label;
    }
}
