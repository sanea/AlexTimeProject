package ru.alex.webapp.dao;

import java.util.List;
import java.io.Serializable;

/**
 * @author Alexander.Isaenco
 */
public interface GenericDao<T, ID extends Serializable> {

    T findById(ID id, boolean lock);

    List<T> findAll();

    T makePersistent(T entity);

    void makeTransient(T entity);

}
