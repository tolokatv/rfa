package media.toloka.rfa.radio.model.enumerate;

public enum EClientAddressType {
    ADDRESS_PRIVATE("ADDRESS_PRIVATE"),
    ADDRESS_LOCATE_STATION("ADDRESS_LOCATE_STATION"),
    ADDRESS_OFFICIAL("ADDRESS_OFFICIAL"),
    ADDRESS_POSTAL("ADDRESS_POSTAL");

    public final String label;

    private EClientAddressType(String label) {
        this.label = label;
    }
}
