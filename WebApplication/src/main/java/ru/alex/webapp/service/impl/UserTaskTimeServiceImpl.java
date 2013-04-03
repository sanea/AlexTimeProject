package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.UserTaskTimeService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;
import java.util.List;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserTaskTimeServiceImpl extends GenericServiceImpl<UserTaskTime, Long> implements UserTaskTimeService {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    @Autowired
    UserTaskTimeDao userTaskTimeDao;

    @Override
    protected GenericDao<UserTaskTime, Long> getDao() {
        return userTaskTimeDao;
    }

    @Override
    public void update(UserTaskTime entity) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public void add(UserTaskTime entity) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public void remove(UserTaskTime entity) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<UserTaskTime> getAll(Site site, User user, Task task, TaskType taskType, Date from, Date to) throws Exception {
        logger.debug("getAll site={}, user={}, task={}, taskType={}, from={}, to={}", site, user, task, taskType, from, to);
        if (site == null && user == null && task == null && taskType == null && from == null && to == null)
            return userTaskTimeDao.findAll();
        if ((site != null && site.getId() == null) || (user != null && user.getUsername() == null)
                || (task != null && task.getId() == null))
            throw new IllegalArgumentException("Wrong input params");
        return userTaskTimeDao.getAll(site, user, task, taskType, from, to);
    }
}
