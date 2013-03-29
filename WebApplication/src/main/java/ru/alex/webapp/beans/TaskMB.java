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
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
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
    private TaskService taskService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionMB sessionMB;

    private List<Site> siteList;
    private Site selectedSite;
    private boolean finishChangeDisable;

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

    public void onSiteRowSelect(SelectEvent event) {
        logger.debug("onSiteRowSelect site={}", selectedSite);
        try {
            User mergedUser = userService.startChange(sessionMB.getCurrentUser());
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


    //TODO----------------
    private List<UserTaskWrapper> assignedTasks;
    private int selectedMinutes;
    private UserTaskWrapper selectedTask;
    private boolean startTableUpdater;

    private void initAssignedTasks() {
        logger.debug("initAssignedTasks");
        if (sessionMB.getSelectedSite() != null) {
            try {
                startTableUpdater = false;
                List<UserSiteTask> tasks = taskService.getTasksForUser(sessionMB.getCurrentUser().getUsername());
                logger.debug("tasks={}", tasks);
                List<UserTaskWrapper> taskWrappers = new ArrayList<>(tasks.size());
                for (UserSiteTask ut : tasks) {
                    UserTaskTime currentTime = taskService.getCurrentTimeForUserTask(ut.getSiteTask().getTaskByTaskId().getId(), sessionMB.getCurrentUser().getUsername());
                    logger.debug("currentTime={}", currentTime);
                    int timeSpentSec = taskService.getTimeSpentSecForUserTask(ut.getSiteTask().getTaskByTaskId().getId(), sessionMB.getCurrentUser().getUsername());
                    logger.debug("timeSpentSec={}", timeSpentSec);
                    taskWrappers.add(new UserTaskWrapper(ut, currentTime, timeSpentSec));
                    if (TaskStatus.getStatus(ut.getStatus()) == TaskStatus.RUNNING)
                        startTableUpdater = true;
                }
                assignedTasks = taskWrappers;
                logger.debug("startTableUpdater={}", startTableUpdater);
                RequestContext reqCtx = RequestContext.getCurrentInstance();
                if (startTableUpdater)
                    reqCtx.execute("if (!tableUpdater.isActive()) { tableUpdater.start(); }");
                else
                    reqCtx.execute("tableUpdater.stop();");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
            }
        }
    }

    public List<UserTaskWrapper> getAssignedTasks() {
        return assignedTasks;
    }

    public void startProcess() {
        logger.debug("startProcess selectedTask={}, sessionMB.getCurrentUser().getUsername()={}, selectedMinutes={}", selectedTask, sessionMB.getCurrentUser().getUsername(), selectedMinutes);
        if (selectedMinutes <= 0) {
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting process", "minutes should be > 0"));
            return;
        }
        try {
            taskService.startTask(selectedTask.getTaskId(), sessionMB.getCurrentUser().getUsername(), selectedMinutes * 60);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting process", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void startTask(String taskId) {
        logger.debug("startTask {}", taskId);
        try {
            taskService.startTask(Long.valueOf(taskId), sessionMB.getCurrentUser().getUsername(), 0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void pause(String taskId) {
        logger.debug("pause {}", taskId);
        try {
            taskService.pauseTask(Long.valueOf(taskId), sessionMB.getCurrentUser().getUsername());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error pausing task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void resume(String taskId) {
        logger.debug("resume {}", taskId);
        try {
            taskService.resumeTask(Long.valueOf(taskId), sessionMB.getCurrentUser().getUsername());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error resuming task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void extendProcess() {
        logger.debug("extendProcess selectedTask={}, sessionMB.getCurrentUser().getUsername()={}, selectedMinutes={}", selectedTask, sessionMB.getCurrentUser().getUsername(), selectedMinutes);
        try {
            taskService.extendTask(selectedTask.getTaskId(), sessionMB.getCurrentUser().getUsername(), selectedMinutes * 60);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error extending task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void stop(String taskId) {
        logger.debug("stop {}", taskId);
        try {
            taskService.stopTask(Long.valueOf(taskId), sessionMB.getCurrentUser().getUsername());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error stoping task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public int getSelectedMinutes() {
        return selectedMinutes;
    }

    public void setSelectedMinutes(int selectedMinutes) {
        this.selectedMinutes = selectedMinutes;
    }

    public UserTaskWrapper getSelectedTask() {
        return selectedTask;
    }

    public boolean getStartTableUpdater() {
        return startTableUpdater;
    }

    public int getUpdateIntervalSec() {
        return UPDATE_INTERVAL_SEC;
    }

    public void startListener(ActionEvent event) {
        selectedMinutes = 30;
        selectedTask = (UserTaskWrapper) event.getComponent().getAttributes().get("task");
        logger.debug("startListener selectedTask={}", selectedTask);
    }

    public void refreshTable() {
        boolean needInit = false;
        for (UserTaskWrapper task : assignedTasks) {
            if (TaskStatus.getStatus(task.getCurrentStatus()) == TaskStatus.RUNNING) {
                int timeLeft = task.getTimeLeftSec() - UPDATE_INTERVAL_SEC;
                if (timeLeft > 0)
                    task.setTimeLeftSec(timeLeft);
                else
                    needInit = true;
            }
        }
        if (needInit)
            initAssignedTasks();
    }

}

