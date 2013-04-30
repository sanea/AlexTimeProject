package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.UserAdminSiteDao;
import ru.alex.webapp.model.UserAdminSite;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class UserAdminSiteDaoImpl extends GenericDaoImpl<UserAdminSite, Long> implements UserAdminSiteDao {
}
