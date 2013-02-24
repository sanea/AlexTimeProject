package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskDao;
import ru.alex.webapp.model.TaskStatus;
import ru.alex.webapp.model.UserTask;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskDaoImpl extends GenericDaoImpl<UserTask, Long> implements UserTaskDao {
    @Override
    public List<UserTask> getTasksForUser(String username) {
        return getEntityManager().createQuery("select ut from UserTask ut where ut.enabled = true and ut.userByUsername.username = :username and ut.taskByTaskId.enabled = true", UserTask.class).setParameter("username", username).getResultList();
    }

    @Override
    public List<UserTask> getUsersForTask(Long taskId) {
        return getEntityManager().createQuery("select ut from UserTask ut where ut.enabled = true and ut.taskByTaskId.id = :taskId and ut.userByUsername.enabled = true", UserTask.class).setParameter("taskId", taskId).getResultList();
    }

    @Override
    public UserTask getTaskForUser(String username, Long taskId) {
        return getEntityManager().createQuery("select ut from UserTask ut where ut.enabled = true and ut.userByUsername.username = :username and ut.taskByTaskId.id = :taskId and ut.taskByTaskId.enabled = true", UserTask.class).setParameter("username", username).setParameter("taskId", taskId).getSingleResult();
    }

    @Override
    public List<UserTask> getTaskForUserAll(String username, Long taskId) {
        return getEntityManager().createQuery("select ut from UserTask ut where ut.userByUsername.username = :username and ut.taskByTaskId.id = :taskId", UserTask.class).setParameter("username", username).setParameter("taskId", taskId).getResultList();
    }

    @Override
    public List<UserTask> getRunningTasks() {
        return getEntityManager().createQuery("select ut from UserTask ut where ut.enabled = true and ut.status = :taskStatus and ut.userByUsername.enabled = true", UserTask.class).setParameter("taskStatus", TaskStatus.RUNNING.getStatusStr()).getResultList();
    }
}
