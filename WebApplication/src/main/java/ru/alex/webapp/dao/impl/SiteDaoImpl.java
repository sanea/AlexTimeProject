package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.SiteDao;
import ru.alex.webapp.model.Site;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class SiteDaoImpl extends GenericDaoImpl<Site, Long> implements SiteDao {

}
