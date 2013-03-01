package ru.alex.webapp.service;


import ru.alex.webapp.model.User;

import java.util.List;

public interface UserService {
    public User getUser(String username) throws Exception;

    public List<User> getAllEnabledUsers() throws Exception;

    public List<User> getAllUsers() throws Exception;

    public void addUser(User user) throws Exception;

    public void updateUser(User user) throws Exception;

    public void deleteUser(User user) throws Exception;

    public boolean isDeletableUser(User user) throws Exception;
}