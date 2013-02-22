package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.model.User;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserDaoImpl extends GenericDaoImpl<User, String> implements UserDao {
    @Override
    public List<User> getEnabledUsers() {
        return getEntityManager().createQuery("select u from User u where u.enabled = true", User.class).getResultList();
    }
}
