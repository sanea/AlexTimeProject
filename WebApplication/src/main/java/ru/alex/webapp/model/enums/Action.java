package ru.alex.webapp.model.enums;

import ru.alex.webapp.util.CustomActionConfiguration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Alexander.Isaenco
 */
public enum Action {
    START(Action.ACTION_START), RESUME(Action.ACTION_RESUME), EXTEND(Action.ACTION_EXTEND), FINISH(Action.ACTION_FINISH),
    STOP(Action.ACTION_STOP), CUSTOM1(Action.ACTION_CUSTOM1), CUSTOM2(Action.ACTION_CUSTOM2), CUSTOM3(Action.ACTION_CUSTOM3);  //Custom like pause

    private static final char ACTION_START = 'r';
    private static final char ACTION_RESUME = 'c';
    private static final char ACTION_EXTEND = 'e';
    private static final char ACTION_FINISH = 'f';
    private static final char ACTION_STOP = 's';
    private static final char ACTION_CUSTOM1 = '1';
    private static final char ACTION_CUSTOM2 = '2';
    private static final char ACTION_CUSTOM3 = '3';

    private final char action;

    private Action(char action) {
        this.action = action;
    }

    private static char getChar(String str) {
        if (str == null || str.length() != 1)
            throw new IllegalArgumentException("wrong str " + str);
        return str.charAt(0);
    }

    public static Action getAction(String action) {
        switch (getChar(action)) {
            case Action.ACTION_START:
                return START;
            case Action.ACTION_EXTEND:
                return EXTEND;
            case Action.ACTION_FINISH:
                return FINISH;
            case Action.ACTION_STOP:
                return STOP;
            case Action.ACTION_CUSTOM1:
                return CUSTOM1;
            case Action.ACTION_CUSTOM2:
                return CUSTOM2;
            case Action.ACTION_CUSTOM3:
                return CUSTOM3;
            default:
                throw new IllegalArgumentException("wrong action " + action);
        }
    }

    public static String getActionFormatted(String action, Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
        switch (getAction(action)) {
            case START:
                return resourceBundle.getString("action.start");
            case EXTEND:
                return resourceBundle.getString("action.extend");
            case FINISH:
                return resourceBundle.getString("action.finish");
            case STOP:
                return resourceBundle.getString("action.stop");
            case CUSTOM1:
                return CustomActionConfiguration.getCustomActionFormatted(CustomActionEnum.CUSTOM_1, locale);
            case CUSTOM2:
                return CustomActionConfiguration.getCustomActionFormatted(CustomActionEnum.CUSTOM_2, locale);
            case CUSTOM3:
                return CustomActionConfiguration.getCustomActionFormatted(CustomActionEnum.CUSTOM_3, locale);
            default:
                throw new IllegalArgumentException("wrong action " + action);
        }
    }

    public String getActionFormatted(Locale locale) {
        return getActionFormatted(String.valueOf(action), locale);
    }

    public String getActionStr() {
        return String.valueOf(action);
    }

    public char getAction() {
        return action;
    }

}
