package ru.alex.webapp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alex.webapp.model.UsersEntity;

import java.util.List;

/**
 * User: Alexander.Isaenco
 * Date: 05.02.13
 * Time: 19:49
 */
@Repository
public class UserDaoImpl implements UserDao {
    private SessionFactory sessionFactory;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void addUser(UsersEntity user) {
        currentSession().save(user);
    }

    @Override
    public void updateUser(UsersEntity user) {
        currentSession().update(user);
    }

    @Override
    public UsersEntity getUserByUsername(String username) {
        return (UsersEntity) currentSession().get(UsersEntity.class, username);
    }

    @Override
    public void deleteUser(String username) {
        currentSession().delete(username, UsersEntity.class);
    }

    @Override
    public List<UsersEntity> findAllUsers() {
        return currentSession().createQuery("select ue from UsersEntity ue").list();
    }
}
