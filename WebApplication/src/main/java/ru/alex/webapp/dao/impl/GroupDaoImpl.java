package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.GroupDao;
import ru.alex.webapp.model.Group;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class GroupDaoImpl extends GenericDaoImpl<Group, Long> implements GroupDao {

}
