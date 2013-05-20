package ru.alex.webapp.beans.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.webapp.model.TaskTime;
import ru.alex.webapp.model.TaskTimeSeq;
import ru.alex.webapp.model.UserChange;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.util.TimeUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author Alex
 */
public class TaskTimeWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TaskTimeWrapper.class);
    private TaskTime taskTime;
    private ResourceBundle resourceBundle;

    public TaskTimeWrapper(TaskTime taskTime, ResourceBundle resourceBundle) {
        logger.debug("init taskTime={}", taskTime);
        if (taskTime == null)
            throw new IllegalArgumentException("taskTime can't be null");
        this.taskTime = taskTime;
        this.resourceBundle = resourceBundle;
    }

    public Long getId() {
        return taskTime.getId();
    }

    public String getUsername() {
        return taskTime.getUserSiteTaskById().getUserByUsername().getUsername();
    }

    public String getSiteName() {
        return taskTime.getUserSiteTaskById().getSiteTask().getSiteBySiteId().getName();
    }

    public String getTaskName() {
        return taskTime.getUserSiteTaskById().getSiteTask().getTaskByTaskId().getName();
    }

    public String getTaskType() {
        return taskTime.getUserSiteTaskById().getSiteTask().getTaskByTaskId().getType();
    }

    public String getTaskTypeFormatted() {
        return TaskType.getTypeFormatted(getTaskType(), resourceBundle);
    }

    public BigDecimal getTaskPriceHour() {
        return taskTime.getPriceHour();
    }

    public boolean getTaskIncome() {
        return taskTime.getUserSiteTaskById().getSiteTask().getTaskByTaskId().getIncome();
    }

    public BigDecimal getTotal() {
        if (taskTime.getTotal() == null)
            return null;
        if (taskTime.getUserSiteTaskById().getSiteTask().getTaskByTaskId().getIncome())
            return taskTime.getTotal();
        else
            return taskTime.getTotal().negate();
    }

    public Date getStartTime() {
        return taskTime.getStartTime();
    }

    public Date getFinishTime() {
        return taskTime.getFinishTime();
    }

    public Integer getDurationPlaySec() {
        return taskTime.getDurationPlaySec();
    }

    public String getDurationPlayFormatted() {
        return TimeUtils.formatTimeSec(getDurationPlaySec(), resourceBundle);
    }

    public Integer getDurationCustom1Sec() {
        return taskTime.getDurationCustom1Sec();
    }

    public String getDurationCustom1Formatted() {
        return (getDurationCustom1Sec() != null) ? TimeUtils.formatTimeSec(getDurationCustom1Sec(), resourceBundle) : null;
    }

    public Integer getDurationCustom2Sec() {
        return taskTime.getDurationCustom2Sec();
    }

    public String getDurationCustom2Formatted() {
        return (getDurationCustom2Sec() != null) ? TimeUtils.formatTimeSec(getDurationCustom2Sec(), resourceBundle) : null;
    }

    public Integer getDurationCustom3Sec() {
        return taskTime.getDurationCustom3Sec();
    }

    public String getDurationCustom3Formatted() {
        return (getDurationCustom3Sec() != null) ? TimeUtils.formatTimeSec(getDurationCustom3Sec(), resourceBundle) : null;
    }

    public TaskTimeSeq getTaskTimeSeq() {
        return taskTime.getTimeSeq();
    }

    public UserChange getUserChange() {
        return taskTime.getUserChange();
    }

    public TaskTime getTaskTime() {
        return taskTime;
    }

    public Boolean getDeleted() {
        return taskTime.getDeleted();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTaskWrapper");
        sb.append("{taskTime=").append(taskTime);
        sb.append('}');
        return sb.toString();
    }

}
