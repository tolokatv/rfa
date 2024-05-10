package media.toloka.rfa.radio.model.enumerate;

public enum EContractStatus {
// TODO винести назву типу контракту до application.property
// Це для інтернаціоналізації інтерфейсу
    CONTRACT_FREE("Безкоштовний"),
    CONTRACT_PAY("Комерційний");

    public final String label;

    private EContractStatus(String label) {
        this.label = label;
    }
}
