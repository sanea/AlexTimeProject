package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.TaskDao;
import ru.alex.webapp.model.Task;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class TaskDaoImpl extends GenericDaoImpl<Task, Long> implements TaskDao {
    @Override
    public List<Task> getTasksForUser(String username) {
        return getEntityManager().createQuery("select t from Task t join t.userTasksById ut where ut.userByUsername.username=:username order by t.type").setParameter("username", username).getResultList();
    }
}
