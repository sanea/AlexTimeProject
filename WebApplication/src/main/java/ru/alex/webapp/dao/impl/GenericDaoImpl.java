package ru.alex.webapp.dao.impl;

import ru.alex.webapp.dao.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public abstract class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    private Class<T> entityBeanType;
    @PersistenceContext
    private EntityManager em;

    public GenericDaoImpl() {
        this.entityBeanType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public EntityManager getEntityManager() {
        if (em == null)
            throw new IllegalStateException("EntityManager has not been set on DAO before usage");
        return em;
    }

    protected Class<T> getEntityBeanType() {
        return entityBeanType;
    }

    @Override
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock) {
            entity = getEntityManager().find(getEntityBeanType(), id);
            getEntityManager().lock(entity, LockModeType.WRITE);
        } else {
            entity = getEntityManager().find(getEntityBeanType(), id);
        }
        return entity;
    }

    public List<T> findAll() {
        return getEntityManager().createQuery("from " + getEntityBeanType().getName()).getResultList();
    }

    @Override
    public T makePersistent(T entity) {
        return getEntityManager().merge(entity);
    }

    @Override
    public void makeTransient(T entity) {
        getEntityManager().remove(entity);
    }

    public void flush() {
        getEntityManager().flush();
    }

    public void clear() {
        getEntityManager().clear();
    }

}

