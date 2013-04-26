package ru.alex.webapp.beans;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.TimeSequenceWrapper;
import ru.alex.webapp.beans.wrappers.UserTaskTimeWrapper;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.UserTaskTimeSeq;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;
import ru.alex.webapp.service.UserTaskTimeService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class CancelTaskTimeMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CancelTaskTimeMB.class);

    @Autowired
    private UserTaskTimeService userTaskTimeService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SessionMB sessionMB;

    private List<Site> siteList;
    private List<User> userList;
    private List<Task> taskList;
    private List<TaskType> taskTypeList;
    private Site selectedSite;
    private User selectedUser;
    private Task selectedTask;
    private TaskType selectedTaskType;
    private Date dateFrom;
    private Date dateTo;

    private UserTaskTimeWrapper selectedTaskWrapper;
    private List<TimeSequenceWrapper> selectedTimeSeqList;

    private LazyDataModel<UserTaskTimeWrapper> lazyFilteredTasks;

    @PostConstruct
    private void init() {
        logger.debug("init");
        try {
            siteList = siteService.getNotDeletedSites();
            userList = userService.getEnabledNotDeletedUsers();
            taskList = taskService.getEnabledNotDeletedTasks();
            taskTypeList = Arrays.asList(TaskType.values());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of allTasks", e.toString()));
        }
    }

    public String getRowsPerPageTemplate() {
        String size = "";
        if (lazyFilteredTasks != null) {
            int rowCount = lazyFilteredTasks.getRowCount();
            size = rowCount > 50 ? "," + rowCount : "";
        }
        return "5,10,15,20,25,50" + size;
    }

    public List<Site> getSiteList() {
        return siteList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public List<TaskType> getTaskTypeList() {
        return taskTypeList;
    }

    public Site getSelectedSite() {
        return selectedSite;
    }

    public void setSelectedSite(Site selectedSite) {
        this.selectedSite = selectedSite;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public TaskType getSelectedTaskType() {
        return selectedTaskType;
    }

    public void setSelectedTaskType(TaskType selectedTaskType) {
        this.selectedTaskType = selectedTaskType;
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public LazyDataModel<UserTaskTimeWrapper> getLazyFilteredTasks() {
        return lazyFilteredTasks;
    }


    public UserTaskTimeWrapper getSelectedTaskWrapper() {
        return selectedTaskWrapper;
    }

    public void setSelectedTaskWrapper(UserTaskTimeWrapper selectedTaskWrapper) {
        this.selectedTaskWrapper = selectedTaskWrapper;
    }

    public List<TimeSequenceWrapper> getSelectedTimeSeqList() {
        return selectedTimeSeqList;
    }

    public void selectTaskListener(ActionEvent event) {
        selectedTaskWrapper = (UserTaskTimeWrapper) event.getComponent().getAttributes().get("taskTime");
        logger.debug("selectTaskListener selectedTaskWrapper={}", selectedTaskWrapper);
        try {
            List<UserTaskTimeSeq> taskTimeSeqList = buildTimeSeqList(selectedTaskWrapper.getUserTaskTimeSeq());
            logger.debug("selectTaskListener taskTimeSeqList={}", taskTimeSeqList);
            List<TimeSequenceWrapper> timeSeqList = new ArrayList<>(taskTimeSeqList.size());
            for (UserTaskTimeSeq timeSeq : taskTimeSeqList)
                timeSeqList.add(new TimeSequenceWrapper(timeSeq, sessionMB.getResourceBundle()));
            selectedTimeSeqList = timeSeqList;
            RequestContext.getCurrentInstance().addCallbackParam("showTaskDlg", true);
            logger.debug("selectTaskListener timeSeqList={}", selectedTimeSeqList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            RequestContext.getCurrentInstance().addCallbackParam("showTaskDlg", false);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in select task", e.toString()));
        }
    }

    public void filterTable() {
        logger.debug("filterTable selectedSite={}, selectedUser={}, selectedTask={}, selectedTaskType={}, dateFrom={}, dateTo={}", selectedSite, selectedUser, selectedTask, selectedTaskType, dateFrom, dateTo);
        lazyFilteredTasks = new TaskTimeLazyModel(userTaskTimeService, sessionMB.getResourceBundle(), selectedSite, selectedUser, selectedTask, selectedTaskType, dateFrom, dateTo);
    }


    public void reset() {
        selectedSite = null;
        selectedUser = null;
        selectedTask = null;
        selectedTaskType = null;
        dateFrom = null;
        dateTo = null;
        lazyFilteredTasks = null;
    }

    public void removeTaskTimeRecord() {
        logger.debug("removeTaskTimeRecord selectedTaskWrapper={}", selectedTaskWrapper);
        try {
            userTaskTimeService.remove(selectedTaskWrapper.getUserTaskTime());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in removing task time record", e.toString()));
        }
    }

    public void restoreTaskTimeRecord() {
        logger.debug("restoreTaskTimeRecord selectedTaskWrapper={}", selectedTaskWrapper);
        try {
            userTaskTimeService.restore(selectedTaskWrapper.getUserTaskTime());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in restoring task time record", e.toString()));
        }
    }

    private List<UserTaskTimeSeq> buildTimeSeqList(UserTaskTimeSeq timeSeq) throws Exception {
        logger.debug("buildTimeSeqList timeSeq={}", timeSeq);
        List<UserTaskTimeSeq> timeSeqList = new ArrayList<>();
        if (timeSeq != null) {
            timeSeqList.add(timeSeq);
            if (timeSeq.getNextTimeSeq() != null)
                timeSeqList.addAll(buildTimeSeqList(timeSeq.getNextTimeSeq()));
        }
        return timeSeqList;
    }


    public static class TaskTimeLazyModel extends LazyDataModel<UserTaskTimeWrapper> {
        private static final Logger logger = LoggerFactory.getLogger(TaskTimeLazyModel.class);

        private UserTaskTimeService userTaskTimeService;
        private ResourceBundle resourceBundle;
        private Site site;
        private User user;
        private Task task;
        private TaskType taskType;
        private Date dateFrom;
        private Date dateTo;
        private int rowCount = 0;


        public TaskTimeLazyModel(UserTaskTimeService userTaskTimeService, ResourceBundle resourceBundle, Site site, User user, Task task, TaskType taskType, Date from, Date to) {
            logger.debug("TaskTimeLazyModel() userTaskTimeService={}, site={}, user={}, task={}, taskType={}, from={}, to={}", userTaskTimeService, site, user, task, taskType, from, to);
            this.userTaskTimeService = userTaskTimeService;
            this.resourceBundle = resourceBundle;
            this.site = site;
            this.user = user;
            this.task = task;
            this.taskType = taskType;
            this.dateFrom = from;
            this.dateTo = to;
            try {
                rowCount = userTaskTimeService.getAllCount(site, user, task, taskType, from, to, true).intValue();
                logger.debug("TaskTimeLazyModel() rowCount={}", rowCount);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error building TaskTimeLazyModel", e.toString()));
            }
        }

        @Override
        public int getRowCount() {
            return rowCount;
        }

        @Override
        public List<UserTaskTimeWrapper> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
            logger.debug("load() first={}, pageSize={}, sortField={}, sortOrder={}, filters={}", first, pageSize, sortField, sortOrder, filters);
            List<UserTaskTimeWrapper> timeWrapperList = null;
            try {
                int last = first + pageSize > rowCount ? rowCount : first + pageSize;
                List<UserTaskTime> userTaskTimeList = userTaskTimeService.getAll(site, user, task, taskType, dateFrom, dateTo, true, first, last);
                logger.debug("load userTaskTimeList={}", userTaskTimeList);
                timeWrapperList = new ArrayList<>(userTaskTimeList.size());
                for (UserTaskTime taskTime : userTaskTimeList) {
                    //check if task is not current (null check for data consistence and performance)
                    if (taskTime.getTotal() == null && taskTime.getUserSiteTaskById().getCurrentTime().equals(taskTime))
                        continue;
                    UserTaskTimeWrapper taskTimeWrapper = new UserTaskTimeWrapper(taskTime, resourceBundle);
                    timeWrapperList.add(taskTimeWrapper);
                }
                logger.debug("load timeWrapperList={}", timeWrapperList);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in load of task time list", e.toString()));
            }
            return timeWrapperList;
        }
    }
}

