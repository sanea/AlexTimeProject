package ru.alex.webapp.beans;

import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.TaskType;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class TaskEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TaskEditMB.class);
    List<Task> taskList;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    private String userName;
    private Task newTask = new Task();

    @PostConstruct
    private void init() {
        userName = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("init username=" + userName);
        try{
            taskList = taskService.getAllTasks();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public TaskType[] getTaskTypes() {
        return TaskType.values();
    }

    public Task getNewTask() {
        return newTask;
    }

    public String getTaskTypeFormated(String taskType) {
        return TaskType.getTypeFormatted(taskType);
    }

    public void onEdit(RowEditEvent event) {
        Task task = (Task) event.getObject();
        logger.debug("onEdit task=" + task);
        FacesMessage msg = new FacesMessage("Task Edited", task.getTaskName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
        Task task = (Task) event.getObject();
        logger.debug("onCancel task=" + task);
        FacesMessage msg = new FacesMessage("Task Canceled", task.getTaskName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void addNewTask() {
        logger.debug("addNewTask newTask=" + newTask);
    }

    public void addNewTaskListener(ActionEvent event) {
        logger.debug("addNewTaskListener");
        newTask = new Task();
    }

}

