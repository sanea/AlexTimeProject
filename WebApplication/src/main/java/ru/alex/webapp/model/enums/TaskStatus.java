package ru.alex.webapp.model.enums;

import ru.alex.webapp.util.CustomActionConfiguration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Alexander.Isaenco
 */
public enum TaskStatus {
    RUNNING(TaskStatus.STATUS_RUNNING), COMPLETED(TaskStatus.STATUS_COMPLETED), STOPPED(TaskStatus.STATUS_STOPPED),
    UNKNOWN(TaskStatus.STATUS_UNKNOWN), CUSTOM1(TaskStatus.STATUS_CUSTOM1), CUSTOM2(TaskStatus.STATUS_CUSTOM2), CUSTOM3(TaskStatus.STATUS_CUSTOM3);

    private static final char STATUS_RUNNING = 'r';
    private static final char STATUS_COMPLETED = 'c';
    private static final char STATUS_STOPPED = 's';
    private static final char STATUS_UNKNOWN = 'u';
    private static final char STATUS_CUSTOM1 = '1';
    private static final char STATUS_CUSTOM2 = '2';
    private static final char STATUS_CUSTOM3 = '3';

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
            case TaskStatus.STATUS_RUNNING:
                return RUNNING;
            case TaskStatus.STATUS_COMPLETED:
                return COMPLETED;
            case TaskStatus.STATUS_STOPPED:
                return STOPPED;
            case TaskStatus.STATUS_UNKNOWN:
                return UNKNOWN;
            case TaskStatus.STATUS_CUSTOM1:
                return CUSTOM1;
            case TaskStatus.STATUS_CUSTOM2:
                return CUSTOM2;
            case TaskStatus.STATUS_CUSTOM3:
                return CUSTOM3;
            default:
                throw new IllegalArgumentException("wrong status " + status);
        }
    }

    public static String getStatusFormatted(String status, Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
        switch (getStatus(status)) {
            case RUNNING:
                return resourceBundle.getString("status.running");
            case COMPLETED:
                return resourceBundle.getString("status.completed");
            case STOPPED:
                return resourceBundle.getString("status.stopped");
            case UNKNOWN:
                return resourceBundle.getString("status.unknown");
            case CUSTOM1:
                return CustomActionConfiguration.getCustomActionFormatted(CustomActionEnum.CUSTOM_1, locale);
            case CUSTOM2:
                return CustomActionConfiguration.getCustomActionFormatted(CustomActionEnum.CUSTOM_2, locale);
            case CUSTOM3:
                return CustomActionConfiguration.getCustomActionFormatted(CustomActionEnum.CUSTOM_3, locale);
            default:
                throw new IllegalArgumentException("wrong status " + status);
        }
    }

    public String getStatusFormatted(Locale locale) {
        return getStatusFormatted(String.valueOf(status), locale);
    }

    public String getStatusStr() {
        return String.valueOf(status);
    }

    public char getStatus() {
        return status;
    }

}