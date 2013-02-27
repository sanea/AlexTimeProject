package ru.alex.webapp.util;

/**
 * @author Alexander.Isaenco
 */
public class TimeUtils {
    public static String formatTimeSec(int timeSec) {
        int hours = timeSec / 3600;
        timeSec = timeSec - hours * 3600;
        int minutes = timeSec / 60;
        int seconds = timeSec - minutes * 60;
        return (hours != 0 ? String.valueOf(hours) + " hours " : "")
                + (minutes != 0 ? String.valueOf(minutes) + " minutes " : "")
                + (seconds != 0 ? String.valueOf(seconds) + " seconds" : "0 seconds");
    }
}
