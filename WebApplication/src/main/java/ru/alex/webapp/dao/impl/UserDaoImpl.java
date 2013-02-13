package ru.alex.webapp.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.model.Users;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserDaoImpl extends GenericDAOImpl<Users, String> implements UserDao {
}
