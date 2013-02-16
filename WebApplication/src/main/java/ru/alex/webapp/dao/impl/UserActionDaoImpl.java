package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserActionDao;
import ru.alex.webapp.model.UserAction;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserActionDaoImpl extends GenericDaoImpl<UserAction, Long> implements UserActionDao {
}
