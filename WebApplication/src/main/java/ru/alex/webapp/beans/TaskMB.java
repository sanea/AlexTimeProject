package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.TaskWrapper;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;

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
    private static final Logger logger = Logger.getLogger(UserMB.class);
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    private String userName;
    private List<TaskWrapper> assignedTasks;
    private int selectedMinutes;
    private TaskWrapper selectedTask;

    @PostConstruct
    private void init() {
        userName = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("init username=" + userName);
        initAssignedTasks();
    }

    private void initAssignedTasks() {
        logger.debug("initAssignedTasks");
        try {
            List<UserTask> tasks = taskService.getTasksForUser(userName);
            logger.debug("tasks=" + tasks);
            List<TaskWrapper> taskWrappers = new ArrayList<TaskWrapper>(tasks.size());
            for (UserTask ut : tasks) {
                UserTaskTime currentTime = taskService.getCurrentTimeForUser(ut.getId(), userName);
                logger.debug("currentTime=" + currentTime);
                taskWrappers.add(new TaskWrapper(ut, currentTime));
            }
            assignedTasks = taskWrappers;
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting task", e.toString()));
        }
    }

    public List<TaskWrapper> getAssignedTasks() {
        return assignedTasks;
    }

    public void startProcess() {
        logger.debug("startProcess " + selectedTask + " " + userName + " " + selectedMinutes);
        try {
            taskService.startTask(selectedTask.getTaskId(), userName, selectedMinutes * 60);
            initAssignedTasks();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting task", e.toString()));
        }
    }

    public void startTask(String taskId) {
        logger.debug("startTask " + taskId);
        try {
            taskService.startTask(Long.valueOf(taskId), userName, 0);
            initAssignedTasks();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting task", e.toString()));
        }
    }

    public void pause(String taskId) {
        logger.debug("pause " + taskId);
        try {
            taskService.pauseTask(Long.valueOf(taskId), userName);
            initAssignedTasks();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error pausing task", e.toString()));
        }
    }

    public void resume(String taskId) {
        logger.debug("resume " + taskId);
        try {
            taskService.resumeTask(Long.valueOf(taskId), userName);
            initAssignedTasks();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error resuming task", e.toString()));
        }
    }

    public void extendProcess() {
        logger.debug("extendProcess " + selectedTask + " " + userName + " " + selectedMinutes);
        try {
            taskService.extendTask(selectedTask.getTaskId(), userName, selectedMinutes * 60);
            initAssignedTasks();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error extending task", e.toString()));
        }
    }

    public void stop(String taskId) {
        logger.debug("stop " + taskId);
        try {
            taskService.stopTask(Long.valueOf(taskId), userName);
            initAssignedTasks();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error stoping task", e.toString()));
        }
    }

    public int getSelectedMinutes() {
        return selectedMinutes;
    }

    public void setSelectedMinutes(int selectedMinutes) {
        this.selectedMinutes = selectedMinutes;
    }

    public TaskWrapper getSelectedTask() {
        return selectedTask;
    }

    public void startListener(ActionEvent event) {
        selectedMinutes = 30;
        selectedTask = (TaskWrapper) event.getComponent().getAttributes().get("task");
        logger.debug("startListener selectedTask=" + selectedTask);
    }

}

