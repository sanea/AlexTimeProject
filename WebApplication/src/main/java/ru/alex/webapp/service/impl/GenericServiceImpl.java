package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.service.GenericService;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alex
 */
public abstract class GenericServiceImpl<T, ID extends Serializable> implements GenericService<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);

    protected abstract GenericDao<T, ID> getDao();

    @Override
    public T findById(ID id) {
        return getDao().findById(id);
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    public Long count() {
        return getDao().count();
    }

    protected void throwExceptionIfNotExists(ID id) throws Exception {
        T mergedEntity = getDao().findById(id);
        logger.debug("throwExceptionIfNotExists {}={}", getDao().getEntityBeanType().getName(), mergedEntity);
        if (mergedEntity == null)
            throw new Exception("Can't find " + getDao().getEntityBeanType().getName() + " with id=" + id);
    }
}
