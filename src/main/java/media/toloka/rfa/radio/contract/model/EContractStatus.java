package media.toloka.rfa.radio.contract.model;

public enum EContractStatus {
// TODO винести низву типу контракту до application.property
    CONTRACT_FREE("Безкоштовний"),
    CONTRACT_PAY("Комерційний");

    public final String label;

    private EContractStatus(String label) {
        this.label = label;
    }
}
