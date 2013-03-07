package ru.alex.webapp.dao;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface GenericDao<T, ID extends Serializable> {

    T findById(ID id);

    List<T> findAll();

    Long count();

    T merge(T entity);

    void persist(T entity);

    void makeTransient(T entity);

    void refresh(T entity);

    public void flush();

    public void clear();

    public void lock(T entity, LockModeType lockModeType);

}
