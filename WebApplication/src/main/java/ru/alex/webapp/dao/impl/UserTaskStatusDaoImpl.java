package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserTaskStatusDao;
import ru.alex.webapp.model.UserTaskStatus;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserTaskStatusDaoImpl extends GenericDaoImpl<UserTaskStatus, Long> implements UserTaskStatusDao {
}
