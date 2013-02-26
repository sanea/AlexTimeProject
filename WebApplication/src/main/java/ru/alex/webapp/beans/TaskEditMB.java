package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.*;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
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
    private Task selectedTask;
    private List<UserTaskAssigned> assignedList;

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

    public List<UserTaskAssigned> getAssignedList() {
        return assignedList;
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
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
            selectedTask = (Task) event.getComponent().getAttributes().get("task");
            logger.debug("assignListener selectedTask=" + selectedTask);

            List<User> userList = userService.getAllUsers();
            logger.debug("assignListener userList=" + userList);

            List<UserTask> userTaskList = taskService.getUsersForTask(selectedTask.getId());
            logger.debug("assignListener userTaskList=" + userTaskList);

            assignedList = new ArrayList<UserTaskAssigned>(userList.size());

            for (User u : userList) {
                boolean isAssigned = false;
                boolean isRunning = false;
                for (UserTask ut : userTaskList) {
                    if (u.getUsername().equals(ut.getUserByUsername().getUsername())) {
                        isAssigned = true;
                        isRunning = TaskStatus.getStatus(ut.getStatus()) == TaskStatus.RUNNING;
                        break;
                    }
                }
                assignedList.add(new UserTaskAssigned(u.getUsername(), isAssigned, isRunning));
            }

            logger.debug("assignListener assignedList=" + assignedList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in getting user list for task", e.toString()));
        }
    }

    public void assignTask() {
        logger.debug("assignTask selectedTask=" + selectedTask);
        try {
            Long innerSelectedTaskId = selectedTask.getId();
            for (UserTaskAssigned assigned : assignedList) {
                 taskService.updateUserTask(innerSelectedTaskId, assigned.getUsername(), assigned.getAssigned());
            }
            initTasks();
            logger.debug("assignTask taskList=" + taskList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Assigned", selectedTask.getName()));
            assignedList = null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void removeTask() {
        logger.debug("removeTask selectedTask=" + selectedTask);
        try {
            taskService.removeTask(selectedTask.getId());
            initTasks();
            logger.debug("removeTask taskList=" + taskList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Task Removed", selectedTask.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing task", e.toString()));
        }
    }

    public static class UserTaskAssigned {
        private String username;
        private boolean assigned;
        private boolean disabled;

        public UserTaskAssigned(String username, boolean assigned, boolean disabled) {
            this.username = username;
            this.assigned = assigned;
            this.disabled = disabled;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean getAssigned() {
            return assigned;
        }

        public void setAssigned(boolean assigned) {
            this.assigned = assigned;
        }

        public boolean getDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        public String toString() {
            return "UserTaskAssigned{" +
                    "username='" + username + '\'' +
                    ", assigned=" + assigned +
                    ", disabled=" + disabled +
                    '}';
        }
    }

}

