package media.toloka.rfa.radio.document.model;

public enum EDocumentStatus {

    // TODO Винести найменування статусу документа до application.properties
    // 0-невідомо, 1-завантажено, 2-на розгляді, 3 схвалено, 4 відхилено

    STATUS_UNKNOWN("Невідомо"),
    STATUS_LOADED("Завантажено"),
    STATUS_REVIEW("На розгляді"),
    STATUS_APPROVED("Схвалено"),
    STATUS_REJECTED("Відхилено");

    public final String label;

    private EDocumentStatus(String label) {
        this.label = label;
    }
}