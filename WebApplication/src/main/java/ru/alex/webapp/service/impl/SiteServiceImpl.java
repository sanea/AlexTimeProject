package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.SiteDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.UserChange;
import ru.alex.webapp.service.SiteService;

import java.util.Collection;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class SiteServiceImpl extends GenericServiceImpl<Site, Long> implements SiteService {
    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);
    @Autowired
    SiteDao siteDao;

    @Override
    protected GenericDao<Site, Long> getDao() {
        return siteDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(Site entity) throws Exception {
        logger.debug("update site={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity, entity.getId());
        siteDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(Site entity) throws Exception {
        logger.debug("add site={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getName() == null || entity.getName().equals(""))
            throw new Exception("Name can't be empty");
        siteDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(Site entity) throws Exception {
        logger.debug("remove site={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity, entity.getId());
        if (!isSiteDeletable(entity))
            throw new Exception("Site has active user changes, please wait or close user session.");
        entity.setName(entity.getName() + REMOVED_NAME_APPEND);
        entity.setDeleted(true);
        siteDao.merge(entity);
    }

    @Override
    public boolean isSiteDeletable(Site site) throws Exception {
        logger.debug("isSiteDeletable site={}", site);
        if (site == null)
            throw new IllegalArgumentException("Wrong site");
        throwExceptionIfNotExists(site, site.getId());
        boolean result;
        if (site.getDeleted()) {
            result = false;
        } else {
            Collection<UserChange> userChangeList = site.getUserChangeList();
            logger.debug("isSiteDeletable userChangeList={}", userChangeList);
            if (userChangeList == null || userChangeList.size() == 0)
                result = true;
            else
                result = false;
        }
        logger.debug("isSiteDeletable " + result);
        return result;
    }


}
