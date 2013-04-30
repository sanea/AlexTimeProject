package ru.alex.webapp.service;

import ru.alex.webapp.model.UserAdmin;
import ru.alex.webapp.model.UserAdminSite;
import ru.alex.webapp.model.UserAdminSuper;
import ru.alex.webapp.model.UserAdminTask;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface UserAdminService extends GenericService<UserAdmin, Long> {
    List<UserAdminSuper> findAllSuper();

    List<UserAdminSite> findAllSite();

    List<UserAdminTask> findAllTask();
}
