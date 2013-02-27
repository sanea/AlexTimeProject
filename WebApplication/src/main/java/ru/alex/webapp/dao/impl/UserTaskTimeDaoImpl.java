package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.UserTaskTime;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskTimeDaoImpl extends GenericDaoImpl<UserTaskTime, Long> implements UserTaskTimeDao {
    @Override
    public List<UserTaskTime> getCurrentTime(Long taskId, String username) {
        return getEntityManager().createQuery("select t from UserTaskTime t where t.current = true and t.userTaskById.userByUsername.username = :username and t.userTaskById.taskByTaskId.id = :taskId", UserTaskTime.class)
                .setParameter("username", username).setParameter("taskId", taskId).getResultList();
    }

    @Override
    public List<UserTaskTime> getAllNotCurrentTime() {
        return getEntityManager().createQuery("select  t from UserTaskTime t where t.current != true", UserTaskTime.class).getResultList();
    }
}
