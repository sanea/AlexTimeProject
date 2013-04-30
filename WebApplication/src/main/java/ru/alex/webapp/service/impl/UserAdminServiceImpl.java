package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.UserAdminSiteDao;
import ru.alex.webapp.dao.UserAdminSuperDao;
import ru.alex.webapp.dao.UserAdminTaskDao;
import ru.alex.webapp.model.UserAdmin;
import ru.alex.webapp.model.UserAdminSite;
import ru.alex.webapp.model.UserAdminSuper;
import ru.alex.webapp.model.UserAdminTask;
import ru.alex.webapp.service.UserAdminService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserAdminServiceImpl extends GenericServiceImpl<UserAdmin, Long> implements UserAdminService {
    private static final Logger logger = LoggerFactory.getLogger(UserAdminServiceImpl.class);

    @Autowired
    UserAdminSuperDao userAdminSuperDao;
    @Autowired
    UserAdminSiteDao userAdminSiteDao;
    @Autowired
    UserAdminTaskDao userAdminTaskDao;

    @Override
    protected GenericDao<UserAdmin, Long> getDao() {
        throw new UnsupportedOperationException("can't get distinct Dao");
    }

    @Override
    public UserAdmin findById(Long id) {
        UserAdmin userAdmin = userAdminSuperDao.findById(id);
        if (userAdmin != null)
            return userAdmin;
        userAdmin = userAdminSiteDao.findById(id);
        if (userAdmin != null)
            return userAdmin;
        userAdmin = userAdminTaskDao.findById(id);
        if (userAdmin != null)
            return userAdmin;
        return null;
    }

    @Override
    public List<UserAdmin> findAll() {
        List<UserAdmin> userAdminList = new ArrayList<>();
        userAdminList.addAll(userAdminSuperDao.findAll());
        userAdminList.addAll(userAdminSiteDao.findAll());
        userAdminList.addAll(userAdminTaskDao.findAll());
        return userAdminList;
    }

    @Override
    public Long count() {
        return userAdminSuperDao.count() + userAdminSiteDao.count() + userAdminTaskDao.count();
    }

    @Override
    protected void throwExceptionIfNotExists(Long aLong) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(UserAdmin entity) throws Exception {
        logger.debug("update userAdmin={}", entity);
        //TODO
        throw new NotImplementedException();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(UserAdmin entity) throws Exception {
        logger.debug("add userAdmin={}", entity);
        //TODO
        throw new NotImplementedException();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(UserAdmin entity) throws Exception {
        throw new UnsupportedOperationException("Can't remove userChange, only finish!");
    }

    @Override
    public List<UserAdminSuper> findAllSuper() {
        return userAdminSuperDao.findAll();
    }

    @Override
    public List<UserAdminSite> findAllSite() {
        return userAdminSiteDao.findAll();
    }

    @Override
    public List<UserAdminTask> findAllTask() {
        return userAdminTaskDao.findAll();
    }
}
