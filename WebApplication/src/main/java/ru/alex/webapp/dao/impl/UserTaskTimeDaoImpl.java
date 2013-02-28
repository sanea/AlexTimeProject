package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.UserTaskTime;

import javax.persistence.TypedQuery;
import java.util.Date;
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

    @Override
    public List<UserTaskTime> getNotCurrentTime(Date from, Date to) {
        TypedQuery<UserTaskTime> query;
        if (from == null && to == null)
            return getAllNotCurrentTime();
        else if (from != null && to == null)
            query = getEntityManager().createQuery("select  t from UserTaskTime t where t.current != true and t.startTime >= :fromDate", UserTaskTime.class).setParameter("fromDate", from);
        else if (from == null && to != null)
            query = getEntityManager().createQuery("select  t from UserTaskTime t where t.current != true and t.finishTime <= :toDate", UserTaskTime.class).setParameter("toDate", to);
        else
            query = getEntityManager().createQuery("select  t from UserTaskTime t where t.current != true and t.startTime >= :fromDate and t.finishTime <= :toDate", UserTaskTime.class).setParameter("fromDate", from).setParameter("toDate", to);
        return query.getResultList();
    }
}
