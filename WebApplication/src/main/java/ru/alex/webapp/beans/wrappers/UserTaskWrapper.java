package ru.alex.webapp.beans.wrappers;

import org.apache.log4j.Logger;
import ru.alex.webapp.model.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alex
 */
public class UserTaskWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UserTaskWrapper.class);
    private UserTask userTask;
    private UserTaskTime currentTime;
    private int timeLeftSec;

    public UserTaskWrapper(UserTask userTask, UserTaskTime currentTime, int timeSpentSeq) {
        logger.debug("init UserTaskWrapper " + userTask + " " + currentTime);
        this.userTask = userTask;
        this.currentTime = currentTime;
        if (currentTime != null) {
            this.timeLeftSec = currentTime.getDurationSec() - timeSpentSeq;
            logger.debug("init UserTaskWrapper timeLeftSec=" + this.timeLeftSec);
        }
    }

    public String getUsername() {
        return userTask.getUserByUsername().getUsername();
    }

    public String getTaskName() {
        return userTask.getTaskByTaskId().getName();
    }

    public String getTaskTypeFormatted() {
        return TaskType.getTypeFormatted(userTask.getTaskByTaskId().getType());
    }

    public String getTaskType() {
        return userTask.getTaskByTaskId().getType();
    }

    public BigDecimal getTaskPrice() {
        return userTask.getTaskByTaskId().getPrice();
    }

    public int getTimeLeftSec() {
        return timeLeftSec;
    }

    public void setTimeLeftSec(int timeLeftSec) {
        this.timeLeftSec = timeLeftSec;
    }

    public String getCurrentStatusFormatted() {
        return TaskStatus.getStatusFormatted(userTask.getStatus());
    }

    public String getCurrentStatus() {
        return userTask.getStatus();
    }

    public Long getTaskId() {
        return userTask.getTaskByTaskId().getId();
    }

    public Date getFinishTime() {
        return currentTime != null ? currentTime.getFinishTime() : null;
    }

    public Integer getDuration() {
        return currentTime != null ? Integer.valueOf(currentTime.getDurationSec()) : null;
    }

    public String getTimeLeftFormatted() {
        return formattTimeSec(timeLeftSec);
    }

    private String formattTimeSec(int timeSec) {
        int hours = timeSec / 3600;
        timeSec = timeSec - hours * 3600;
        int minutes = timeSec / 60;
        int seconds = timeSec - minutes * 60;
        return (hours != 0 ? String.valueOf(hours) + " hours ": "")
                + (minutes != 0 ? String.valueOf(minutes) + " minutes " : "")
                + (seconds != 0 ? String.valueOf(seconds) + " seconds" : "0 seconds");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTaskWrapper");
        sb.append("{userTask=").append(userTask);
        sb.append(", currentTime=").append(currentTime);
        sb.append(", timeLeftSec=").append(timeLeftSec);
        sb.append('}');
        return sb.toString();
    }
}
