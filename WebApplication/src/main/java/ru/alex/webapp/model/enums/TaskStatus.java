package ru.alex.webapp.model.enums;

import ru.alex.webapp.util.CustomActionConfiguration;

/**
 * @author Alexander.Isaenco
 */
public enum TaskStatus {
    RUNNING('r'), COMPLETED('c'), STOPPED('s'), UNKNOWN('u'), CUSTOM1('1'), CUSTOM2('2'), CUSTOM3('3');
    private final char status;

    private TaskStatus(char status) {
        this.status = status;
    }

    private static char getChar(String str) {
        if (str == null || str.length() != 1)
            throw new IllegalArgumentException("wrong str " + str);
        return str.charAt(0);
    }

    public static TaskStatus getStatus(String status) {
        switch (getChar(status)) {
            case 'r':
                return RUNNING;
            case 'c':
                return COMPLETED;
            case 's':
                return STOPPED;
            case 'u':
                return UNKNOWN;
            case '1':
                return CUSTOM1;
            case '2':
                return CUSTOM2;
            case '3':
                return CUSTOM3;
            default:
                throw new IllegalArgumentException("wrong status " + status);
        }
    }

    public static String getStatusFormatted(String status) {
        switch (getStatus(status)) {
            case RUNNING:
                return "Running";
            case COMPLETED:
                return "Completed";
            case STOPPED:
                return "Stopped";
            case UNKNOWN:
                return "Unknown";
            case CUSTOM1:
                return CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_1).getName();
            case CUSTOM2:
                return CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_2).getName();
            case CUSTOM3:
                return CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_3).getName();
            default:
                throw new IllegalArgumentException("wrong status " + status);
        }
    }

    public String getStatusFormatted() {
        return getStatusFormatted(String.valueOf(status));
    }

    public String getStatusStr() {
        return String.valueOf(status);
    }

    public char getStatus() {
        return status;
    }

}