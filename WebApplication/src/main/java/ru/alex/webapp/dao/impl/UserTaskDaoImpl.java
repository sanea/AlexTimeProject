package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskDao;
import ru.alex.webapp.model.UserTask;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskDaoImpl extends GenericDaoImpl<UserTask, Long> implements UserTaskDao {
    @Override
    public List<UserTask> getTasksForUser(String username) {
        return getEntityManager().createQuery("select ut from UserTask ut where ut.userByUsername.username = :username", UserTask.class).setParameter("username", username).getResultList();
    }

    @Override
    public UserTask getTaskForUser(String username, Long taskId) {
        return getEntityManager().createQuery("select ut from UserTask ut where ut.userByUsername.username = :username and ut.taskByTaskId.id = :taskId", UserTask.class).setParameter("username", username).setParameter("taskId", taskId).getSingleResult();
    }
}
