package media.toloka.rfa.radio.model.enumerate;

public enum EUserStatus {
    USERSTATUS_ADDRESS("Схвалити адресу"),
    USERSTATUS_DOCUMENTS("Схвалити документи"),
    USERSTATUS_ENABLE("Відхилити"),
    USERSTATUS_DISABLE("Заборонити"),
    USERSTATUS_BLOCK("Заблокувати");

    public final String label;

    private EUserStatus(String label) {
        this.label = label;
    }
}
