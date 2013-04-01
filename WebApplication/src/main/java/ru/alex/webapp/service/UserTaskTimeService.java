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
    List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to);
}
