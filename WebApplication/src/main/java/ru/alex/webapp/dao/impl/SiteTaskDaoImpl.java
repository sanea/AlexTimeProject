package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.SiteTaskDao;
import ru.alex.webapp.model.SiteTask;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class SiteTaskDaoImpl extends GenericDaoImpl<SiteTask, Long> implements SiteTaskDao {

}
