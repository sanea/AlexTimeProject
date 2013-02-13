package ru.alex.webapp.service;

import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskStatus;

import java.util.List;

public interface TaskService {
    List<UserTask> getTasksForUser(String username);

    void startTask(Long taskId, String username, int seconds) throws Exception;

    void pauseTask(Long taskId, String username) throws Exception;

    void extendTask(Long taskId, String username, int seconds) throws Exception;

    List<UserTaskStatus> getAllTaskStatus();
}
