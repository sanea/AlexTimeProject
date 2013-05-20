package ru.alex.webapp.beans;

import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.SiteTask;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.service.SiteTaskService;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;
import ru.alex.webapp.service.UserSiteTaskService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class AssignTasksEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AssignTasksEditMB.class);
    @Autowired
    private SiteService siteService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private SiteTaskService siteTaskService;
    @Autowired
    private UserSiteTaskService userSiteTaskService;
    private List<Site> siteList;
    private List<Task> taskList;
    private List<User> userList;
    private Site selectedSite;
    private AssignedTask selectedTask;
    private List<AssignedTask> assignedTaskList;
    private List<AssignedUser> assignedUserList;

    @PostConstruct
    private void init() {
        initSites();
        initTasks();
        initUsers();
    }

    private void initSites() {
        try {
            siteList = siteService.getNotDeletedSites();
            logger.debug("init siteList={}", siteList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of sites", e.toString()));
        }
    }

    private void initTasks() {
        try {
            taskList = taskService.getEnabledNotDeletedTasks();
            logger.debug("init siteList={}", taskList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    private void initUsers() {
        try {
            userList = userService.getEnabledNotDeletedUsers();
            logger.debug("init userList={}", userList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of users", e.toString()));
        }
    }

    public List<Site> getSiteList() {
        return siteList;
    }

    public List<AssignedTask> getAssignedTaskList() {
        return assignedTaskList;
    }

    public List<AssignedUser> getAssignedUserList() {
        return assignedUserList;
    }

    public Site getSelectedSite() {
        return selectedSite;
    }

    public void setSelectedSite(Site selectedSite) {
        this.selectedSite = selectedSite;
    }

    public AssignedTask getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(AssignedTask selectedTask) {
        this.selectedTask = selectedTask;
    }

    public void onSiteRowSelect(SelectEvent event) {
        logger.debug("onSiteRowSelect site={}", selectedSite);
        try {
            //find assigned tasks
            List<SiteTask> siteTaskList = siteTaskService.getNotDeletedSiteTasks(selectedSite);
            logger.debug("onSiteRowSelect siteTaskList={}", siteTaskList);
            Set<Task> taskSet = new HashSet<>(siteTaskList.size(), 1);
            for (SiteTask siteTask : siteTaskList)
                taskSet.add(siteTask.getTaskByTaskId());
            logger.debug("onSiteRowSelect taskSet={}", taskSet);

            //find tasks in progress
            List<UserSiteTask> userSiteTaskList = userSiteTaskService.getAllCurrentTime(selectedSite);
            logger.debug("onSiteRowSelect userSiteTaskList={}", userSiteTaskList);
            Set<Task> disabledTaskSet = new HashSet<>(userSiteTaskList.size(), 1);
            for (UserSiteTask userSiteTask : userSiteTaskList)
                disabledTaskSet.add(userSiteTask.getSiteTask().getTaskByTaskId());
            logger.debug("onSiteRowSelect disabledTaskSet={}", disabledTaskSet);

            assignedTaskList = new ArrayList<>(taskList.size());
            for (Task t : taskList) {
                boolean selected = taskSet.contains(t);
                boolean disabled = disabledTaskSet.contains(t);
                assignedTaskList.add(new AssignedTask(t, selected, disabled));
            }
            logger.debug("onSiteRowSelect assignedTaskList={}", assignedTaskList);
            selectedTask = null;
            assignedUserList = null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in onSiteRowSelect", e.toString()));
        }
    }

    public void onSiteRowUnselect(UnselectEvent event) {
        logger.debug("onSiteRowUnselect site={}", ((Site) event.getObject()));
        assignedTaskList = null;
        selectedTask = null;
        assignedUserList = null;
    }

    public void onTaskRowSelect(SelectEvent event) {
        logger.debug("onTaskRowSelect task={}", selectedTask);
        try {
            if (selectedTask.isSelected()) {
                assignedUserList = buildAssignedUserList(selectedSite, selectedTask.getTask());
            } else {
                assignedUserList = null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in onTaskRowSelect", e.toString()));
        }
    }

    private List<AssignedUser> buildAssignedUserList(Site site, Task task) throws Exception {
        logger.debug("buildAssignedUserList site={}, task={}", site, task);
        //find assigned siteTask for user
        List<UserSiteTask> userSiteTaskList = userSiteTaskService.getNotDeletedUserSiteTasks(site, task);
        logger.debug("buildAssignedUserList userSiteTaskList={}", userSiteTaskList);
        Set<User> selectedUserSet = new HashSet<>(userSiteTaskList.size(), 1);
        Set<User> disabledUserSet = new HashSet<>(userSiteTaskList.size());
        for (UserSiteTask userSiteTask : userSiteTaskList) {
            User user = userSiteTask.getUserByUsername();
            selectedUserSet.add(user);
            if (userSiteTask.getCurrentTime() != null)
                disabledUserSet.add(user);
        }
        logger.debug("buildAssignedUserList selectedUserSet={}", selectedUserSet);
        logger.debug("buildAssignedUserList disabledUserSet={}", disabledUserSet);
        List<AssignedUser> assignedUserList = new ArrayList<>(userList.size());
        for (User user : userList) {
            boolean selected = selectedUserSet.contains(user);
            boolean disabled = disabledUserSet.contains(user);
            assignedUserList.add(new AssignedUser(user, selected, disabled));
        }
        logger.debug("buildAssignedUserList assignedUserList={}", assignedUserList);
        return assignedUserList;
    }

    public void onTaskRowUnselect(UnselectEvent event) {
        logger.debug("onTaskRowSelect task={}, {}", ((AssignedTask) event.getObject()), selectedTask);
        assignedUserList = null;
    }

    public void assignTask(AjaxBehaviorEvent event) {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) event.getComponent();
        boolean isSelected = checkbox.isSelected();
        AssignedTask assignedTask = (AssignedTask) checkbox.getAttributes().get("task");
        logger.debug("selectTask selectedSite={}, isSelected={}, assignedTask={}", selectedSite, isSelected, assignedTask);
        try {
            if (isSelected) {
                siteTaskService.addSiteTask(selectedSite, assignedTask.getTask());
            } else {
                siteTaskService.removeSiteTask(selectedSite, assignedTask.getTask());
            }
            //rebuild list only when checkbox is in selected row
            if (assignedTask.equals(selectedTask)) {
                onTaskRowSelect(null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in selectTask", e.toString()));
        }
    }

    public void assignUser(AjaxBehaviorEvent event) {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) event.getComponent();
        boolean isSelected = checkbox.isSelected();
        AssignedUser assignedUser = (AssignedUser) checkbox.getAttributes().get("user");
        logger.debug("assignUser selectedSite={}, isSelected={}, assignedUser={}", selectedSite, isSelected, assignedUser);
        try {
            if (isSelected) {
                userSiteTaskService.addUserSiteTask(assignedUser.getUser(), selectedSite, selectedTask.getTask());
            } else {
                userSiteTaskService.removeUserSiteTask(assignedUser.getUser(), selectedSite, selectedTask.getTask());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in selectTask", e.toString()));
        }
    }

    // Helper class declaration:
    public static class AssignedTask {
        private Task task;
        private boolean selected;
        private boolean disabled;

        public AssignedTask(Task task, boolean selected, boolean disabled) {
            this.task = task;
            this.selected = selected;
            this.disabled = disabled;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        public String toString() {
            return "AssignTask{" +
                    "task=" + task +
                    ", selected=" + selected +
                    ", disabled=" + disabled +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AssignedTask that = (AssignedTask) o;

            if (task != null ? !task.equals(that.task) : that.task != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return task != null ? task.hashCode() : 0;
        }
    }

    public static class AssignedUser {
        private User user;
        private boolean selected;
        private boolean disabled;

        public AssignedUser(User user, boolean selected, boolean disabled) {
            this.user = user;
            this.selected = selected;
            this.disabled = disabled;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        public String toString() {
            return "AssignUser{" +
                    "user=" + user +
                    ", selected=" + selected +
                    ", disabled=" + disabled +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AssignedUser that = (AssignedUser) o;

            if (user != null ? !user.equals(that.user) : that.user != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return user != null ? user.hashCode() : 0;
        }
    }
}

