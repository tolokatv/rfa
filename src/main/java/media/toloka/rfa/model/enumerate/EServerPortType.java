package media.toloka.rfa.model.enumerate;

public enum EServerPortType {

    PORT_FREE("PORT_FREE"),
    PORT_MAIN("PORT_MAIN"),
    PORT_SHOW("PORT_SHOW"),
    PORT_WEB("PORT_WEB");

    public final String label;

    private EServerPortType(String label) {
        this.label = label;
    }
}
