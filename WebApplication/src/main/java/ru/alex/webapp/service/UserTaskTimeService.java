package ru.alex.webapp.service;

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
public interface UserTaskTimeService extends GenericService<UserTaskTime, Long> {

    void restore(UserTaskTime entity) throws Exception;

    List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to) throws Exception;

    List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to, boolean showDeleted, int start, int end) throws Exception;

    Long getAllCount(Site site, User user, Task task, TaskType taskType, Date from, Date to, boolean withDeleted) throws Exception;
}
