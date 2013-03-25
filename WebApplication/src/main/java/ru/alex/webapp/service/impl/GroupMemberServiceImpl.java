package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.GroupMemberDao;
import ru.alex.webapp.model.GroupMember;
import ru.alex.webapp.service.GroupMemberService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class GroupMemberServiceImpl extends GenericServiceImpl<GroupMember, Long> implements GroupMemberService {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    @Autowired
    GroupMemberDao groupMemberDao;

    @Override
    protected GenericDao<GroupMember, Long> getDao() {
        return groupMemberDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(GroupMember entity) throws Exception {
        logger.debug("update groupMember={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity, entity.getId());
        groupMemberDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(GroupMember entity) throws Exception {
        logger.debug("add groupMember={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getGroupByGroupId() == null || entity.getUserByUsername() == null)
            throw new Exception("User and group can't be null");
        Map<String, Object> params = new HashMap<>(1);
        params.put("username", entity.getUserByUsername().getUsername());
        List<GroupMember> groupMembers = groupMemberDao.findWithNamedQuery(GroupMember.BY_USERNAME, params);
        logger.debug("add groupMembers by username={}", groupMembers);
        if (groupMembers != null && groupMembers.size() > 0)
            throw new Exception("Username should be unique");
        groupMemberDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(GroupMember entity) throws Exception {
        logger.debug("remove groupMember={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity, entity.getId());
        GroupMember mergedEntity = groupMemberDao.merge(entity);
        mergedEntity.getUserByUsername().setGroupMemberByUsername(null);
        mergedEntity.getGroupByGroupId().getGroupAuthorityById().remove(mergedEntity);
        groupMemberDao.remove(mergedEntity);
    }

}
