package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserChangeDao;
import ru.alex.webapp.model.UserChange;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserChangeDaoImpl extends GenericDaoImpl<UserChange, Long> implements UserChangeDao {
}
