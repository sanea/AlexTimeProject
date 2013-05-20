package ru.alex.webapp.service;

import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.TaskTime;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.enums.TaskType;

import java.util.Date;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface TaskTimeService extends GenericService<TaskTime, Long> {

    void restore(TaskTime entity) throws Exception;

    List<TaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to) throws Exception;

    List<TaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to, boolean showDeleted, int start, int end) throws Exception;

    Long getAllCount(Site site, User user, Task task, TaskType taskType, Date from, Date to, boolean withDeleted) throws Exception;
}
