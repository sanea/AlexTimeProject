package ru.alex.webapp.model.enums;

import ru.alex.webapp.util.CustomActionConfiguration;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Alexander.Isaenco
 */
public enum Action {
    START('r'), RESUME('c'), EXTEND('e'), FINISH('f'), STOP('s'), CUSTOM1('1'), CUSTOM2('2'), CUSTOM3('3');  //Custom like pause
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
            case 'r':
                return START;
            case 'e':
                return EXTEND;
            case 'f':
                return FINISH;
            case 's':
                return STOP;
            case '1':
                return CUSTOM1;
            case '2':
                return CUSTOM2;
            case '3':
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
