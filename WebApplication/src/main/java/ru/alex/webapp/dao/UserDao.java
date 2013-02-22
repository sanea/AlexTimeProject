package ru.alex.webapp.dao;

import ru.alex.webapp.model.User;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserDao extends GenericDao<User, String> {
    List<User> getEnabledUsers();
}
