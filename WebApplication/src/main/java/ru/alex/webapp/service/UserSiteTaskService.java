package ru.alex.webapp.service;

import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.enums.Action;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserSiteTaskService extends GenericService<UserSiteTask, Long> {
    List<UserSiteTask> getAllCurrentTime() throws Exception;

    List<UserSiteTask> getAllCurrentTime(Site site) throws Exception;

    List<UserSiteTask> getNotDeletedUserSiteTasks(Site site, Task task) throws Exception;

    List<UserSiteTask> getNotDeletedUserSiteTasks(Site site, User user) throws Exception;

    UserSiteTask getByUserSiteTask(User user, Site site, Task task) throws Exception;

    void addUserSiteTask(User user, Site site, Task task) throws Exception;

    void removeUserSiteTask(User user, Site site, Task task) throws Exception;

    //void checkAllTasks();

    void startTask(UserSiteTask userSiteTask, BigDecimal customPrice) throws Exception;

    void startProcess(UserSiteTask userSiteTask, int seconds, Action action) throws Exception;

    void switchProcess(UserSiteTask userSiteTask, int seconds, Action action) throws Exception;

    void resumeProcess(UserSiteTask userSiteTask) throws Exception;

    void switchCustom(UserSiteTask userSiteTask, int seconds, Action action) throws Exception;

    void extendProcess(UserSiteTask userSiteTask, int seconds) throws Exception;

    void stopProcess(UserSiteTask userSiteTask) throws Exception;

    boolean checkTask(UserSiteTask task) throws Exception;

}
