package ru.alex.webapp.beans;

import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class SiteEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SiteEditMB.class);
    @Autowired
    private SiteService siteService;
    private List<Site> siteList;
    private Map<Long, Boolean> siteDeletable;
    private Site newSite = new Site();
    private Site selectedSite;

    @PostConstruct
    private void init() {
        initSites();
    }

    private void initSites() {
        try {
            siteList = siteService.findAll();
            logger.debug("initSites siteList={}", siteList);
            siteDeletable = new HashMap(siteList.size());
            for (Site s : siteList) {
                boolean deletable = siteService.isSiteDeletable(s);
                siteDeletable.put(s.getId(), deletable);
            }
            logger.debug("initSites siteEditable={}", siteDeletable);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of sites", e.toString()));
        }
    }

    public List<Site> getSiteList() {
        return siteList;
    }

    public Map<Long, Boolean> getSiteDeletable() {
        return siteDeletable;
    }

    public Site getNewSite() {
        return newSite;
    }

    public void setNewSite(Site newSite) {
        this.newSite = newSite;
    }

    public Site getSelectedSite() {
        return selectedSite;
    }

    public void setSelectedSite(Site selectedSite) {
        this.selectedSite = selectedSite;
    }

    public void onEdit(RowEditEvent event) {
        Site site = (Site) event.getObject();
        logger.debug("onEdit site={}", site);
        try {
            siteService.update(site);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Site Edited", site.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing site", e.toString()));
        }
        initSites();
    }

    public void onCancel(RowEditEvent event) {
        Site site = (Site) event.getObject();
        logger.debug("onCancel site={}", site);
    }

    public void addNewSiteListener(ActionEvent event) {
        logger.debug("addNewSiteListener");
        newSite = new Site();
        newSite.setDeleted(false);
    }

    public void addNewSite() {
        logger.debug("addNewSite newSite={}", newSite);
        try {
            siteService.add(newSite);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Site Added", newSite.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in adding site", e.toString()));
        }
        initSites();
    }

    public void removeSite() {
        logger.debug("removeSite selectedSite={}", selectedSite);
        try {
            siteService.remove(selectedSite);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Site Removed", selectedSite.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing site", e.toString()));
        }
        initSites();
    }

}

