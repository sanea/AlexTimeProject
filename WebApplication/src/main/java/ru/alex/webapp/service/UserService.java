package ru.alex.webapp.service;


import ru.alex.webapp.model.UsersEntity;

import java.util.List;

public interface UserService {
    public UsersEntity getUser(String username);

    public List<UsersEntity> getAllUsers();

//    public UsersEntity authenticate(String username, String password);

    public void addUser(UsersEntity user);

    public void saveUser(UsersEntity user);

    public void deleteUser(UsersEntity user);
}