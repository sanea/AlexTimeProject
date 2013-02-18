package ru.alex.webapp.beans.wrappers;

import ru.alex.webapp.model.Task;

import java.math.BigDecimal;

/**
 * @author Alex
 */
public class TaskWrapper {
    private Task task;
    private boolean isActive;
    private int timeLeft;
    private char currentStatus;

    public TaskWrapper(Task task) {
        this.task = task;
    }

    public String getTaskName() {
        return task.getTaskName();
    }

    public String getTaskTypeStr() {
        String taskType = "";
        switch (Task.TaskType.getType(task.getTaskType())) {
            case TASK:
                taskType = "Task";
                break;
            case PROCESS:
                taskType = "Process";
                break;
            default:
                break;
        }
        return taskType;
    }

    public int getTaskType() {
        return task.getTaskType();
    }

    public BigDecimal getTaskPrice() {
        return task.getTaskPrice();
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public char getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(char currentStatus) {
        this.currentStatus = currentStatus;
    }
}
