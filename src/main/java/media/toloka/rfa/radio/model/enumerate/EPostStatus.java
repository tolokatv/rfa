package media.toloka.rfa.radio.model.enumerate;

public enum EPostStatus {
    POSTSTATUS_REDY("Готовий"),
    POSTSTATUS_PUBLICATE("Опубліковано"),
    POSTSTATUS_REJECT("Відхилено"),
    POSTSTATUS_DELETE("Видалено"),
    POSTSTATUS_REQUEST("Запит на публікацію");

    public final String label;

    private EPostStatus(String label) {
        this.label = label;
    }
}
