package ru.alex.webapp.beans;

import org.primefaces.event.data.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.UserTaskWrapper;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.UserTaskTimeSeq;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.util.TimeUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private TaskService taskService;
    private List<UserTaskWrapper> allTasks;
    private BigDecimal total = new BigDecimal(0);
    private List<UserTaskWrapper> filteredTasks;
    private UserTaskWrapper selectedTask;
    private List<TimeSequence> selectedTimeSeqList;
    private SelectItem[] taskTypeOptions;
    private Date dateFrom;
    private Date dateTo;

    //TODO implement LazyDataModel

    @PostConstruct
    private void init() {
        logger.debug("init");
        initAllTasks();

        TaskType[] values = TaskType.values();
        taskTypeOptions = new SelectItem[values.length + 1];
        taskTypeOptions[0] = new SelectItem("", "Select");
        SelectItem item;
        for (int i = 0; i < values.length; i++) {
            item = new SelectItem(values[i].getTypeStr(), values[i].getTypeFormatted());
            taskTypeOptions[i + 1] = item;
            logger.debug("init taskTypeOptions[{}] = {}:{}", (i + 1), item.getValue(), item.getLabel());
        }
    }

    private void initAllTasks() {
        logger.debug("initAllTasks");
        try {
            List<UserTaskTime> taskTimeList = taskService.getAllNotCurrentTime(dateFrom, dateTo);
            logger.debug("initAllTasks taskTimeList={}", taskTimeList);
            List<UserTaskWrapper> allTasksLocal = new ArrayList<UserTaskWrapper>(taskTimeList.size());
            for (UserTaskTime taskTime : taskTimeList) {
                UserTaskWrapper taskWrapper = new UserTaskWrapper(taskTime.getUserTaskById(), taskTime);
                allTasksLocal.add(taskWrapper);
                total = total.add(taskWrapper.getSum());
            }
            allTasks = allTasksLocal;
            logger.debug("initAllTasks allTasks={}", allTasks);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    public List<UserTaskWrapper> getAllTasks() {
        return allTasks;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<UserTaskWrapper> getFilteredTasks() {
        return filteredTasks;
    }

    public void setFilteredTasks(List<UserTaskWrapper> filteredTasks) {
        this.filteredTasks = filteredTasks;
    }

    public UserTaskWrapper getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(UserTaskWrapper selectedTask) {
        this.selectedTask = selectedTask;
    }

    public List<TimeSequence> getSelectedTimeSeqList() {
        return selectedTimeSeqList;
    }

    public SelectItem[] getTaskTypeOptions() {
        return taskTypeOptions;
    }

    public String getRowsPerPageTemplate() {
        String size = "";
        if (allTasks != null)
            size = allTasks.size() > 50 ? "," + allTasks.size() : "";
        return "5,10,15,20,25,50" + size;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void selectTaskListener(ActionEvent event) {
        try {
            selectedTask = (UserTaskWrapper) event.getComponent().getAttributes().get("task");
            logger.debug("selectTaskListener selectedTask={}", selectedTask);
            List<UserTaskTimeSeq> taskTimeSeqList = buildTimeSeqList(selectedTask.getTaskTime().getTimeSeq());
            logger.debug("selectTaskListener taskTimeSeqList={}", taskTimeSeqList);
            List<TimeSequence> timeSeqList = new ArrayList<TimeSequence>(taskTimeSeqList.size());
            for (UserTaskTimeSeq timeSeq : taskTimeSeqList)
                timeSeqList.add(new TimeSequence(timeSeq));
            selectedTimeSeqList = timeSeqList;
            logger.debug("selectTaskListener timeSeqList={}", selectedTimeSeqList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in select task", e.toString()));
        }
    }

    public void filterListener(FilterEvent event) {
        logger.debug("filterListener filteredTasks={}", filteredTasks);
        total = new BigDecimal(0);
        for (UserTaskWrapper taskWrapper : filteredTasks)
            total = total.add(taskWrapper.getSum());
        logger.debug("filterListener total={}", total);
    }

    public void updateTable() {
        logger.debug("updateTable dateFrom={}, dateTo={}", dateFrom, dateTo);
        initAllTasks();
    }

    private List<UserTaskTimeSeq> buildTimeSeqList(UserTaskTimeSeq timeSeq) throws Exception {
        logger.debug("buildTimeSeqList timeSeq={}", timeSeq);
        List<UserTaskTimeSeq> timeSeqList = new ArrayList<UserTaskTimeSeq>();
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

        public TimeSequence(UserTaskTimeSeq timeSeq) {
            this.startTime = timeSeq.getStartTime();
            this.endTime = timeSeq.getEndTime();
            this.durationSec = (int) ((endTime.getTime() - startTime.getTime()) / 1000);
            this.durationFormatted = TimeUtils.formatTimeSec(durationSec);
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

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("TimeSequence");
            sb.append("{startTime=").append(startTime);
            sb.append(", endTime=").append(endTime);
            sb.append(", durationSec=").append(durationSec);
            sb.append(", durationFormatted='").append(durationFormatted).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}

