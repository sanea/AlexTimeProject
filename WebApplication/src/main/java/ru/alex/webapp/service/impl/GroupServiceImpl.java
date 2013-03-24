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
import ru.alex.webapp.service.GroupService;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class GroupServiceImpl extends GenericServiceImpl<Group, Long> implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    @Autowired
    GroupDao groupDao;

    @Override
    protected GenericDao<Group, Long> getDao() {
        return groupDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void edit(Group entity) throws Exception {
        //TODO implement Method
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(Group entity) throws Exception {
        //TODO implement Method
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(Group entity) throws Exception {
        //TODO implement Method
    }
}
