package ru.alex.webapp.model.enums;

/**
 * @author Alexander.Isaenco
 */
public enum CustomActionEnum {
    CUSTOM_1(1), CUSTOM_2(2), CUSTOM_3(3);
    private final int id;

    private CustomActionEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CustomActionEnum getCustomAction(int id) {
        switch (id) {
            case 1:
                return CUSTOM_1;
            case 2:
                return CUSTOM_2;
            case 3:
                return CUSTOM_3;
            default:
                throw new IllegalArgumentException("wrong CustomAction id " + id);
        }
    }
}
