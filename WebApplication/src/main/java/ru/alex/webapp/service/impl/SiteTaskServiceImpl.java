package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.SiteTaskDao;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.SiteTask;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.service.SiteTaskService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class SiteTaskServiceImpl extends GenericServiceImpl<SiteTask, Long> implements SiteTaskService {
    private static final Logger logger = LoggerFactory.getLogger(SiteTaskServiceImpl.class);
    @Autowired
    SiteTaskDao siteTaskDao;
    @Autowired
    UserTaskTimeDao userTaskTimeDao;

    @Override
    protected GenericDao<SiteTask, Long> getDao() {
        return siteTaskDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(SiteTask entity) throws Exception {
        logger.debug("update siteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        siteTaskDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(SiteTask entity) throws Exception {
        logger.debug("add siteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getSiteBySiteId() == null || entity.getTaskByTaskId() == null)
            throw new Exception("Wrong params");
        entity.setDeleted(false);
        siteTaskDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(SiteTask entity) throws Exception {
        logger.debug("remove siteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());

        Map<String, Object> params = new HashMap<>(1);
        params.put("siteTaskId", entity.getId());
        Collection<UserTaskTime> userTaskTimeList = userTaskTimeDao.findWithNamedQuery(UserTaskTime.BY_SITE_TASK, params);
        logger.debug("remove BY_SITE_TASK userTaskTimeList={}", userTaskTimeList);
        if (userTaskTimeList == null || userTaskTimeList.size() == 0) {
            entity = siteTaskDao.merge(entity);
            entity.getTaskByTaskId().getSiteTasksById().remove(entity);
            entity.getSiteBySiteId().getSiteTaskList().remove(entity);
            siteTaskDao.remove(entity);
        } else {
            entity.setDeleted(true);
            siteTaskDao.merge(entity);
        }
    }

    @Override
    public List<SiteTask> getNotDeletedSiteTasks(Site site) throws Exception {
        logger.debug("getNotDeletedSiteTasks site={}", site);
        if (site == null || site.getId() == null)
            throw new IllegalArgumentException("Wrong site");
        Map<String, Object> params = new HashMap<>(1);
        params.put("siteId", site.getId());
        List<SiteTask> siteTaskList = siteTaskDao.findWithNamedQuery(SiteTask.ALL_NOT_DELETED_BY_SITE, params);
        logger.debug("getNotDeletedSiteTasks siteTaskList={}", siteTaskList);
        return siteTaskList;
    }

    @Override
    public SiteTask getBySiteTask(Site site, Task task) throws Exception {
        logger.debug("getBySiteTask site={}, task={}", site, task);
        if (site == null || site.getId() == null)
            throw new IllegalArgumentException("Wrong site");
        if (task == null || task.getId() == null)
            throw new IllegalArgumentException("Wrong task");
        Map<String, Object> params = new HashMap<>(2);
        params.put("siteId", site.getId());
        params.put("taskId", task.getId());
        List<SiteTask> siteTaskList = siteTaskDao.findWithNamedQuery(SiteTask.BY_SITE_TASK, params);
        logger.debug("getBySiteTask siteTaskList={}", siteTaskList);
        return siteTaskList == null || siteTaskList.size() == 0 ? null : siteTaskList.get(0);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void addSiteTask(Site site, Task task) throws Exception {
        logger.debug("addSiteTask site={}, task={}", site, task);
        SiteTask siteTask = getBySiteTask(site, task);
        if (siteTask == null) {
            siteTask = new SiteTask();
            siteTask.setSiteBySiteId(site);
            siteTask.setTaskByTaskId(task);
            siteTask.setDeleted(false);
            add(siteTask);
        } else {
            if (siteTask.getDeleted()) {
                siteTask.setDeleted(false);
                siteTaskDao.merge(siteTask);
            } else {
                throw new Exception("Can't add, siteTask exists already with siteId=" + site.getId() + ", taskId=" + task.getId());
            }
        }
    }

    @Override
    public void removeSiteTask(Site site, Task task) throws Exception {
        logger.debug("removeSiteTask site={}, task={}", site, task);
        SiteTask siteTask = getBySiteTask(site, task);
        if (siteTask == null || siteTask.getDeleted()) {
            throw new Exception("Can't remove, siteTask is removed, siteId=" + site.getId() + ", taskId=" + task.getId());
        }
        remove(siteTask);
    }
}
