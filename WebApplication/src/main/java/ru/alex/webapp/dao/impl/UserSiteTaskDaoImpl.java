package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserSiteTaskDao;
import ru.alex.webapp.model.UserSiteTask;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserSiteTaskDaoImpl extends GenericDaoImpl<UserSiteTask, Long> implements UserSiteTaskDao {
}
