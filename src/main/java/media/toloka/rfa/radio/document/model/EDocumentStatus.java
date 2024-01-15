package media.toloka.rfa.radio.document.model;

public enum EDocumentStatus {

    // 0-невідомо, 1-завантажено, 2-на розгляді, 3 схвалено, 4 відхилено
    STATUS_UNKNOWN("STATUS_UNCNOWN"),
    STATUS_LOADED("STATUS_LOADED"),
    STATUS_REVIEW("STATUS_REVIEW"),
    STATUS_APPROVED("STATUS_APPROVED"),
    STATUS_REJECTED("STATUS_REJECTED");

    public final String label;

    private EDocumentStatus(String label) {
        this.label = label;
    }
}