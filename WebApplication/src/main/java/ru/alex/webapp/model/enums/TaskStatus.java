package ru.alex.webapp.model.enums;

/**
 * @author Alexander.Isaenco
 */
public enum TaskStatus {
    RUNNING('r'), COMPLETED('c'), PAUSED('p'), STOPPED('s'), UNKNOWN('u');
    private char status;

    private TaskStatus(char status) {
        this.status = status;
    }

    private static char getChar(String str) {
        if (str == null || str.length() != 1)
            throw new IllegalArgumentException("wrong str");
        return str.charAt(0);
    }

    public static TaskStatus getStatus(String status) {
        switch (getChar(status)) {
            case 'r':
                return RUNNING;
            case 'c':
                return COMPLETED;
            case 'p':
                return PAUSED;
            case 's':
                return STOPPED;
            case 'u':
                return UNKNOWN;
            default:
                throw new IllegalArgumentException("wrong status");
        }
    }

    public static String getStatusFormatted(String status) {
        switch (getChar(status)) {
            case 'r':
                return "Running";
            case 'c':
                return "Completed";
            case 'p':
                return "Paused";
            case 's':
                return "Stopped";
            case 'u':
                return "Unknown";
            default:
                throw new IllegalArgumentException("wrong status");
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