package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.model.User;
import ru.alex.webapp.service.UserService;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserServiceImpl extends GenericServiceImpl<User, String> implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

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
        userDao.persist(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void edit(User user) throws Exception {
        logger.debug("edit user={}", user);
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
        User userEntity = userDao.findById(user.getUsername());
        if (userEntity == null)
            throw new Exception("Can't find user for delete" + user.getUsername());
        if (!isUserDeletable(userEntity))
            throw new Exception("User has active user change, please wait or close user session.");
        userEntity.setDeleted(true);
        userEntity.setEnabled(false);
        userDao.merge(userEntity);
    }

    @Override
    public boolean isUserDeletable(User user) throws Exception {
        logger.debug("isUserDeletable user={}", user);
        if (user == null || user.getUsername() == null || user.getUsername().equals(""))
            throw new IllegalArgumentException("Wrong user");
        User userEntity = userDao.findById(user.getUsername());
        logger.debug("isUserDeletable userEntity={}", userEntity);
        if (userEntity == null)
            throw new Exception("isUserDeletable can't find user " + user.getUsername());
        boolean result;
        if (userEntity.isDeleted()) {
            result = false;
        } else {
            if (userEntity.getCurrentChange() != null)
                result = false;
            else
                result = true;
        }
        logger.debug("isUserDeletable {}", result);
        return result;
    }
}