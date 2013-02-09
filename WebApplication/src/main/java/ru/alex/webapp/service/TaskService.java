package ru.alex.webapp.service;

import ru.alex.webapp.model.UserTaskEntity;
import ru.alex.webapp.model.UserTaskStatusEntity;

import java.util.List;

public interface TaskService {
    List<UserTaskEntity> getTasksForUser(String username);

    void startTask(Long taskId, String username) throws Exception;

    void endTask(Long taskId, String username) throws Exception;

    List<UserTaskStatusEntity> getAllTaskStatus();
}
