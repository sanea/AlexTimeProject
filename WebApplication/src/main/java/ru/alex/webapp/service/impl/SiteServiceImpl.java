package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.SiteDao;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.service.SiteService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class SiteServiceImpl extends GenericServiceImpl<Site, Long> implements SiteService {
    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);
    @Autowired
    SiteDao siteDao;
    @Autowired
    UserTaskTimeDao userTaskTimeDao;

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
        entity.setDeleted(false);
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

        Map<String, Object> params = new HashMap<>(1);
        params.put("siteId", entity.getId());
        Collection<UserTaskTime> userTaskTimeList = userTaskTimeDao.findWithNamedQuery(UserTaskTime.BY_SITE_ID, params);
        logger.debug("remove BY_SITE_ID userTaskTimeList={}", userTaskTimeList);
        if (userTaskTimeList == null || userTaskTimeList.size() == 0) {
            entity = siteDao.merge(entity);
            siteDao.remove(entity);
        } else {
            entity.setName(entity.getName() + REMOVED_NAME_APPEND);
            entity.setDeleted(true);
            siteDao.merge(entity);
        }
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
            site = siteDao.merge(site);
            Map<String, Object> params = new HashMap<>(1);
            params.put("siteId", site.getId());
            Collection<UserTaskTime> userTaskTimeList = userTaskTimeDao.findWithNamedQuery(UserTaskTime.CURRENT_BY_SITE_ID, params);
            logger.debug("isSiteDeletable CURRENT_BY_SITE_ID userTaskTimeList={}", userTaskTimeList);
            if (userTaskTimeList == null || userTaskTimeList.size() == 0)
                result = true;
            else
                result = false;
        }
        logger.debug("isSiteDeletable " + result);
        return result;
    }


}
