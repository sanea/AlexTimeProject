package ru.alex.webapp.service;


import ru.alex.webapp.model.User;

import java.io.Serializable;
import java.util.List;

public interface UserService extends Serializable {
    public User getUser(String username);

    public List<User> getAllUsers();

//    public User authenticate(String username, String password);

    public void addUser(User user);

    public void saveUser(User user);

    public void deleteUser(User user);
}