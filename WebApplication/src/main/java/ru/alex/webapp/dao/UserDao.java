package ru.alex.webapp.dao;

import ru.alex.webapp.model.UsersEntity;

import java.util.List;

/**
 * User: Alexander.Isaenco
 * Date: 05.02.13
 * Time: 19:44
 */
public interface UserDao {
    void addUser(UsersEntity user);

    void updateUser(UsersEntity user);

    UsersEntity getUserByUsername(String username);

    void deleteUser(String username);

    List<UsersEntity> findAllUsers();
}
