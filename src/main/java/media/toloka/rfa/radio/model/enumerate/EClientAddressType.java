package media.toloka.rfa.radio.model.enumerate;

public enum EClientAddressType {
    ADDRESS_PRIVATE("Приватна адреса"),
    ADDRESS_LOCATE_STATION("Адреса розташування редакції"),
    ADDRESS_OFFICIAL("Адреса реєстрації"),
    ADDRESS_POSTAL("Поштова адреса");

    public final String label;

    private EClientAddressType(String label) {
        this.label = label;
    }
}
