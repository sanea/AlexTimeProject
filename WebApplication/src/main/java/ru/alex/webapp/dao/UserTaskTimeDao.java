package ru.alex.webapp.dao;

import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.enums.TaskType;

import java.util.Date;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserTaskTimeDao extends GenericDao<UserTaskTime, Long> {
    public List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date dateFrom, Date dateTo, boolean showDeleted, int start, int end);

    public Long countAll(Site site, User user, Task task, TaskType taskType, Date dateFrom, Date dateTo, boolean withDeleted);
}
