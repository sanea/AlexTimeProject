package ru.alex.webapp.util;

import java.util.ResourceBundle;

/**
 * @author Alexander.Isaenco
 */
public class TimeUtils {
    public static String formatTimeSec(int timeSec, ResourceBundle resourceBundle) {
        int hours = timeSec / 3600;
        timeSec = timeSec - hours * 3600;
        int minutes = timeSec / 60;
        int seconds = timeSec - minutes * 60;
        String result = (hours != 0 ? String.valueOf(hours) + " " + resourceBundle.getString("time.hours") + " " : "")
                + (minutes != 0 ? String.valueOf(minutes) + " " + resourceBundle.getString("time.minutes") + " " : "")
                + (seconds != 0 ? String.valueOf(seconds) + " " + resourceBundle.getString("time.seconds") + " " : "");
        return result.equals("") ? "0 " + resourceBundle.getString("time.seconds") : result;
    }
}
