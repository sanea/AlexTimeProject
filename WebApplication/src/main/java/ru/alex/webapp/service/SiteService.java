package ru.alex.webapp.service;

import ru.alex.webapp.model.Site;

/**
 * @author Alexander.Isaenco
 */
public interface SiteService extends GenericService<Site, Long> {
    boolean isSiteDeletable(Site site) throws Exception;
}
