package ru.alex.webapp.beans;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.UserTaskTimeWrapper;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.UserTaskTimeSeq;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;
import ru.alex.webapp.service.UserTaskTimeService;
import ru.alex.webapp.util.FacesUtil;
import ru.alex.webapp.util.TimeUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class AllTaskMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AllTaskMB.class);
    @Autowired
    private UserTaskTimeService userTaskTimeService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;

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

    private List<UserTaskTimeWrapper> filteredTasks;
    private int totalMinutesIncome;
    private int totalMinutesOutcome;
    private BigDecimal totalIncome;
    private BigDecimal totalOutcome;
    private BigDecimal summ;

    private UserTaskTimeWrapper selectedTaskWrapper;
    private List<TimeSequence> selectedTimeSeqList;

    //No LazyDataModel - no need, to count sums and salaries, we need to get all records! memory is more cheap

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

    private void getFilteredTasks(Site site, User user, Task task, TaskType taskType, Date from, Date to) {
        logger.debug("getFilteredTasks site={}, user={}, task={}, taskType={}, from={}, to={}", site, user, task, taskType, from, to);
        try {
            List<UserTaskTime> userTaskTimeList = userTaskTimeService.getAll(site, user, task, taskType, from, to);
            logger.debug("getFilteredTasks userTaskTimeList={}", userTaskTimeList);
            filteredTasks = new ArrayList<>(userTaskTimeList.size());
            totalMinutesIncome = 0;
            totalMinutesOutcome = 0;
            totalIncome = new BigDecimal(0);
            totalOutcome = new BigDecimal(0);
            summ = new BigDecimal(0);
            for (UserTaskTime taskTime : userTaskTimeList) {
                UserTaskTimeWrapper taskTimeWrapper = new UserTaskTimeWrapper(taskTime);
                filteredTasks.add(taskTimeWrapper);
                if (taskTimeWrapper.getTaskIncome()) {
                    totalMinutesIncome += taskTimeWrapper.getDurationPlaySec();
                    totalIncome = totalIncome.add(taskTimeWrapper.getTotal());
                } else {
                    totalMinutesOutcome += taskTimeWrapper.getDurationPlaySec();
                    totalOutcome = totalOutcome.add(taskTimeWrapper.getTotal());
                }
            }
            summ = totalIncome.add(totalOutcome);
            logger.debug("getFilteredTasks filteredTasks={}", filteredTasks);
            logger.debug("getFilteredTasks totalMinutesIncome={}, totalMinutesOutcome={}, totalIncome={}, totalOutcome={}, sum={}",
                    totalMinutesIncome, totalMinutesOutcome, totalIncome, totalOutcome, summ);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }


    public String getRowsPerPageTemplate() {
        String size = "";
        if (filteredTasks != null)
            size = filteredTasks.size() > 50 ? "," + filteredTasks.size() : "";
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

    public List<UserTaskTimeWrapper> getFilteredTasks() {
        return filteredTasks;
    }

    public int getTotalMinutesIncome() {
        return totalMinutesIncome;
    }

    public int getTotalMinutesOutcome() {
        return totalMinutesOutcome;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalOutcome() {
        return totalOutcome;
    }

    public BigDecimal getSumm() {
        return summ;
    }

    public UserTaskTimeWrapper getSelectedTaskWrapper() {
        return selectedTaskWrapper;
    }

    public void setSelectedTaskWrapper(UserTaskTimeWrapper selectedTaskWrapper) {
        this.selectedTaskWrapper = selectedTaskWrapper;
    }

    public List<TimeSequence> getSelectedTimeSeqList() {
        return selectedTimeSeqList;
    }

    public void selectTaskListener(ActionEvent event) {
        selectedTaskWrapper = (UserTaskTimeWrapper) event.getComponent().getAttributes().get("taskTime");
        logger.debug("selectTaskListener selectedTaskWrapper={}", selectedTaskWrapper);
        try {
            List<UserTaskTimeSeq> taskTimeSeqList = buildTimeSeqList(selectedTaskWrapper.getUserTaskTimeSeq());
            logger.debug("selectTaskListener taskTimeSeqList={}", taskTimeSeqList);
            List<TimeSequence> timeSeqList = new ArrayList<>(taskTimeSeqList.size());
            for (UserTaskTimeSeq timeSeq : taskTimeSeqList)
                timeSeqList.add(new TimeSequence(timeSeq));
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
        getFilteredTasks(selectedSite, selectedUser, selectedTask, selectedTaskType, dateFrom, dateTo);
    }

    public void reset() {
        selectedSite = null;
        selectedUser = null;
        selectedTask = null;
        selectedTaskType = null;
        dateFrom = null;
        dateTo = null;
        filteredTasks = null;
        totalMinutesIncome = 0;
        totalMinutesOutcome = 0;
        totalIncome = null;
        totalOutcome = null;
        summ = null;
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

    public static class TimeSequence {
        private Date startTime;
        private Date endTime;
        private int durationSec;
        private String durationFormatted;
        private String statusFormatted;

        public TimeSequence(UserTaskTimeSeq timeSeq) {
            this.startTime = timeSeq.getStartTime();
            this.endTime = timeSeq.getEndTime();
            this.durationSec = (int) ((endTime.getTime() - startTime.getTime()) / 1000);
            this.durationFormatted = TimeUtils.formatTimeSec(durationSec);
            this.statusFormatted = TaskStatus.getStatusFormatted(timeSeq.getTaskStatus());
        }

        public Date getStartTime() {
            return startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public int getDurationSec() {
            return durationSec;
        }

        public String getDurationFormatted() {
            return durationFormatted;
        }

        public String getStatusFormatted() {
            return statusFormatted;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("TimeSequence");
            sb.append("{startTime=").append(startTime);
            sb.append(", endTime=").append(endTime);
            sb.append(", durationSec=").append(durationSec);
            sb.append(", durationFormatted='").append(durationFormatted).append('\'');
            sb.append(", statusFormatted='").append(statusFormatted).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}

