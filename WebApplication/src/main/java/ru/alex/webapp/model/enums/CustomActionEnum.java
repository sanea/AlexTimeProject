package ru.alex.webapp.model.enums;

/**
 * @author Alexander.Isaenco
 */
public enum CustomActionEnum {
    CUSTOM1(1), CUSTOM2(2), CUSTOM3(3);
    private final int id;

    private CustomActionEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
