package ru.alex.webapp.dao;

import ru.alex.webapp.model.UserTask;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserTaskDao extends GenericDao<UserTask, Long> {
    List<UserTask> getTasksForUser(String username);
    List<UserTask> getUsersForTask(Long taskId);
    UserTask getTaskForUser(String username, Long taskId);
    List<UserTask> getTaskForUserAll(String username, Long taskId);
    List<UserTask> getRunningTasks();
}
