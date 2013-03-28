package ru.alex.webapp.service;

import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserSiteTask;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserSiteTaskService extends GenericService<UserSiteTask, Long> {
    List<UserSiteTask> getAllCurrentTime() throws Exception;

    List<UserSiteTask> getNotDeletedUserSiteTasks(Site site, Task task) throws Exception;

    UserSiteTask getByUserSiteTask(User user, Site site, Task task) throws Exception;

    void addUserSiteTask(User user, Site site, Task task) throws Exception;

    void removeUserSiteTask(User user, Site site, Task task) throws Exception;

}
