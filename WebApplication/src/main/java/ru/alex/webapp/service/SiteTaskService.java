package ru.alex.webapp.service;

import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.SiteTask;
import ru.alex.webapp.model.Task;

import java.util.List;

/**
 * @author Alexander.Isaenco
 */
public interface SiteTaskService extends GenericService<SiteTask, Long> {
    List<SiteTask> getNotDeletedSiteTasks(Site site) throws Exception;

    SiteTask getBySiteTask(Site site, Task task) throws Exception;

    void addSiteTask(Site site, Task task) throws Exception;

    void removeSiteTask(Site site, Task task) throws Exception;
}
