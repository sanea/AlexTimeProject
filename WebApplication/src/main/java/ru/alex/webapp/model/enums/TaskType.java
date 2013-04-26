package ru.alex.webapp.model.enums;

import java.util.ResourceBundle;

/**
 * @author Alexander.Isaenco
 */
public enum TaskType {
    PROCESS('p'), TASK('t'), TASK_CUSTOM_PRICE('c');
    private final char type;

    private TaskType(char type) {
        this.type = type;
    }

    private static char getChar(String str) {
        if (str == null || str.length() != 1)
            throw new IllegalArgumentException("wrong str " + str);
        return str.charAt(0);
    }

    public static TaskType getType(String type) {
        switch (getChar(type)) {
            case 'p':
                return PROCESS;
            case 't':
                return TASK;
            case 'c':
                return TASK_CUSTOM_PRICE;
            default:
                throw new IllegalArgumentException("wrong task type " + type);
        }
    }

    public static String getTypeFormatted(String type, ResourceBundle resourceBundle) {
        switch (getType(type)) {
            case PROCESS:
                return resourceBundle.getString("type.process");
            case TASK:
                return resourceBundle.getString("type.task");
            case TASK_CUSTOM_PRICE:
                return resourceBundle.getString("type.task.custom");
            default:
                throw new IllegalArgumentException("wrong task type " + type);
        }
    }

    public String getTypeFormatted(ResourceBundle resourceBundle) {
        return getTypeFormatted(String.valueOf(type), resourceBundle);
    }

    public String getTypeStr() {
        return String.valueOf(type);
    }

    public char getType() {
        return type;
    }
}