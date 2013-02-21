package ru.alex.webapp.model;

/**
 * @author Alexander.Isaenco
 */
public enum Action {
    START('r'), PAUSE('p'), RESUME('c'), EXTEND('e'), FINISH('f'), STOP('s');
    private char action;

    private Action(char action) {
        this.action = action;
    }

    private static char getChar(String str) {
        if (str == null || str.length() != 1)
            throw new IllegalArgumentException("wrong str");
        return str.charAt(0);
    }

    public static Action getAction(String action) {
        switch (getChar(action)) {
            case 'r':
                return START;
            case 'p':
                return PAUSE;
            case 'c':
                return RESUME;
            case 'e':
                return EXTEND;
            case 'f':
                return FINISH;
            case 's':
                return STOP;
            default:
                throw new IllegalArgumentException("wrong action");
        }
    }

    public static String getActionFormatted(String action) {
        switch (getChar(action)) {
            case 'r':
                return "Start";
            case 'p':
                return "Pause";
            case 'c':
                return "Resume";
            case 'e':
                return "Extend";
            case 'f':
                return "Finish";
            case 's':
                return "Stop";
            default:
                throw new IllegalArgumentException("wrong action");
        }
    }

    public String getActionStr() {
        return String.valueOf(action);
    }

    public char getAction() {
        return action;
    }

}
