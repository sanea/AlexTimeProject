package ru.alex.webapp.beans.wrappers;

import org.apache.log4j.Logger;
import ru.alex.webapp.model.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alex
 */
public class TaskWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TaskWrapper.class);
    private UserTask userTask;
    private UserTaskTime currentTime;
    private int timeLeft;

    public TaskWrapper(UserTask userTask, UserTaskTime currentTime, int timeSpent) {
        logger.debug("init TaskWrapper " + userTask + " " + currentTime);
        this.userTask = userTask;
        this.currentTime = currentTime;
        if (currentTime != null) {
            this.timeLeft = currentTime.getDurationSec() - timeSpent;
            logger.debug("init TaskWrapper timeLeft=" + this.timeLeft);
        }
    }

    public String getTaskName() {
        return userTask.getTaskByTaskId().getTaskName();
    }

    public String getTaskTypeStr() {
        return TaskType.getTypeFormatted(userTask.getTaskByTaskId().getTaskType());
    }

    public String getTaskType() {
        return userTask.getTaskByTaskId().getTaskType();
    }

    public BigDecimal getTaskPrice() {
        return userTask.getTaskByTaskId().getTaskPrice();
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TaskWrapper");
        sb.append("{userTask=").append(userTask);
        sb.append(", currentTime=").append(currentTime);
        sb.append(", timeLeft=").append(timeLeft);
        sb.append('}');
        return sb.toString();
    }
}
