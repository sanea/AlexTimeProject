package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.TaskWrapper;
import ru.alex.webapp.model.UserTask;
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
    private Long selectedTaskId;

    @PostConstruct
    private void init() {
        userName = SecurityContextHolder.getContext().getAuthentication().getName();
        initAssignedTasks();
    }

    private void initAssignedTasks() {
        List<UserTask> tasks = taskService.getTasksForUser(userName);
        List<TaskWrapper> taskWrappers = new ArrayList<TaskWrapper>(tasks.size());
        for (UserTask ut : tasks) {
            taskWrappers.add(new TaskWrapper(ut));
        }
        assignedTasks = taskWrappers;
    }

    public List<TaskWrapper> getAssignedTasks() {
        return assignedTasks;
    }

    public void start() {
        try {
            taskService.startTask(selectedTaskId, userName, selectedMinutes * 60);
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error starting task", e.toString()));
        }
    }

    public void pause(TaskWrapper task) {
        try {
            taskService.pauseTask(task.getTaskId(), userName);
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error pausing task", e.toString()));
        }
    }

    public void resume(TaskWrapper task) {
        try {
            taskService.resumeTask(task.getTaskId(), userName);
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error resuming task", e.toString()));
        }
    }

    public void extend(TaskWrapper task, int minutes) {
        try {
            taskService.extendTask(task.getTaskId(), userName, minutes * 60);
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error extending task", e.toString()));
        }
    }

    public void finish(TaskWrapper task) {
        try {
            taskService.stopTask(task.getTaskId(), userName);
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

    public void startListener(ActionEvent event) {
        selectedMinutes = 30;
        selectedTaskId = (Long) event.getComponent().getAttributes().get("taskId");
    }

}

