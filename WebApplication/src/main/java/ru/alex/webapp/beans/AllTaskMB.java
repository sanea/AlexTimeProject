package ru.alex.webapp.beans;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
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
import ru.alex.webapp.model.enums.CustomActionEnum;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;
import ru.alex.webapp.service.UserTaskTimeService;
import ru.alex.webapp.util.CustomActionConfiguration;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class AllTaskMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AllTaskMB.class);

    private static final Map<String, ColumnModel> COLUMN_MODEL = new LinkedHashMap<>();

    static {
        COLUMN_MODEL.put("id", new ColumnModel("id", "id", "id"));
        COLUMN_MODEL.put("siteName", new ColumnModel("site", "siteName", "siteName"));
        COLUMN_MODEL.put("username", new ColumnModel("user", "username", "username"));
        COLUMN_MODEL.put("taskName", new ColumnModel("task", "taskName", "taskName"));
        COLUMN_MODEL.put("taskType", new ColumnModel("type", "taskTypeFormatted", "taskType"));
        COLUMN_MODEL.put("taskPriceHour", new ColumnModel("price.hour", "taskPriceHour", "taskPriceHour"));
        COLUMN_MODEL.put("durationPlaySec", new ColumnModel("duration.play", "durationPlayFormatted", "durationPlaySec"));
        if (CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_1).getEnabled()) {
//            COLUMN_MODEL.put("durationCustom1Sec", new ColumnModel(CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_1).getName(), "durationCustom1Formatted", "durationCustom1Sec"));
            COLUMN_MODEL.put("durationCustom1Sec", new ColumnModel("custom1", "durationCustom1Formatted", "durationCustom1Sec"));
        }
        if (CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_2).getEnabled()) {
//            COLUMN_MODEL.put("durationCustom2Sec", new ColumnModel(CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_2).getName(), "durationCustom2Formatted", "durationCustom2Sec"));
            COLUMN_MODEL.put("durationCustom2Sec", new ColumnModel("custom2", "durationCustom2Formatted", "durationCustom2Sec"));
        }
        if (CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_3).getEnabled()) {
//            COLUMN_MODEL.put("durationCustom3Sec", new ColumnModel(CustomActionConfiguration.getInstance().getCustomAction(CustomActionEnum.CUSTOM_3).getName(), "durationCustom3Formatted", "durationCustom3Sec"));
            COLUMN_MODEL.put("durationCustom3Sec", new ColumnModel("custom3", "durationCustom3Formatted", "durationCustom3Sec"));
        }
        COLUMN_MODEL.put("total", new ColumnModel("total", "total", "total"));
        COLUMN_MODEL.put("startTime", new ColumnModel("start.time", "startTime", "startTime"));
        COLUMN_MODEL.put("finishTime", new ColumnModel("finish.time", "finishTime", "finishTime"));
    }

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

    private DataTable dataTable;
    private List<ColumnModel> columns;
    private Map<String, Boolean> colSelectedMap;
    private List<String> colNameList;

    private UserTaskTimeWrapper selectedTaskWrapper;
    private List<TimeSequenceWrapper> selectedTimeSeqList;

    //No LazyDataModel - no need, to count sums and salaries, we need to get all records! memory is more cheap

    public AllTaskMB() {
        logger.debug("COLUMN_MODEL={}", COLUMN_MODEL);
        columns = new ArrayList<>(COLUMN_MODEL.size());
        colSelectedMap = new LinkedHashMap<>(COLUMN_MODEL.size());
        for (String col : COLUMN_MODEL.keySet()) {
            colSelectedMap.put(col, true);
        }
        colNameList = new ArrayList<>(colSelectedMap.keySet());
        createDynamicColumns();
    }

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
                //check if task is not current (null check for data consistence and performance)
                if (taskTime.getTotal() == null && taskTime.getUserSiteTaskById().getCurrentTime().equals(taskTime))
                    continue;
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

    public String getTotalTimeIncome() {
        return TimeUtils.formatTimeSec(totalMinutesIncome);
    }

    public int getTotalMinutesOutcome() {
        return totalMinutesOutcome;
    }

    public String getTotalTimeOutcome() {
        return TimeUtils.formatTimeSec(totalMinutesOutcome);
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

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public Map<String, Boolean> getColSelectedMap() {
        return colSelectedMap;
    }

    public List<String> getColNameList() {
        return colNameList;
    }

    public Map<String, ColumnModel> getColumnModel() {
        return COLUMN_MODEL;
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
                timeSeqList.add(new TimeSequenceWrapper(timeSeq));
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

    public void selectColumnListener() {
        logger.debug("selectColumnListener");
        createDynamicColumns();
        dataTable.setSortBy(null);
    }

    private void createDynamicColumns() {
        logger.debug("createDynamicColumns colSelectedMap={}", colSelectedMap);
        columns.clear();
        for (String col : colSelectedMap.keySet()) {
            if (colSelectedMap.get(col)) {
                ColumnModel columnModel = COLUMN_MODEL.get(col);
                columns.add(columnModel);
            }
        }
        logger.debug("createDynamicColumns columns={}", columns);
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

    public static class ColumnModel implements Serializable {

        private String header;
        private String val;
        private String sortVal;

        public ColumnModel(String header, String val, String sortVal) {
            this.header = header;
            this.val = val;
            this.sortVal = sortVal;
        }

        public String getHeader() {
            return header;
        }

        public String getVal() {
            return val;
        }

        public String getSortVal() {
            return sortVal;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ColumnModel");
            sb.append("{header='").append(header).append('\'');
            sb.append(", val='").append(val).append('\'');
            sb.append(", sortVal='").append(sortVal).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}

