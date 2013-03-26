package ru.alex.webapp.dao;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander.Isaenco
 */
public interface GenericDao<T, ID extends Serializable> {

    Class<T> getEntityBeanType();

    T findById(ID id);

    List<T> findAll();

    Long count();

    T merge(T entity);

    void persist(T entity);

    void remove(T entity);

    void refresh(T entity);

    void flush();

    void clear();

    void lock(T entity, LockModeType lockModeType);

    List<T> findWithNamedQuery(String namedQueryName);

    List<T> findWithNamedQuery(String namedQueryName, int resultLimit);

    List<T> findWithNamedQuery(String namedQueryName, int start, int end);

    List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters);

    List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit);

    List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int start, int end);
}
