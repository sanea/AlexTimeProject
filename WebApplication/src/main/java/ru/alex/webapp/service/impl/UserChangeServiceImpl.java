package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.UserChangeDao;
import ru.alex.webapp.model.UserChange;
import ru.alex.webapp.service.UserChangeService;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserChangeServiceImpl extends GenericServiceImpl<UserChange, Long> implements UserChangeService {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    @Autowired
    UserChangeDao userChangeDao;

    @Override
    protected GenericDao<UserChange, Long> getDao() {
        return userChangeDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(UserChange entity) throws Exception {
        logger.debug("update userChange={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        UserChange userChange = userChangeDao.findById(entity.getId());
        if (!entity.getStartTime().equals(userChange.getStartTime()))
            throw new Exception("Can't change start time.");
        if (entity.getEndTime() == null)
            throw new Exception("Nothing to update, set end time.");
        userChangeDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(UserChange entity) throws Exception {
        logger.debug("add userChange={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getStartTime() == null)
            throw new Exception("StartTime can't be empty");
        if (entity.getSite() == null)
            throw new Exception("Site can't be empty");
        if (entity.getUser() == null)
            throw new Exception("User can't be empty");
        userChangeDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(UserChange entity) throws Exception {
        throw new UnsupportedOperationException("Can't remove userChange, only finish!");
    }
}
