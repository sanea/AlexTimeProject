package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.TaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.TaskTime;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.TaskTimeService;

import java.util.Date;
import java.util.List;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class TaskTimeServiceImpl extends GenericServiceImpl<TaskTime, Long> implements TaskTimeService {
    private static final Logger logger = LoggerFactory.getLogger(TaskTimeServiceImpl.class);
    @Autowired
    TaskTimeDao taskTimeDao;

    @Override
    protected GenericDao<TaskTime, Long> getDao() {
        return taskTimeDao;
    }

    @Override
    public void update(TaskTime entity) throws Exception {
        throw new UnsupportedOperationException("Can't update");
    }

    @Override
    public void add(TaskTime entity) throws Exception {
        throw new UnsupportedOperationException("Adding is on process start");
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(TaskTime entity) throws Exception {
        if (entity == null)
            throw new IllegalArgumentException("TaskTime is null");
        if (entity.getTotal() == null && entity.getUserSiteTaskById().getCurrentTime().equals(entity))
            throw new Exception("Can't remove current time!");
        if (entity.getDeleted())
            throw new Exception("Can't remove deleted time!");
        entity.setDeleted(true);
        taskTimeDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void restore(TaskTime entity) throws Exception {
        if (entity == null)
            throw new IllegalArgumentException("TaskTime is null");
        if (entity.getTotal() == null && entity.getUserSiteTaskById().getCurrentTime().equals(entity))
            throw new Exception("Can't remove current time!");
        if (!entity.getDeleted())
            throw new Exception("Can't restore not deleted time!");
        entity.setDeleted(false);
        taskTimeDao.merge(entity);
    }

    @Override
    public List<TaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to) throws Exception {
        return getAll(site, user, task, taskType, from, to, false, 0, 0);
    }

    @Override
    public List<TaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to, boolean showDeleted, int start, int end) throws Exception {
        logger.debug("getAll site={}, user={}, task={}, taskType={}, from={}, to={}, showDeleted={}, start={}, end={}", site, user, task, taskType, from, to, showDeleted, start, end);
        if (site == null && user == null && task == null && taskType == null && from == null && to == null) {
            if (showDeleted) {
                return taskTimeDao.findWithNamedQuery(TaskTime.ALL, start, end);
            } else {
                return taskTimeDao.findWithNamedQuery(TaskTime.ALL_NOT_DELETED, start, end);
            }
        }
        if (start < 0 || end < 0)
            throw new IllegalArgumentException("start and end should > 0");
        if ((site != null && site.getId() == null) || (user != null && user.getUsername() == null)
                || (task != null && task.getId() == null))
            throw new IllegalArgumentException("Wrong input params");
        return taskTimeDao.getAll(site, user, task, taskType, from, to, showDeleted, start, end);
    }

    @Override
    public Long getAllCount(Site site, User user, Task task, TaskType taskType, Date from, Date to, boolean withDeleted) throws Exception {
        logger.debug("getAll site={}, user={}, task={}, taskType={}, from={}, to={}, withDeleted={}", site, user, task, taskType, from, to, withDeleted);
        if (site == null && user == null && task == null && taskType == null && from == null && to == null) {
            if (withDeleted) {
                return taskTimeDao.count();
            } else {
                return taskTimeDao.countWithNamedQuery(TaskTime.COUNT_NOT_DELETED);
            }
        }
        if ((site != null && site.getId() == null) || (user != null && user.getUsername() == null)
                || (task != null && task.getId() == null))
            throw new IllegalArgumentException("Wrong input params");
        return taskTimeDao.countAll(site, user, task, taskType, from, to, withDeleted);
    }
}
