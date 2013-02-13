package ru.alex.webapp.service;


import ru.alex.webapp.model.Users;

import java.util.List;

public interface UserService {
    public Users getUser(String username);

    public List<Users> getAllUsers();

//    public Users authenticate(String username, String password);

    public void addUser(Users user);

    public void saveUser(Users user);

    public void deleteUser(Users user);
}