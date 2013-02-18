package ru.alex.webapp.service;

import ru.alex.webapp.beans.wrappers.TaskWrapper;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskTime;

import java.util.List;

public interface TaskService {
    List<UserTask> getTasksForUser(String username);

    void startTask(Long taskId, String username, int seconds) throws Exception;

    void pauseTask(Long taskId, String username) throws Exception;

    void resumeTask(Long taskId, String username) throws Exception;

    void extendTask(Long taskId, String username, int seconds) throws Exception;

    void stopTask(Long taskId, String username) throws Exception;
}
