package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.UserSiteTaskDao;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.service.UserSiteTaskService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserSiteTaskServiceImpl extends GenericServiceImpl<UserSiteTask, Long> implements UserSiteTaskService {
    private static final Logger logger = LoggerFactory.getLogger(UserSiteTaskServiceImpl.class);
    @Autowired
    UserSiteTaskDao userSiteTaskDao;
    @Autowired
    UserTaskTimeDao userTaskTimeDao;

    @Override
    protected GenericDao<UserSiteTask, Long> getDao() {
        return userSiteTaskDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(UserSiteTask entity) throws Exception {
        logger.debug("update userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        userSiteTaskDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(UserSiteTask entity) throws Exception {
        logger.debug("add userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getSiteTask() == null || entity.getUserByUsername() == null
                || entity.getStatus() == null || entity.getUpdateTime() == null
                || entity.getCreateTime() == null || entity.getDeleted() == null)
            throw new Exception("Wrong params");
        entity.setDeleted(false);
        userSiteTaskDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(UserSiteTask entity) throws Exception {
        logger.debug("remove userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());

        Map<String, Object> params = new HashMap<>(1);
        params.put("userSiteTaskId", entity.getId());
        Collection<UserTaskTime> userTaskTimeList = userTaskTimeDao.findWithNamedQuery(UserTaskTime.BY_USER_SITE_TASK, params);
        logger.debug("remove BY_USER_SITE_TASK userTaskTimeList={}", userTaskTimeList);
        if (userTaskTimeList == null || userTaskTimeList.size() == 0) {
            entity = userSiteTaskDao.merge(entity);
            userSiteTaskDao.remove(entity);
        } else {
            entity.setDeleted(true);
            userSiteTaskDao.merge(entity);
        }
    }

}
