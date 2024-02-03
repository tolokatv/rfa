package media.toloka.rfa.radio.model.enumerate;

public enum ECardInfoType {
    CARDINFO_type1("CARDINFO_type1"),
    CARDINFO_type2("CARDINFO_type2");

    public final String label;

    private ECardInfoType(String label) {
        this.label = label;
    }
}
