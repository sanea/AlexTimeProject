package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.GroupDao;
import ru.alex.webapp.model.Group;
import ru.alex.webapp.model.GroupMember;
import ru.alex.webapp.service.GroupService;

import java.util.Collection;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class GroupServiceImpl extends GenericServiceImpl<Group, Long> implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    @Autowired
    GroupDao groupDao;

    @Override
    protected GenericDao<Group, Long> getDao() {
        return groupDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(Group entity) throws Exception {
        logger.debug("update group={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        groupDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(Group entity) throws Exception {
        logger.debug("add group={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getGroupName() == null || entity.getGroupName().equals(""))
            throw new Exception("Name can't be empty");
        groupDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(Group entity) throws Exception {
        logger.debug("remove group={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        Group mergedEntity = groupDao.merge(entity);
        Collection<GroupMember> groupMembers = mergedEntity.getGroupMemberById();
        logger.debug("remove groupMembers={}", groupMembers);
        if (groupMembers != null && groupMembers.size() > 0)
            throw new Exception("Can't remove group with assigned Users.");
        groupDao.remove(mergedEntity); //Cascade.ALL
    }
}
