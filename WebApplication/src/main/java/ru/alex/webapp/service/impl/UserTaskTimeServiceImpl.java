package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.UserTaskTimeService;

import java.util.Date;
import java.util.List;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserTaskTimeServiceImpl extends GenericServiceImpl<UserTaskTime, Long> implements UserTaskTimeService {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    @Autowired
    UserTaskTimeDao userTaskTimeDao;

    @Override
    protected GenericDao<UserTaskTime, Long> getDao() {
        return userTaskTimeDao;
    }

    @Override
    public void update(UserTaskTime entity) throws Exception {
        throw new UnsupportedOperationException("Can't update");
    }

    @Override
    public void add(UserTaskTime entity) throws Exception {
        throw new UnsupportedOperationException("Adding is on process start");
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(UserTaskTime entity) throws Exception {
        if (entity == null)
            throw new IllegalArgumentException("UserTaskTime is null");
        if (entity.getTotal() == null && entity.getUserSiteTaskById().getCurrentTime().equals(entity))
            throw new Exception("Can't remove current time!");
        entity.setDeleted(true);
        userTaskTimeDao.merge(entity);
    }

    @Override
    public List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to) throws Exception {
        return getAll(site, user, task, taskType, from, to, false, 0, 0);
    }

    @Override
    public List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to, boolean showDeleted, int start, int end) throws Exception {
        logger.debug("getAll site={}, user={}, task={}, taskType={}, from={}, to={}, showDeleted={}, start={}, end={}", site, user, task, taskType, from, to, showDeleted, start, end);
        if (site == null && user == null && task == null && taskType == null && from == null && to == null) {
            if (showDeleted) {
                return userTaskTimeDao.findWithNamedQuery(UserTaskTime.ALL, start, end);
            } else {
                return userTaskTimeDao.findWithNamedQuery(UserTaskTime.ALL_NOT_DELETED, start, end);
            }
        }
        if (start < 0 || end < 0)
            throw new IllegalArgumentException("start and end should > 0");
        if ((site != null && site.getId() == null) || (user != null && user.getUsername() == null)
                || (task != null && task.getId() == null))
            throw new IllegalArgumentException("Wrong input params");
        return userTaskTimeDao.getAll(site, user, task, taskType, from, to, showDeleted, start, end);
    }

    @Override
    public Long getAllCount(Site site, User user, Task task, TaskType taskType, Date from, Date to, boolean withDeleted) throws Exception {
        logger.debug("getAll site={}, user={}, task={}, taskType={}, from={}, to={}, withDeleted={}", site, user, task, taskType, from, to, withDeleted);
        if (site == null && user == null && task == null && taskType == null && from == null && to == null) {
            if (withDeleted) {
                return userTaskTimeDao.count();
            } else {
                return userTaskTimeDao.countWithNamedQuery(UserTaskTime.COUNT_NOT_DELETED);
            }
        }
        if ((site != null && site.getId() == null) || (user != null && user.getUsername() == null)
                || (task != null && task.getId() == null))
            throw new IllegalArgumentException("Wrong input params");
        return userTaskTimeDao.countAll(site, user, task, taskType, from, to, withDeleted);
    }
}
