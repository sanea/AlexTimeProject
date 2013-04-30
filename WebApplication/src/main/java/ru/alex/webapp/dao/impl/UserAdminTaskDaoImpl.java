package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserAdminTaskDao;
import ru.alex.webapp.model.UserAdminTask;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserAdminTaskDaoImpl extends GenericDaoImpl<UserAdminTask, Long> implements UserAdminTaskDao {
}
