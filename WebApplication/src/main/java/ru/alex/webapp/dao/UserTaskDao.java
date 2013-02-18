package ru.alex.webapp.dao;

import ru.alex.webapp.model.UserTask;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserTaskDao extends GenericDao<UserTask, Long> {
    List<UserTask> getTasksForUser(String username);
    UserTask getTaskForUser(String username, Long taskId);
}
