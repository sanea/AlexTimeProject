package ru.alex.webapp.beans;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.UserTaskWrapper;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.enums.Action;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.service.UserService;
import ru.alex.webapp.service.UserSiteTaskService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class TaskMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TaskMB.class);
    private static final int UPDATE_INTERVAL_SEC = 1;
    @Autowired
    private UserSiteTaskService userSiteTaskService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionMB sessionMB;
    private List<Site> siteList;
    private Site selectedSite;
    private boolean finishChangeDisable;
    private List<UserTaskWrapper> assignedTasks;
    private boolean startTableUpdater;
    private UserTaskWrapper selectedTask;
    private Action selectedAction;
    private int selectedMinutes;
    private BigDecimal selectedPrice;
    private boolean showExtend;

    @PostConstruct
    private void init() {
        initSites();
        initAssignedTasks();
    }

    private void initSites() {
        logger.debug("initSites");
        try {
            siteList = siteService.getNotDeletedSites(sessionMB.getCurrentUser());
            logger.debug("initSites siteList={}", siteList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of sites", e.toString()));
        }
    }

    private void initAssignedTasks() {
        logger.debug("initAssignedTasks");
        if (sessionMB.getSelectedSite() != null) {
            try {
                startTableUpdater = false;
                finishChangeDisable = false;
                List<UserSiteTask> userSiteTaskList = userSiteTaskService.getNotDeletedUserSiteTasks(sessionMB.getSelectedSite(), sessionMB.getCurrentUser());
                logger.debug("initAssignedTasks userSiteTaskList={}", userSiteTaskList);
                assignedTasks = new ArrayList<>(userSiteTaskList.size());
                for (UserSiteTask ut : userSiteTaskList) {
                    assignedTasks.add(new UserTaskWrapper(ut, sessionMB.getResourceBundle()));
                    if (ut.getCurrentTime() != null) {
                        startTableUpdater = true;
                        finishChangeDisable = true;
                    }
                }
                logger.debug("assignedTasks={}", assignedTasks);
                logger.debug("startTableUpdater={}", startTableUpdater);
                RequestContext reqCtx = RequestContext.getCurrentInstance();
                if (startTableUpdater)
                    reqCtx.execute("if (!tableUpdater.isActive()) { tableUpdater.start(); }");
                else
                    reqCtx.execute("tableUpdater.stop();");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of assigned tasks", e.toString()));
            }
        }
    }

    public List<Site> getSiteList() {
        return siteList;
    }

    public Site getSelectedSite() {
        return selectedSite;
    }

    public void setSelectedSite(Site selectedSite) {
        this.selectedSite = selectedSite;
    }

    public boolean isFinishChangeDisable() {
        return finishChangeDisable;
    }

    public List<UserTaskWrapper> getAssignedTasks() {
        return assignedTasks;
    }

    public UserTaskWrapper getSelectedTask() {
        return selectedTask;
    }

    public String getSelectedAction() {
        return selectedAction == null ? null : selectedAction.getActionFormatted(sessionMB.getResourceBundle());
    }

    public int getSelectedMinutes() {
        return selectedMinutes;
    }

    public void setSelectedMinutes(int selectedMinutes) {
        this.selectedMinutes = selectedMinutes;
    }

    public BigDecimal getSelectedPrice() {
        return selectedPrice;
    }

    public void setSelectedPrice(BigDecimal selectedPrice) {
        this.selectedPrice = selectedPrice;
    }

    public boolean isShowExtend() {
        return showExtend;
    }

    public int getUpdateIntervalSec() {
        return UPDATE_INTERVAL_SEC;
    }

    public boolean isStartTableUpdater() {
        return startTableUpdater;
    }

    public void onSiteRowSelect(SelectEvent event) {
        logger.debug("onSiteRowSelect site={}", selectedSite);
        try {
            User mergedUser = userService.startChange(sessionMB.getCurrentUser(), selectedSite);
            logger.debug("onSiteRowSelect mergedUser={}", mergedUser);
            sessionMB.setCurrentUser(mergedUser);
            sessionMB.setSelectedSite(selectedSite);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in starting change", e.toString()));
        }
        initAssignedTasks();
    }

    public void finishChange(ActionEvent event) {
        logger.debug("finishChange");
        try {
            User mergedUser = userService.finishChange(sessionMB.getCurrentUser());
            logger.debug("finishChange mergedUser={}", mergedUser);
            sessionMB.setCurrentUser(mergedUser);
            sessionMB.setSelectedSite(null);
            assignedTasks = null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in starting change", e.toString()));
        }
        initSites();
    }

    public void startListener(ActionEvent event) {
        selectedMinutes = 30;
        selectedTask = (UserTaskWrapper) event.getComponent().getAttributes().get("task");
        Object actionAttr = event.getComponent().getAttributes().get("action");
        if (actionAttr != null)
            selectedAction = Action.getAction((String) actionAttr);
        Object showExtendAttr = event.getComponent().getAttributes().get("showExtend");
        showExtend = showExtendAttr != null && ((Boolean) showExtendAttr).booleanValue();
        logger.debug("startListener selectedTask={}, selectedAction={}", selectedTask, selectedAction);
    }

    public void priceListener(ActionEvent event) {
        selectedPrice = new BigDecimal(0);
        selectedTask = (UserTaskWrapper) event.getComponent().getAttributes().get("task");
        logger.debug("priceListener selectedTask={}", selectedTask);
    }

    public void startTask(UserTaskWrapper task) {
        logger.debug("startTask {}", task);
        try {
            userSiteTaskService.startTask(task.getUserSiteTask(), null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting task", e.toString()));
        }
        initAssignedTasks();
    }

    public void startCustomPriceTask() {
        logger.debug("startTask {}, {}", selectedTask, selectedPrice);
        if (selectedPrice.compareTo(new BigDecimal(0)) < 0) {
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting tasl", "custom price should be >= 0"));
            return;
        }
        try {
            userSiteTaskService.startTask(selectedTask.getUserSiteTask(), selectedPrice);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting Custom Price task", e.toString()));
        }
        initAssignedTasks();
    }

    public void startProcess() {
        logger.debug("startProcess selectedTask={}, selectedMinutes={}, selectedAction={}", selectedTask, selectedMinutes, selectedAction);
        if (selectedMinutes <= 0) {
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting process", "minutes should be > 0"));
            return;
        }
        try {
            TaskStatus taskStatus = TaskStatus.getStatus(selectedTask.getCurrentStatus());
            if (taskStatus != TaskStatus.RUNNING)
                userSiteTaskService.startProcess(selectedTask.getUserSiteTask(), selectedMinutes * 60, selectedAction);
            else
                userSiteTaskService.switchProcess(selectedTask.getUserSiteTask(), selectedMinutes * 60, selectedAction);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting process", e.toString()));
        }
        initAssignedTasks();
    }

    public void resumeProcess(UserTaskWrapper task) {
        logger.debug("startProcess selectedTask={}", task);
        try {
            userSiteTaskService.resumeProcess(task.getUserSiteTask());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting process", e.toString()));
        }
        initAssignedTasks();
    }

    public void extendProcess() {
        logger.debug("extendProcess selectedTask={}, selectedMinutes={}, selectedAction={}", selectedTask, selectedMinutes, selectedAction);
        try {
            userSiteTaskService.extendProcess(selectedTask.getUserSiteTask(), selectedMinutes * 60);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error extending task", e.toString()));
        }
        initAssignedTasks();
    }

    public void stop(UserTaskWrapper task) {
        logger.debug("stop {}", task);
        try {
            userSiteTaskService.stopProcess(task.getUserSiteTask());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error stoping task", e.toString()));
        }
        initAssignedTasks();
    }

    public void refreshTable() {
        boolean needInit = false;
        for (UserTaskWrapper task : assignedTasks) {
            TaskStatus taskStatus = TaskStatus.getStatus(task.getCurrentStatus());
            if (taskStatus == TaskStatus.RUNNING || taskStatus == TaskStatus.CUSTOM1 || taskStatus == TaskStatus.CUSTOM2 || taskStatus == TaskStatus.CUSTOM3) {
                int timeLeft = task.getTimeLeftSec() - UPDATE_INTERVAL_SEC;
                if (timeLeft <= 0) {
                    needInit = true;
                    break;
                }
            }
        }
        if (needInit)
            initAssignedTasks();
    }

}

