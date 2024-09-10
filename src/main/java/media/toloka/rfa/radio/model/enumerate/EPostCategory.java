package media.toloka.rfa.radio.model.enumerate;

public enum EPostCategory {
    POST_ANONCE("Анонс"),
    POST_MUSIC("Музична стаття"),
    POST_GRANTS("Гранти"),
    POST_OTG("Новини громади"),
    POST_NEWS("Новина сайту"),
    POST_FREE("Будь що");

    public final String label;

    private EPostCategory(String label) {
        this.label = label;
    }
}
