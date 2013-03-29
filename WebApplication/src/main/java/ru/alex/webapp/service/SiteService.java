package ru.alex.webapp.service;

import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.User;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface SiteService extends GenericService<Site, Long> {
    boolean isSiteDeletable(Site site) throws Exception;
    List<Site> getNotDeletedSites() throws Exception;
    List<Site> getNotDeletedSites(User user) throws Exception;
}
