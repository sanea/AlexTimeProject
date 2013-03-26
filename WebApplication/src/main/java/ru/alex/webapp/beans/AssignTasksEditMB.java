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
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.service.*;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        logger.debug("onSiteRowSelect site={}, {}", ((Site) event.getObject()), selectedSite);
//        FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected Site", selectedSite.toString()));
        //TODO populate assignedTaskList
        //test data
        assignedTaskList = new ArrayList<>(taskList.size());
        for (Task t : taskList) {
            assignedTaskList.add(new AssignedTask(t, false, false));
        }
    }

    public void onSiteRowUnselect(UnselectEvent event) {
        logger.debug("onSiteRowUnselect site={}, {}", ((Site) event.getObject()), selectedSite);
//        FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected Site", ((Site) event.getObject()).toString()));
        //TODO clear assignedTaskList and assignedUserList
        assignedTaskList = null;
        assignedUserList = null;
    }

    public void onTaskRowSelect(SelectEvent event) {
        logger.debug("onTaskRowSelect task={}, {}", ((AssignedTask) event.getObject()), selectedTask);
//        FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected Task", selectedTask.toString()));
        //TODO populate assignedUserList
        //test data
        assignedUserList = new ArrayList<>(userList.size());
        for (User u : userList) {
            assignedUserList.add(new AssignedUser(u, false, !selectedTask.isSelected()));
        }
    }

    public void onTaskRowUnselect(UnselectEvent event) {
        logger.debug("onTaskRowSelect task={}, {}", ((AssignedTask) event.getObject()), selectedTask);
//        FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected Task", ((Task) event.getObject()).toString()));
        //TODO clear assignedUserList
        assignedUserList = null;
    }

    public void selectAllTasks(AjaxBehaviorEvent event) {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) event.getComponent();
        boolean isSelected = checkbox.isSelected();
        logger.debug("selectAllTasks selectedSite={}, isSelected={}", selectedSite, isSelected);
        //TODO save or delete SiteTask relations (on delete disable user_site_task)
        for (AssignedTask t : assignedTaskList) {
            t.setSelected(isSelected);
        }

        selectedTask = null;
        assignedUserList = null;
    }

    public void selectTask(AjaxBehaviorEvent event) {
        SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) event.getComponent();
        boolean isSelected = checkbox.isSelected();
        AssignedTask assignedTask = (AssignedTask) checkbox.getAttributes().get("task");
        logger.debug("selectAllTasks selectedSite={}, isSelected={}, assignedTask={}", selectedSite, isSelected, assignedTask);
        //TODO save or delete SiteTask one relation
        assignedTask.setSelected(isSelected);
        if (assignedTask.equals(selectedTask)) {
            for (AssignedUser u : assignedUserList) {
                u.setDisabled(!isSelected);
            }
        }
    }

    public void selectAllUsers(AjaxBehaviorEvent event) {
        //TODO save or delete UserSiteTask relations
    }

    public void selectUser(AjaxBehaviorEvent event) {
        //TODO save or delete UserSiteTask one relation
    }

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

