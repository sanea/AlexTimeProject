package ru.alex.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.alex.webapp.dao.GroupMemberDao;
import ru.alex.webapp.model.GroupMember;

/**
 * @author Alexander.Isaenco
 */
@Repository
public class GroupMemberDaoImpl extends GenericDaoImpl<GroupMember, Long> implements GroupMemberDao {

}
