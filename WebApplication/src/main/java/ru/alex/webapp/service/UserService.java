package ru.alex.webapp.service;


import ru.alex.webapp.model.User;

public interface UserService extends GenericService<User, String> {
    public boolean isUserDeletable(User user) throws Exception;
}