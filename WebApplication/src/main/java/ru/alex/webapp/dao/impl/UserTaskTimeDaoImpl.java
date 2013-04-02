package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.enums.TaskType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.Date;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskTimeDaoImpl extends GenericDaoImpl<UserTaskTime, Long> implements UserTaskTimeDao {
    @Override
    public List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserTaskTime> cq = cb.createQuery(UserTaskTime.class);

        Metamodel m = getEntityManager().getMetamodel();
        EntityType<UserTaskTime> UserTaskTime_ = m.entity(UserTaskTime.class);
        Root<UserTaskTime> userTaskTime = cq.from(UserTaskTime_);

        cq.where(cb.equal(userTaskTime.get(UserTaskTime_.), "Fido"));

        criteria.select(entityRoot);
        criteria.where(builder.equal(entityRoot.get()))
        return getEntityManager().createQuery(criteria).getResultList();
    }
}
