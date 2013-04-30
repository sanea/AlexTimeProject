package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserAdminSuperDao;
import ru.alex.webapp.model.UserAdminSuper;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserAdminSuperDaoImpl extends GenericDaoImpl<UserAdminSuper, Long> implements UserAdminSuperDao {
}
