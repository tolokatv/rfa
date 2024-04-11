package media.toloka.rfa.radio.store.model;

public enum EStoreFileType {

    STORE_FILE("Файл"),
    STORE_DOCUMENT("Документ"),
    STORE_TRACK("Трек"),
    STORE_ALBUMCOVER("Обкладинка альбому"),
    STORE_POSTCOVER("Обкладинка посту"),
    STORE_PHOTO("Фото профайлу");


    public final String label;

    private EStoreFileType(String label) {
        this.label = label;
    }
}
