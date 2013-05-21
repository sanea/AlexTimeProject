package ru.alex.webapp.service;


import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.User;

import java.util.List;

public interface UserService extends GenericService<User, String> {
    static final String USERNAME_REGEXP = "[0-9A-Za-z_\\-]+";

    boolean isUserDeletable(User user) throws Exception;

    List<User> getEnabledNotDeletedUsers() throws Exception;

    User startChange(User user, Site site) throws Exception;

    User finishChange(User user) throws Exception;
}