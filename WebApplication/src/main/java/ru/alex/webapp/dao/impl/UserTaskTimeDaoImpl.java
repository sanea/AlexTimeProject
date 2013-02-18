package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.UserTaskTime;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskTimeDaoImpl extends GenericDaoImpl<UserTaskTime, Long> implements UserTaskTimeDao {
}
