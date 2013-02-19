package ru.alex.webapp.dao;

import ru.alex.webapp.model.UserTaskTime;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserTaskTimeDao extends GenericDao<UserTaskTime, Long> {
    List<UserTaskTime> getCurrentTime(Long taskId, String username);
}
