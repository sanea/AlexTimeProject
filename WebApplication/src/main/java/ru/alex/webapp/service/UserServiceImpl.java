package ru.alex.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.model.UsersEntity;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public UsersEntity getUser(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public List<UsersEntity> getAllUsers() {
        return userDao.findAllUsers();
    }

//    @Override
//    public UsersEntity authenticate(String username, String password) {
//        UsersEntity ue = getUser(username);
//        if (ue != null && ue.isEnabled() && ue.getPassword().equals(password))
//            return ue;
//        else
//            return null;
//    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void addUser(UsersEntity user) {
        userDao.addUser(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveUser(UsersEntity user) {
        userDao.updateUser(user);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void deleteUser(UsersEntity user) {
        userDao.deleteUser(user.getUsername());
    }
}