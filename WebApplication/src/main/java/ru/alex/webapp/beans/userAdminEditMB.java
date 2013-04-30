package ru.alex.webapp.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.UserAdminSite;
import ru.alex.webapp.model.UserAdminSuper;
import ru.alex.webapp.model.UserAdminTask;
import ru.alex.webapp.service.UserAdminService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.List;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class UserAdminEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserAdminEditMB.class);

    @Autowired
    private UserAdminService userAdminService;
    @Autowired
    private SessionMB sessionMB;

    private List<UserAdminSuper> userAdminSuperList;
    private List<UserAdminSite> userAdminSiteList;
    private List<UserAdminTask> userAdminTaskList;

    private UserAdminSuper selectedUserAdminSuper;

    @PostConstruct
    private void init() {
        try {
            userAdminSuperList = userAdminService.findAllSuper();
            logger.debug("init userAdminSuperList={}", userAdminSuperList);
            userAdminSiteList = userAdminService.findAllSite();
            logger.debug("init userAdminSiteList={}", userAdminSiteList);
            userAdminTaskList = userAdminService.findAllTask();
            logger.debug("init userAdminTaskList={}", userAdminTaskList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization", e.toString()));
        }
    }

    public List<UserAdminSuper> getUserAdminSuperList() {
        return userAdminSuperList;
    }

    public List<UserAdminSite> getUserAdminSiteList() {
        return userAdminSiteList;
    }

    public List<UserAdminTask> getUserAdminTaskList() {
        return userAdminTaskList;
    }

    public UserAdminSuper getSelectedUserAdminSuper() {
        return selectedUserAdminSuper;
    }

    public void setSelectedUserAdminSuper(UserAdminSuper selectedUserAdminSuper) {
        this.selectedUserAdminSuper = selectedUserAdminSuper;
    }

    public void addNewUserAdminSuperListener(ActionEvent event) {
        logger.debug("addNewUserAdminSuperListener");
        selectedUserAdminSuper = new UserAdminSuper();
    }
}

