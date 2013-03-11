package ru.alex.webapp.dao;

import ru.alex.webapp.model.UserSiteTask;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserTaskDao extends GenericDao<UserSiteTask, Long> {
    List<UserSiteTask> getTasksForUser(String username);

    List<UserSiteTask> getTasksAllForUser(String username);

    List<UserSiteTask> getUsersForTask(Long taskId);

    UserSiteTask getTaskForUser(String username, Long taskId);

    List<UserSiteTask> getTaskForUserAll(String username, Long taskId);

    List<UserSiteTask> getRunningTasks();
}
