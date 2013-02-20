package ru.alex.webapp.beans.wrappers;

import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alex
 */
public class TaskWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private UserTask userTask;
    private UserTaskTime currentTime;
    private boolean isActive;
    private int timeLeft;

    public TaskWrapper(UserTask userTask, UserTaskTime currentTime) {
        this.userTask = userTask;
        this.currentTime = currentTime;
        if (currentTime != null && currentTime.getFinishTime() != null) {
            Date now = new Date();
            this.timeLeft = (int) ((currentTime.getFinishTime().getTime() - now.getTime()) / 1000);
        }
    }

    public String getTaskName() {
        return userTask.getTaskByTaskId().getTaskName();
    }

    public String getTaskTypeStr() {
        return Task.TaskType.getTypeStr(userTask.getTaskByTaskId().getTaskType());
    }

    public int getTaskType() {
        return userTask.getTaskByTaskId().getTaskType();
    }

    public BigDecimal getTaskPrice() {
        return userTask.getTaskByTaskId().getTaskPrice();
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public String getCurrentStatusFormatted() {
        return UserTask.TaskStatus.getStatusFormated(userTask.getStatus().charAt(0));
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

}
