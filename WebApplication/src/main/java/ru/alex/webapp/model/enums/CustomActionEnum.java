package ru.alex.webapp.model.enums;

/**
 * @author Alexander.Isaenco
 */
public enum CustomActionEnum {
    CUSTOM_1(CustomActionEnum.CUSTOM_ACTION_1), CUSTOM_2(CustomActionEnum.CUSTOM_ACTION_2), CUSTOM_3(CustomActionEnum.CUSTOM_ACTION_3);

    private static final int CUSTOM_ACTION_1 = 1;
    private static final int CUSTOM_ACTION_2 = 2;
    private static final int CUSTOM_ACTION_3 = 3;

    private final int id;

    private CustomActionEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CustomActionEnum getCustomAction(int id) {
        switch (id) {
            case CustomActionEnum.CUSTOM_ACTION_1:
                return CUSTOM_1;
            case CustomActionEnum.CUSTOM_ACTION_2:
                return CUSTOM_2;
            case CustomActionEnum.CUSTOM_ACTION_3:
                return CUSTOM_3;
            default:
                throw new IllegalArgumentException("wrong CustomAction id " + id);
        }
    }
}
