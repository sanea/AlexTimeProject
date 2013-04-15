package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.CustomActionDao;
import ru.alex.webapp.model.CustomAction;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class CustomActionDaoImpl extends GenericDaoImpl<CustomAction, Long> implements CustomActionDao {

}
