package ru.alex.webapp.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.TaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.TaskTime;
import ru.alex.webapp.model.User;
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
public class TaskTimeDaoImpl extends GenericDaoImpl<TaskTime, Long> implements TaskTimeDao {
    private static final Logger logger = LoggerFactory.getLogger(TaskTimeDaoImpl.class);
    private static final String RESULT_QUERY = "query";
    private static final String RESULT_PARAM = "param";

    @Override
    public List<TaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date dateFrom, Date dateTo, boolean showDeleted, int start, int end) {
        StringBuilder queryBuilder = new StringBuilder("SELECT taskTime FROM TaskTime taskTime WHERE 1=1 ");

        BuildParamResult buildParamResult = buildParams(site, user, task, taskType, dateFrom, dateTo, showDeleted);
        queryBuilder.append(buildParamResult.queryBuilder.toString());
        Map<String, Object> paramMap = buildParamResult.paramMap;
        logger.debug("getAll queryBuilder={}", queryBuilder);
        logger.debug("getAll paramMap={}", paramMap);

        TypedQuery<TaskTime> query = getEntityManager().createQuery(queryBuilder.toString(), TaskTime.class);
        for (String param : paramMap.keySet()) {
            query.setParameter(param, paramMap.get(param));
        }

        if (start > 0) {
            query.setFirstResult(start);
        }
        if (end > 0) {
            query.setMaxResults(end - start);
        }

        return query.getResultList();
    }

    @Override
    public Long countAll(Site site, User user, Task task, TaskType taskType, Date dateFrom, Date dateTo, boolean withDeleted) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(taskTime) FROM TaskTime taskTime WHERE 1=1 ");

        BuildParamResult buildParamResult = buildParams(site, user, task, taskType, dateFrom, dateTo, withDeleted);
        queryBuilder.append(buildParamResult.queryBuilder.toString());
        Map<String, Object> paramMap = buildParamResult.paramMap;
        logger.debug("getAll queryBuilder={}", queryBuilder);
        logger.debug("getAll paramMap={}", paramMap);

        TypedQuery<Long> query = getEntityManager().createQuery(queryBuilder.toString(), Long.class);
        for (String param : paramMap.keySet()) {
            query.setParameter(param, paramMap.get(param));
        }

        return query.getSingleResult();
    }

    private BuildParamResult buildParams(Site site, User user, Task task, TaskType taskType, Date dateFrom, Date dateTo, boolean showDeleted) {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> paramMap = new HashMap<>();
        if (!showDeleted) {
            queryBuilder.append(" and taskTime.deleted = false ");
        }
        if (site != null && site.getId() != null) {
            queryBuilder.append(" and taskTime.userSiteTaskById.siteTask.siteBySiteId.id = :siteId");
            paramMap.put("siteId", site.getId());
        }
        if (user != null && user.getUsername() != null && !user.getUsername().isEmpty()) {
            queryBuilder.append(" and taskTime.userSiteTaskById.userByUsername.username = :username");
            paramMap.put("username", user.getUsername());
        }
        if (task != null && task.getId() != null) {
            queryBuilder.append(" and taskTime.userSiteTaskById.siteTask.taskByTaskId.id = :taskId");
            paramMap.put("taskId", task.getId());
        }
        if (taskType != null) {
            queryBuilder.append(" and taskTime.userSiteTaskById.siteTask.taskByTaskId.type = :taskType");
            paramMap.put("taskType", taskType.getTypeStr());
        }
        if (dateFrom != null) {
            queryBuilder.append(" and taskTime.startTime >= :dateFrom");
            paramMap.put("dateFrom", dateFrom);
        }
        if (dateTo != null) {
            queryBuilder.append(" and taskTime.startTime <= :dateTo");
            paramMap.put("dateTo", dateTo);
        }
        return new BuildParamResult(queryBuilder, paramMap);
    }

    private static class BuildParamResult {
        StringBuilder queryBuilder;
        Map<String, Object> paramMap;

        BuildParamResult(StringBuilder queryBuilder, Map<String, Object> paramMap) {
            this.queryBuilder = queryBuilder;
            this.paramMap = paramMap;
        }
    }
}
