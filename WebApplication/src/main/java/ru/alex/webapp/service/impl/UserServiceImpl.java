package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.dao.UserSiteTaskDao;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserServiceImpl extends GenericServiceImpl<User, String> implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserSiteTaskDao userSiteTaskDao;

    @Override
    protected GenericDao<User, String> getDao() {
        return userDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(User user) throws Exception {
        logger.debug("addUser user={}", user);
        if (user == null || user.getUsername() == null || user.getUsername().equals("")
                || user.getPassword() == null || user.getPassword().equals(""))
            throw new IllegalArgumentException("Wrong user");
        user.setDeleted(false);
        userDao.persist(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(User user) throws Exception {
        logger.debug("update user={}", user);
        if (user == null || user.getUsername() == null || user.getUsername().equals("")
                || user.getPassword() == null || user.getPassword().equals(""))
            throw new IllegalArgumentException("Wrong user");
        User userEntity = userDao.findById(user.getUsername());
        if (userEntity == null)
            throw new Exception("Can't find for update user " + user.getUsername());
        User mergedUser = userDao.merge(user);
        logger.debug("Updated user: {}", mergedUser);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(User user) throws Exception {
        logger.debug("deleteUser user={}", user);
        if (user == null || user.getUsername() == null || user.getUsername().equals(""))
            throw new IllegalArgumentException("Wrong user");
        throwExceptionIfNotExists(user.getUsername());
        if (!isUserDeletable(user))
            throw new Exception("User has active user change, please wait or close user session.");

        Map<String, Object> params = new HashMap<>(1);
        params.put("username", user.getUsername());
        Collection<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.BY_USERNAME, params);
        logger.debug("remove BY_USERNAME userSiteTaskList={}", userSiteTaskList);
        if (userSiteTaskList == null || userSiteTaskList.size() == 0) {
            user = userDao.merge(user);
            userDao.remove(user);
        } else {
            user.setDeleted(true);
            user.setEnabled(false);
            userDao.merge(user);
        }
    }

    /**
     * Returns true if user is not deleted and
     * task has no current change (if change is set, that means that the user is online)
     *
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public boolean isUserDeletable(User user) throws Exception {
        logger.debug("isUserDeletable user={}", user);
        if (user == null || user.getUsername() == null || user.getUsername().equals(""))
            throw new IllegalArgumentException("Wrong user");
        throwExceptionIfNotExists(user.getUsername());
        boolean result;
        if (user.isDeleted()) {
            result = false;
        } else {
            User userEntity = userDao.findById(user.getUsername());
            if (userEntity.getCurrentChange() != null)
                result = false;
            else
                result = true;
        }
        logger.debug("isUserDeletable {}", result);
        return result;
    }

    @Override
    public List<User> getEnabledNotDeletedUsers() throws Exception {
        List<User> userList = userDao.findWithNamedQuery(User.ALL_ENABLED_NOT_DELETED);
        logger.debug("getEnabledNotDeletedUsers userList={}", userList);
        return userList;
    }
}