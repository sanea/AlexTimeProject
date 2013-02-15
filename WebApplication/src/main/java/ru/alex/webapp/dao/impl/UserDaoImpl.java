package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.model.Users;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserDaoImpl extends GenericDaoImpl<Users, String> implements UserDao {
}
