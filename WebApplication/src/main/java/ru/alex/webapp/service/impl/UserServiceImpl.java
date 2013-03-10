package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.dao.UserTaskDao;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.service.UserService;

import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserTaskDao userTaskDao;

    @Override
    public User getUser(String username) throws Exception {
        logger.debug("getUser username={}", username);
        if (username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong username: " + username);
        User user = userDao.findById(username);
        logger.debug("getUser user={}", user);
        return user;
    }

    @Override
    public List<User> getAllEnabledUsers() throws Exception {
        List<User> users = userDao.getEnabledUsers();
        logger.debug("getAllEnabledUsers users={}", users);
        return users;
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        List<User> users = userDao.findAll();
        logger.debug("getAllUsers users={}", users);
        return users;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void addUser(User user) throws Exception {
        logger.debug("addUser user={}", user);
        if (user == null || user.getUsername() == null || user.getUsername().equals("")
                || user.getPassword() == null || user.getPassword().equals(""))
            throw new IllegalArgumentException("Wrong user");
        userDao.persist(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void updateUser(User user) throws Exception {
        logger.debug("updateUser user={}", user);
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
    public void deleteUser(User user) throws Exception {
        logger.debug("deleteUser user={}", user);
        if (user == null || user.getUsername() == null || user.getUsername().equals(""))
            throw new IllegalArgumentException("Wrong user");
        User userEntity = userDao.findById(user.getUsername());
        if (userEntity == null)
            throw new Exception("Can't find for delete user " + user.getUsername());
        boolean isDeletable = isDeletableUser(userEntity);
        logger.debug("deleteUser isDeletable={}", isDeletable);
        if (isDeletable)
            userDao.remove(userEntity);
        else
            throw new Exception("Can't delete user " + user.getUsername() + " (only disable)");
    }

    @Override
    public boolean isDeletableUser(User user) throws Exception {
        logger.debug("isDeletableUser user={}", user);
        if (user == null || user.getUsername() == null || user.getUsername().equals(""))
            throw new IllegalArgumentException("Wrong user");
        List<UserTask> userTaskList = userTaskDao.getTasksAllForUser(user.getUsername());
        logger.debug("isDeletableUser userTaskList={}", userTaskList);
        boolean result;
        if (userTaskList != null || userTaskList.size() > 0)
            result = false;
        else
            result = true;
        logger.debug("isDeletableUser {}", result);
        return result;
    }
}