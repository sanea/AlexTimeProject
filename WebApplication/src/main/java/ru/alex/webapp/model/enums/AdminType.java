package ru.alex.webapp.model.enums;

/**
 * @author Alexander.Isaenco
 */
public enum AdminType {
    NONE(AdminType.TYPE_NONE), SUPER(AdminType.TYPE_SUPER), SITE(AdminType.TYPE_SITE), TASK(AdminType.TYPE_TASK);

    public static final String TYPE_NONE = "n";
    public static final String TYPE_SUPER = "a";
    public static final String TYPE_SITE = "s";
    public static final String TYPE_TASK = "t";

    private final String type;

    private AdminType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
