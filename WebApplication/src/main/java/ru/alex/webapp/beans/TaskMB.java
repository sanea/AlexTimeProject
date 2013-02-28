package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.UserTaskWrapper;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.service.TaskService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private static final Logger logger = Logger.getLogger(TaskMB.class);
    @Autowired
    private TaskService taskService;
    private String userName;
    private List<UserTaskWrapper> assignedTasks;
    private int selectedMinutes;
    private UserTaskWrapper selectedTask;
    private boolean startTableUpdater;
    private int updateIntervalSec = 1;

    @PostConstruct
    private void init() {
        userName = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("init username=" + userName);
        initAssignedTasks();
    }

    private void initAssignedTasks() {
        logger.debug("initAssignedTasks");
        try {
            startTableUpdater = false;
            List<UserTask> tasks = taskService.getTasksForUser(userName);
            logger.debug("tasks=" + tasks);
            List<UserTaskWrapper> taskWrappers = new ArrayList<UserTaskWrapper>(tasks.size());
            for (UserTask ut : tasks) {
                UserTaskTime currentTime = taskService.getCurrentTimeForUserTask(ut.getTaskByTaskId().getId(), userName);
                logger.debug("currentTime=" + currentTime);
                int timeSpentSec = taskService.getTimeSpentSecForUserTask(ut.getTaskByTaskId().getId(), userName);
                logger.debug("timeSpentSec=" + timeSpentSec);
                taskWrappers.add(new UserTaskWrapper(ut, currentTime, timeSpentSec));
                if (TaskStatus.getStatus(ut.getStatus()) == TaskStatus.RUNNING)
                    startTableUpdater = true;
            }
            assignedTasks = taskWrappers;
            logger.debug("startTableUpdater=" + startTableUpdater);
            RequestContext reqCtx = RequestContext.getCurrentInstance();
            if (startTableUpdater)
                reqCtx.execute("if (!tableUpdater.isActive()) { tableUpdater.start(); }");
            else
                reqCtx.execute("tableUpdater.stop();");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    public List<UserTaskWrapper> getAssignedTasks() {
        return assignedTasks;
    }

    public void startProcess() {
        logger.debug("startProcess " + selectedTask + " " + userName + " " + selectedMinutes);
        if (selectedMinutes <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting process", "minutes should be > 0"));
            return;
        }
        try {
            taskService.startTask(selectedTask.getTaskId(), userName, selectedMinutes * 60);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting process", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void startTask(String taskId) {
        logger.debug("startTask " + taskId);
        try {
            taskService.startTask(Long.valueOf(taskId), userName, 0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void pause(String taskId) {
        logger.debug("pause " + taskId);
        try {
            taskService.pauseTask(Long.valueOf(taskId), userName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error pausing task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void resume(String taskId) {
        logger.debug("resume " + taskId);
        try {
            taskService.resumeTask(Long.valueOf(taskId), userName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error resuming task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void extendProcess() {
        logger.debug("extendProcess " + selectedTask + " " + userName + " " + selectedMinutes);
        try {
            taskService.extendTask(selectedTask.getTaskId(), userName, selectedMinutes * 60);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error extending task", e.toString()));
        } finally {
            initAssignedTasks();
        }
    }

    public void stop(String taskId) {
        logger.debug("stop " + taskId);
        try {
            taskService.stopTask(Long.valueOf(taskId), userName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error stoping task", e.toString()));
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
        return updateIntervalSec;
    }

    public void startListener(ActionEvent event) {
        selectedMinutes = 30;
        selectedTask = (UserTaskWrapper) event.getComponent().getAttributes().get("task");
        logger.debug("startListener selectedTask=" + selectedTask);
    }

    public void refreshTable() {
        boolean needInit = false;
        for (UserTaskWrapper task : assignedTasks) {
            if (TaskStatus.getStatus(task.getCurrentStatus()) == TaskStatus.RUNNING) {
                int timeLeft = task.getTimeLeftSec() - updateIntervalSec;
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

