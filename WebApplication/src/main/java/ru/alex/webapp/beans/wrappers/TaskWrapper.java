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

    public TaskWrapper(Task task) {
        this.task = task;
    }

    public String getTaskName() {
        return task.getTaskName();
    }

    public String getTaskTypeStr() {
        String taskType = "";
        switch (Task.TaskType.getType(task.getTaskType())) {
            case Task:
                taskType = "Task";
                break;
            case Process:
                taskType = "Process";
                break;
            default:
                break;
        }
        return taskType;
    }

    public Task.TaskType getTaskType() {
        return Task.TaskType.getType(task.getTaskType());
    }

    public BigDecimal getTaskPrice() {
        return task.getTaskPrice();
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
