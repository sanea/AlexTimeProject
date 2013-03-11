package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskDao;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.enums.TaskStatus;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskDaoImpl extends GenericDaoImpl<UserSiteTask, Long> implements UserTaskDao {
    @Override
    public List<UserSiteTask> getTasksForUser(String username) {
        return getEntityManager().createQuery("select ut from UserSiteTask ut where ut.enabled = true and ut.userByUsername.username = :username and ut.taskByTaskId.enabled = true", UserSiteTask.class).setParameter("username", username).getResultList();
    }

    @Override
    public List<UserSiteTask> getTasksAllForUser(String username) {
        return getEntityManager().createQuery("select ut from UserSiteTask ut where ut.userByUsername.username = :username", UserSiteTask.class).setParameter("username", username).getResultList();
    }

    @Override
    public List<UserSiteTask> getUsersForTask(Long taskId) {
        return getEntityManager().createQuery("select ut from UserSiteTask ut where ut.enabled = true and ut.taskByTaskId.id = :taskId and ut.userByUsername.enabled = true", UserSiteTask.class).setParameter("taskId", taskId).getResultList();
    }

    @Override
    public UserSiteTask getTaskForUser(String username, Long taskId) {
        return getEntityManager().createQuery("select ut from UserSiteTask ut where ut.enabled = true and ut.userByUsername.username = :username and ut.taskByTaskId.id = :taskId and ut.taskByTaskId.enabled = true", UserSiteTask.class).setParameter("username", username).setParameter("taskId", taskId).getSingleResult();
    }

    @Override
    public List<UserSiteTask> getTaskForUserAll(String username, Long taskId) {
        return getEntityManager().createQuery("select ut from UserSiteTask ut where ut.userByUsername.username = :username and ut.taskByTaskId.id = :taskId", UserSiteTask.class).setParameter("username", username).setParameter("taskId", taskId).getResultList();
    }

    @Override
    public List<UserSiteTask> getRunningTasks() {
        return getEntityManager().createQuery("select ut from UserSiteTask ut where ut.enabled = true and ut.status = :taskStatus", UserSiteTask.class).setParameter("taskStatus", TaskStatus.RUNNING.getStatusStr()).getResultList();
    }

}
