package ru.alex.webapp.model.enums;

/**
 * @author Alexander.Isaenco
 */
public enum AdminValueType {
    SUPER_RATE(AdminValueType.TYPE_SUPER_RATE),
    SITE_RATE_INCOME(AdminValueType.TYPE_SITE_RATE_INCOME), SITE_RATE_PROFIT(AdminValueType.TYPE_SITE_RATE_PROFIT),
    TASK_VAL(AdminValueType.TYPE_TASK_VAL), TASK_RATE(AdminValueType.TYPE_TASK_RATE);

    public static final String TYPE_SUPER_RATE = "s";
    public static final String TYPE_SITE_RATE_INCOME = "i";
    public static final String TYPE_SITE_RATE_PROFIT = "p";
    public static final String TYPE_TASK_VAL = "v";
    public static final String TYPE_TASK_RATE = "r";

    private final String type;

    private AdminValueType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
