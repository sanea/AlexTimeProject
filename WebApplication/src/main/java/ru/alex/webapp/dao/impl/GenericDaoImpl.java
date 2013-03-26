package ru.alex.webapp.dao.impl;

import ru.alex.webapp.dao.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Alexander.Isaenco
 */
public abstract class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    private final Class<T> entityBeanType;
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        this.entityBeanType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public EntityManager getEntityManager() {
        if (em == null)
            throw new IllegalStateException("EntityManager has not been set on DAO before usage");
        return em;
    }

    @Override
    public Class<T> getEntityBeanType() {
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
    public void remove(T entity) {
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

    @Override
    public List<T> findWithNamedQuery(String namedQueryName) {
        return findWithNamedQuery(namedQueryName, 0, 0);
    }

    @Override
    public List<T> findWithNamedQuery(String namedQueryName, int resultLimit) {
        return findWithNamedQuery(namedQueryName, 0, resultLimit);
    }

    @Override
    public List<T> findWithNamedQuery(String namedQueryName, int start, int end) {
        if (start < 0 || end < 0)
            throw new IllegalArgumentException("start and end should > 0");
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQueryName, getEntityBeanType());
        if (start > 0) {
            query.setFirstResult(start);
        }
        if (end > 0) {
            query.setMaxResults(end - start);
        }
        return query.getResultList();
    }

    @Override
    public List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0, 0);
    }

    @Override
    public List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit) {
        return findWithNamedQuery(namedQueryName, parameters, 0, resultLimit);
    }

    @Override
    public List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int start, int end) {
        if (start < 0 || end < 0)
            throw new IllegalArgumentException("start and end should > 0");
        Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQueryName, getEntityBeanType());
        if (start > 0) {
            query.setFirstResult(start);
        }
        if (end > 0) {
            query.setMaxResults(end - start);
        }
        for (Map.Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }
}

