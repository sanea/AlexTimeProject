package ru.alex.webapp.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.enums.TaskType;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskTimeDaoImpl extends GenericDaoImpl<UserTaskTime, Long> implements UserTaskTimeDao {
    private static final Logger logger = LoggerFactory.getLogger(UserTaskTimeDaoImpl.class);

    @Override
    public List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date dateFrom, Date dateTo) {
        StringBuilder queryBuilder = new StringBuilder("SELECT taskTime from UserTaskTime taskTime");
        Map<String, Object> paramMap = new HashMap<>();
        int paramLength = 0;
        if (site != null && site.getId() != null) {
            queryBuilder.append(paramLength++ == 0 ? " where " : " and ");
            queryBuilder.append("taskTime.userSiteTaskById.siteTask.siteBySiteId.id = :siteId");
            paramMap.put("siteId", site.getId());
        }
        if (user != null && user.getUsername() != null && !user.getUsername().isEmpty()) {
            queryBuilder.append(paramLength++ == 0 ? " where " : " and ");
            queryBuilder.append("taskTime.userSiteTaskById.userByUsername.username = :username");
            paramMap.put("username", user.getUsername());
        }
        if (task != null && task.getId() != null) {
            queryBuilder.append(paramLength++ == 0 ? " where " : " and ");
            queryBuilder.append("taskTime.userSiteTaskById.siteTask.taskByTaskId.id = :taskId");
            paramMap.put("taskId", task.getId());
        }
        if (taskType != null) {
            queryBuilder.append(paramLength++ == 0 ? " where " : " and ");
            queryBuilder.append("taskTime.userSiteTaskById.siteTask.taskByTaskId.type = :taskType");
            paramMap.put("taskType", taskType.getTypeStr());
        }
        if (dateFrom != null) {
            queryBuilder.append(paramLength++ == 0 ? " where " : " and ");
            queryBuilder.append("taskTime.startTime >= :dateFrom");
            paramMap.put("dateFrom", dateFrom);
        }
        if (dateTo != null) {
            queryBuilder.append(paramLength++ == 0 ? " where " : " and ");
            queryBuilder.append("taskTime.startTime <= :dateTo");
            paramMap.put("dateTo", dateTo);
        }
        logger.debug("getAll queryBuilder={}", queryBuilder);
        logger.debug("getAll paramMap={}", paramMap);

        TypedQuery<UserTaskTime> query = getEntityManager().createQuery(queryBuilder.toString(), UserTaskTime.class);
        for (String param : paramMap.keySet()) {
            query.setParameter(param, paramMap.get(param));
        }
        return query.getResultList();
    }
}
