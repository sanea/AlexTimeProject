package ru.alex.webapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.model.User;
import ru.alex.webapp.service.UserService;

import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(String username) {
//        return userDao.getUserByUsername(username);
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

//    @Override
//    public User authenticate(String username, String password) {
//        User ue = getUser(username);
//        if (ue != null && ue.isEnabled() && ue.getPassword().equals(password))
//            return ue;
//        else
//            return null;
//    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void addUser(User user) {
//        userDao.addUser(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveUser(User user) {
//        userDao.updateUser(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void deleteUser(User user) {
//        userDao.deleteUser(user.getUsername());
    }
}