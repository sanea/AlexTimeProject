package ru.alex.webapp.service.impl;

import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.service.GenericService;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alex
 */
public abstract class GenericServiceImpl<T, ID extends Serializable> implements GenericService<T, ID> {

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
}
