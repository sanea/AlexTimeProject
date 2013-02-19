package ru.alex.webapp.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface GenericDao<T, ID extends Serializable> {

    T findById(ID id, boolean lock);

    List<T> findAll();

    T merge(T entity);

    void persist(T entity);

    void makeTransient(T entity);

    public void flush();

    public void clear();
}
