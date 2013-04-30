package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.GroupAuthorityDao;
import ru.alex.webapp.model.GroupAuthority;
import ru.alex.webapp.service.GroupAuthorityService;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class GroupAuthorityServiceImpl extends GenericServiceImpl<GroupAuthority, Long> implements GroupAuthorityService {
    private static final Logger logger = LoggerFactory.getLogger(GroupAuthorityServiceImpl.class);
    @Autowired
    GroupAuthorityDao groupAuthorityDao;

    @Override
    protected GenericDao<GroupAuthority, Long> getDao() {
        return groupAuthorityDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(GroupAuthority entity) throws Exception {
        logger.debug("update groupAuthority={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        groupAuthorityDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(GroupAuthority entity) throws Exception {
        logger.debug("add groupAuthority={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getGroupByGroupId() == null || entity.getAuthority() == null || entity.getAuthority().equals(""))
            throw new Exception("Group and Authority can't be null");
        groupAuthorityDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(GroupAuthority entity) throws Exception {
        logger.debug("remove groupAuthority={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        GroupAuthority mergedEntity = groupAuthorityDao.merge(entity);
        mergedEntity.getGroupByGroupId().getGroupAuthorityById().remove(mergedEntity);
        groupAuthorityDao.remove(mergedEntity);
    }

}
