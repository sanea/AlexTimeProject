package ru.alex.webapp.beans.wrappers;

import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTask;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alex
 */
public class TaskWrapper implements Serializable {
    private UserTask userTask;
    private boolean isActive;
    private int timeLeft;

    public TaskWrapper(UserTask userTask) {
        this.userTask = userTask;
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

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getCurrentStatusStr() {
        return UserTask.TaskStatus.getStatusFormated(userTask.getStatus().charAt(0));
    }

    public String getCurrentStatus() {
        return userTask.getStatus();
    }

    public Long getTaskId() {
        return userTask.getTaskByTaskId().getId();
    }

}
