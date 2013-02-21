package ru.alex.webapp.model;

/**
 * @author Alexander.Isaenco
 */
public enum TaskType {
    PROCESS('p'), TASK('t');
    private char type;

    private TaskType(char type) {
        this.type = type;
    }

    private static char getChar(String str) {
        if (str == null || str.length() != 1)
            throw new IllegalArgumentException("wrong str");
        return str.charAt(0);
    }

    public static TaskType getType(String type) {
        switch (getChar(type)) {
            case 'p':
                return PROCESS;
            case 't':
                return TASK;
            default:
                throw new IllegalArgumentException("wrong task type");
        }
    }

    public static String getTypeFormatted(String type) {
        switch (getChar(type)) {
            case 'p':
                return "Process";
            case 't':
                return "Task";
            default:
                throw new IllegalArgumentException("wrong task type");
        }
    }

    public String getTypeFormatted() {
        return getTypeFormatted(String.valueOf(type));
    }

    public String getTypeStr() {
        return String.valueOf(type);
    }

    public char getType() {
        return type;
    }
}