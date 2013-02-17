package ru.alex.webapp.dao;

import ru.alex.webapp.model.Task;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface TaskDao extends GenericDao<Task, Long> {

    List<Task> getTasksForUser(String username);
}
