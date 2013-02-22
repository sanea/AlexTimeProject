package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.TaskType;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class TaskEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TaskEditMB.class);
    List<Task> taskList;
    Map<Long, Boolean> taskEditable;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    private String userName;
    private Task newTask = new Task();
    private Long selectedTaskId;

    private Map<User, Boolean> taskAssignUserMap;

    @PostConstruct
    private void init() {
        userName = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("init username=" + userName);
        try {
            initTasks();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    private void initTasks() throws Exception {
        taskList = taskService.getAllTasks();
        logger.debug("initTasks taskList=" + taskList);
        taskEditable = new HashMap<Long, Boolean>(taskList.size());
        for (Task t : taskList) {
            boolean editable = taskService.isTaskEditable(t.getId());
            taskEditable.put(t.getId(), editable);
        }
        logger.debug("initTasks taskEditable=" + taskEditable);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public Map<Long, Boolean> getTaskEditable() {
        return taskEditable;
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

    public Map<User, Boolean> getTaskAssignUserMap() {
        return taskAssignUserMap;
    }

    public void onEdit(RowEditEvent event) {
        Task task = (Task) event.getObject();
        logger.debug("onEdit task=" + task);
        try {
            taskService.editTask(task);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Task Edited", task.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }

        try {
            initTasks();
            logger.debug("onEdit taskList=" + taskList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    public void onCancel(RowEditEvent event) {
        Task task = (Task) event.getObject();
        logger.debug("onCancel task=" + task);
    }

    public void addNewTaskListener(ActionEvent event) {
        logger.debug("addNewTaskListener");
        newTask = new Task();
        newTask.setEnabled(true);
    }

    public void addNewTask() {
        logger.debug("addNewTask newTask=" + newTask);
        try {
            taskService.addTask(newTask);
            initTasks();
            logger.debug("addNewTask taskList=" + taskList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Task Added", newTask.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in adding task", e.toString()));
        }
    }

    public void assignListener(ActionEvent event) {
        try {
            selectedTaskId = (Long) event.getComponent().getAttributes().get("taskId");
            logger.debug("assignListener selectedTaskId=" + selectedTaskId);

            List<User> userList = userService.getAllUsers();
            logger.debug("assignListener userList=" + userList);
            taskAssignUserMap = new HashMap<User, Boolean>(userList.size());
            for (User u : userList)
                taskAssignUserMap.put(u, false);

            List<UserTask> userTaskList = taskService.getUsersForTask(selectedTaskId);
            logger.debug("assignListener userTaskList=" + userTaskList);
            for (UserTask ut : userTaskList)
                taskAssignUserMap.put(ut.getUserByUsername(), true);

            logger.debug("assignListener taskAssignUserMap=" + taskAssignUserMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in getting user list for task", e.toString()));
        }
    }

    public void assignTask() {
        logger.debug("assignTask selectedTaskId=" + selectedTaskId);
        //todo
        try {
            //taskService.assignTask(Long.valueOf(selectedTaskId));
            initTasks();
            logger.debug("assignTask taskList=" + taskList);
            selectedTaskId = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Assigned"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void removeListener(ActionEvent event) {
        selectedTaskId = (Long) event.getComponent().getAttributes().get("taskId");
        logger.debug("removeListener selectedTaskId=" + selectedTaskId);
    }

    public void removeTask() {
        logger.debug("removeTask selectedTaskId=" + selectedTaskId);
        try {
            taskService.removeTask(selectedTaskId);
            initTasks();
            logger.debug("removeTask taskList=" + taskList);
            selectedTaskId = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Task Removed"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing task", e.toString()));
        }
    }

}

