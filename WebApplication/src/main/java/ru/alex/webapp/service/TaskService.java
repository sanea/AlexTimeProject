package ru.alex.webapp.service;

import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskTime;

import java.io.Serializable;
import java.util.List;

public interface TaskService extends Serializable {
    List<Task> getAllTasks() throws Exception;

    List<UserTask> getTasksForUser(String username) throws Exception;

    UserTaskTime getCurrentTimeForUser(Long taskId, String username) throws Exception;

    int getTimeSpentForUserTask(Long taskId, String username) throws Exception;

    void startTask(Long taskId, String username, int seconds) throws Exception;

    void pauseTask(Long taskId, String username) throws Exception;

    void resumeTask(Long taskId, String username) throws Exception;

    void extendTask(Long taskId, String username, int seconds) throws Exception;

    void stopTask(Long taskId, String username) throws Exception;
}
