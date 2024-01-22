package media.toloka.rfa.radio.contract.model;

public enum EContractStatus {

    CONTRACT_FREE("CONTRACT_FREE"),
    CONTRACT_PAY("CONTRACT_PAY");

    public final String label;

    private EContractStatus(String label) {
        this.label = label;
    }
}
