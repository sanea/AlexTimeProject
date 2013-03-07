package ru.alex.webapp.dao.impl;

import ru.alex.webapp.dao.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
    public T findById(ID id) {
        return getEntityManager().find(getEntityBeanType(), id);
    }

    public List<T> findAll() {
        return getEntityManager().createQuery("from " + getEntityBeanType().getName(), getEntityBeanType()).getResultList();
    }

    @Override
    public Long count() {
        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(getEntityBeanType())));
        return getEntityManager().createQuery(cq).getSingleResult();
    }

    @Override
    public T merge(T entity) {
        return getEntityManager().merge(entity);
    }

    @Override
    public void persist(T entity) {
        getEntityManager().persist(entity);
    }

    @Override
    public void makeTransient(T entity) {
        getEntityManager().remove(entity);
    }

    @Override
    public void refresh(T entity) {
        if (getEntityManager().contains(entity)) {
            getEntityManager().refresh(entity);
        }
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    @Override
    public void clear() {
        getEntityManager().clear();
    }

    @Override
    public void lock(T entity, LockModeType lockModeType) {
        getEntityManager().lock(entity, lockModeType);
    }
}

