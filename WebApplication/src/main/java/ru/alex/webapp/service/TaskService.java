package ru.alex.webapp.service;

import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskTime;

import java.io.Serializable;
import java.util.List;

public interface TaskService {
    List<Task> getAllTasks() throws Exception;

    List<UserTask> getTasksForUser(String username) throws Exception;

    List<UserTask> getOnlineTasks() throws Exception;

    List<UserTask> getUsersForTask(Long taskId) throws Exception;

    UserTaskTime getCurrentTimeForUserTask(Long taskId, String username) throws Exception;

    int getTimeSpentSecForUserTask(Long taskId, String username) throws Exception;

    void startTask(Long taskId, String username, int seconds) throws Exception;

    void pauseTask(Long taskId, String username) throws Exception;

    void resumeTask(Long taskId, String username) throws Exception;

    void extendTask(Long taskId, String username, int seconds) throws Exception;

    void stopTask(Long taskId, String username) throws Exception;

    void addTask(Task task) throws Exception;

    void removeTask(Long taskId) throws Exception;

    boolean isTaskEditable(Long taskId) throws Exception;

    void editTask(Task task) throws Exception;

    void updateUserTask(Long taskId, String username, boolean assigned) throws Exception;

    void checkAllTasks();
}
