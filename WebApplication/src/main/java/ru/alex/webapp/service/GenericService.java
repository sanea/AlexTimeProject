package ru.alex.webapp.service;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alex
 */
public interface GenericService<T, ID extends Serializable> {
    static final String REMOVED_NAME_APPEND = "_removed";

    T findById(ID id);

    List<T> findAll();

    Long count();

    void edit(T entity) throws Exception;

    void add(T entity) throws Exception;

    void remove(T entity) throws Exception;
}
