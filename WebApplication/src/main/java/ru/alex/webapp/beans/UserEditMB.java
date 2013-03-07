package ru.alex.webapp.beans;

import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.enums.TaskStatus;
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
public class UserEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserEditMB.class);
    Map<String, Boolean> userDeletable;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    private List<User> userList;
    private String userName;
    private User newUser = new User();
    private User selectedUser;

    @PostConstruct
    private void init() {
        userName = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("init username={}", userName);
        try {
            intiUsers();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of users", e.toString()));
        }
    }

    private void intiUsers() throws Exception {
        userList = userService.getAllUsers();
        logger.debug("intiUsers userList={}", userList);
        userDeletable = new HashMap<String, Boolean>(userList.size());
        for (User u : userList) {
            boolean isDeletable = userService.isDeletableUser(u);
            userDeletable.put(u.getUsername(), isDeletable);
        }
        logger.debug("initUsers userDeletable={}" + userDeletable);
    }

    public void onEdit(RowEditEvent event) {
        User user = (User) event.getObject();
        logger.debug("onEdit user={}", user);
        try {
            userService.updateUser(user);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("User Edited", user.getUsername()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in editing task", e.toString()));
        }

        try {
            intiUsers();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of users", e.toString()));
        }
    }

    public void onCancel(RowEditEvent event) {
        User user = (User) event.getObject();
        logger.debug("onCancel user={}", user);
    }

    public void addNewUserListener(ActionEvent event) {
        logger.debug("addNewUserListener");
        newUser = new User();
        newUser.setEnabled(true);
    }

    public void addNewUser() {
        logger.debug("addNewUser newUser={}", newUser);
        try {
            userService.addUser(newUser);
            intiUsers();
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("User Added", newUser.getUsername()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in adding user", e.toString()));
        }
    }

    public void assignListener(ActionEvent event) {
        //TODO
        try {
//            selectedUser = (User) event.getComponent().getAttributes().get("user");
//            logger.debug("assignListener selectedUser={}", selectedUser);
//
//            List<User> userList = userService.getAllEnabledUsers();
//            logger.debug("assignListener userList={}", userList);
//
//            List<UserTask> userTaskList = taskService.getUsersForTask(selectedTask.getId());
//            logger.debug("assignListener userTaskList={}", userTaskList);
//
//            assignedList = new ArrayList<UserTaskAssigned>(userList.size());
//
//            for (User u : userList) {
//                boolean isAssigned = false;
//                boolean isRunning = false;
//                for (UserTask ut : userTaskList) {
//                    if (u.getUsername().equals(ut.getUserByUsername().getUsername())) {
//                        isAssigned = true;
//                        isRunning = TaskStatus.getStatus(ut.getStatus()) == TaskStatus.RUNNING;
//                        break;
//                    }
//                }
//                assignedList.add(new UserTaskAssigned(u.getUsername(), isAssigned, isRunning));
//            }
//
//            logger.debug("assignListener assignedList={}", assignedList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void assignTask() {
        //TODO
        logger.debug("assignTask selectedUser={}", selectedUser);
        try {
//            Long innerSelectedTaskId = selectedTask.getId();
//            for (UserTaskAssigned assigned : assignedList) {
//                taskService.updateUserTask(innerSelectedTaskId, assigned.getUsername(), assigned.getAssigned());
//            }
//            initTasks();
//            logger.debug("assignTask taskList={}", taskList);
//            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Assigned", selectedTask.getName()));
//            assignedList = null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in assigning task", e.toString()));
        }
    }

    public void removeTask() {
        //TODO
        logger.debug("removeTask selectedUser={}", selectedUser);
        try {
//            taskService.removeTask(selectedTask.getId());
//            initTasks();
//            logger.debug("removeTask taskList={}", taskList);
//            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Task Removed", selectedTask.getName()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing task", e.toString()));
        }
    }

    public static class UserGroupAssigned {
        private String username;
        private boolean assigned;

        public UserGroupAssigned(String username, boolean assigned) {
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

