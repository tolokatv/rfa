package media.toloka.rfa.security.model;

public enum ERole {
//    ROLE_USER("User"),
//    ROLE_CREATOR("Creator"),
//    ROLE_MODERATOR("Moderator"),
//    ROLE_EDITOR("Editor"),
//    ROLE_ADMIN("Admin");
    ROLE_UNKNOWN("ROLE_UNCNOWN"),
    ROLE_USER("ROLE_USER"),
    ROLE_CREATER("ROLE_CREATER"),
    ROLE_MODERATOR("ROLE_MODERATOR"),
    ROLE_EDITOR("ROLE_EDITOR"),
    ROLE_ADMIN("ROLE_ADMIN");

    public final String label;

    private ERole(String label) {
        this.label = label;
    }
}