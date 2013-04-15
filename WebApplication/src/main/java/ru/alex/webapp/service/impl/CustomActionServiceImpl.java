package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.CustomActionDao;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.model.CustomAction;
import ru.alex.webapp.service.CustomActionService;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CustomActionServiceImpl extends GenericServiceImpl<CustomAction, Long> implements CustomActionService {
    private static final Logger logger = LoggerFactory.getLogger(CustomActionServiceImpl.class);
    @Autowired
    private CustomActionDao customActionDao;

    @Override
    protected GenericDao<CustomAction, Long> getDao() {
        return customActionDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(CustomAction entity) throws Exception {
        logger.debug("update customAction={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        customActionDao.merge(entity);
    }

    @Override
    public void add(CustomAction entity) throws Exception {
        throw new NoSuchMethodException("not supported");
    }

    @Override
    public void remove(CustomAction entity) throws Exception {
        throw new NoSuchMethodException("not supported");
    }
}
