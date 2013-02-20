package ru.alex.webapp.beans.wrappers;

import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTask;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alex
 */
public class TaskWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private UserTask userTask;
    private boolean isActive;
    private int timeLeft;
    private Date finishTime;

    public TaskWrapper(UserTask userTask, Date finishTime) {
        this.userTask = userTask;
        if (finishTime != null) {
            Date now = new Date();
            this.timeLeft = (int) ((finishTime.getTime() - now.getTime()) / 1000);
            this.finishTime = finishTime;
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
        return finishTime;
    }

}
