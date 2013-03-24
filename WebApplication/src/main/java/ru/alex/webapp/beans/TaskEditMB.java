package ru.alex.webapp.beans;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
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
    private static final Logger logger = LoggerFactory.getLogger(TaskEditMB.class);
    private List<Task> taskList;
    private Map<Long, Boolean> taskEditable;
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
        logger.debug("init username={}", userName);
        try {
            initTasks();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    private void initTasks() throws Exception {
        taskList = taskService.getAllTasks();
        logger.debug("initTasks taskList={}", taskList);
        taskEditable = new HashMap<Long, Boolean>(taskList.size());
        for (Task t : taskList) {
            boolean editable = taskService.isTaskEditable(t.getId());
            taskEditable.put(t.getId(), editable);
        }
        logger.debug("initTasks taskEditable={}", taskEditable);
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
        logger.debug("onEdit task={}", task);
        try {
            taskService.edit(task);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Task Edited", task.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing task", e.toString()));
        }

        try {
            initTasks();
            logger.debug("onEdit taskList={}", taskList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    public void onCancel(RowEditEvent event) {
        Task task = (Task) event.getObject();
        logger.debug("onCancel task={}", task);
    }

    public void addNewTaskListener(ActionEvent event) {
        logger.debug("addNewTaskListener");
        newTask = new Task();
        newTask.setEnabled(true);
    }

    public void addNewTask() {
        logger.debug("addNewTask newTask={}", newTask);
        try {
            taskService.add(newTask);
            initTasks();
            logger.debug("addNewTask taskList={}", taskList);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Task Added", newTask.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in adding task", e.toString()));
        }
    }

    public void assignListener(ActionEvent event) {
        try {
            selectedTask = (Task) event.getComponent().getAttributes().get("task");
            logger.debug("assignListener selectedTask={}", selectedTask);

            List<User> userList = null;//userService.getAllEnabledUsers();
            logger.debug("assignListener userList={}", userList);

            List<UserSiteTask> userSiteTaskList = taskService.getUsersForTask(selectedTask.getId());
            logger.debug("assignListener userSiteTaskList={}", userSiteTaskList);

            assignedList = new ArrayList<UserTaskAssigned>(userList.size());

            for (User u : userList) {
                boolean isAssigned = false;
                boolean isRunning = false;
                for (UserSiteTask ut : userSiteTaskList) {
                    if (u.getUsername().equals(ut.getUserByUsername().getUsername())) {
                        isAssigned = true;
                        isRunning = TaskStatus.getStatus(ut.getStatus()) == TaskStatus.RUNNING;
                        break;
                    }
                }
                assignedList.add(new UserTaskAssigned(u.getUsername(), isAssigned, isRunning));
            }
            RequestContext.getCurrentInstance().addCallbackParam("showAssignDlg", true);
            logger.debug("assignListener assignedList={}", assignedList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            RequestContext.getCurrentInstance().addCallbackParam("showAssignDlg", false);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void assignTask() {
        logger.debug("assignTask selectedTask={}", selectedTask);
        try {
            Long innerSelectedTaskId = selectedTask.getId();
            for (UserTaskAssigned assigned : assignedList) {
                taskService.updateUserTask(innerSelectedTaskId, assigned.getUsername(), assigned.getAssigned());
            }
            initTasks();
            logger.debug("assignTask taskList={}", taskList);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Assigned", selectedTask.getName()));
            assignedList = null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void removeTask() {
        logger.debug("removeTask selectedTask={}", selectedTask);
        try {
            taskService.remove(selectedTask);
            initTasks();
            logger.debug("removeTask taskList={}", taskList);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Task Removed", selectedTask.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing task", e.toString()));
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

