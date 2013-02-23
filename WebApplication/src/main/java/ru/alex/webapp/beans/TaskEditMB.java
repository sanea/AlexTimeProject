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
    private Long selectedTaskId;
    private String selectedTaskName;
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

    public String getSelectedTaskName() {
        return selectedTaskName;
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
            Task selectedTask = (Task) event.getComponent().getAttributes().get("task");
            logger.debug("assignListener selectedTask=" + selectedTask);
            selectedTaskId = selectedTask.getId();
            selectedTaskName = selectedTask.getName();

            List<User> userList = userService.getAllUsers();
            logger.debug("assignListener userList=" + userList);

            List<UserTask> userTaskList = taskService.getUsersForTask(selectedTaskId);
            logger.debug("assignListener userTaskList=" + userTaskList);

            assignedList = new ArrayList<UserTaskAssigned>(userList.size());

            for (User u : userList) {
                boolean isAssigned = false;
                for (UserTask ut : userTaskList) {
                    if (u.getUsername().equals(ut.getUserByUsername().getUsername())) {
                        isAssigned = true;
                        break;
                    }
                }
                assignedList.add(new UserTaskAssigned(u.getUsername(), isAssigned));
            }

            logger.debug("assignListener assignedList=" + assignedList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in getting user list for task", e.toString()));
        }
    }

    public void assignTask() {
        logger.debug("assignTask selectedTaskId=" + selectedTaskId);
        try {
            Long innerSelectedTaskId = selectedTaskId;
            for (UserTaskAssigned assigned : assignedList) {
                 taskService.updateUserTask(innerSelectedTaskId, assigned.getUsername(), assigned.getAssigned());
            }
            initTasks();
            logger.debug("assignTask taskList=" + taskList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Assigned", selectedTaskName));
            selectedTaskId = null;
            selectedTaskName = null;
            assignedList = null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void removeListener(ActionEvent event) {
        Task selectedTask = (Task) event.getComponent().getAttributes().get("task");
        logger.debug("removeListener selectedTask=" + selectedTask);
        selectedTaskId = selectedTask.getId();
        selectedTaskName = selectedTask.getName();
    }

    public void removeTask() {
        logger.debug("removeTask selectedTaskId=" + selectedTaskId);
        try {
            taskService.removeTask(selectedTaskId);
            initTasks();
            logger.debug("removeTask taskList=" + taskList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Task Removed", selectedTaskName));
            selectedTaskId = null;
            selectedTaskName = null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing task", e.toString()));
        }
    }

    public static class UserTaskAssigned {
        private String username;
        private boolean assigned;

        public UserTaskAssigned(String username, boolean assigned) {
            this.username = username;
            this.assigned = assigned;
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

        @Override
        public String toString() {
            return "UserTaskAssigned{" +
                    "username='" + username + '\'' +
                    ", assigned=" + assigned +
                    '}';
        }
    }

}

