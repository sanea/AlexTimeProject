package ru.alex.webapp.beans.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.util.TimeUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @author Alex
 */
public class UserTaskWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserTaskWrapper.class);
    private UserSiteTask userSiteTask;
    private UserTaskTime taskTime;
    private int timeLeftSec;
    private BigDecimal sum;

    public UserTaskWrapper(UserSiteTask userSiteTask, UserTaskTime taskTime) {
        this(userSiteTask, taskTime, 0);
    }

    public UserTaskWrapper(UserSiteTask userSiteTask, UserTaskTime taskTime, int timeSpentSeq) {
        logger.debug("init UserTaskWrapper userSiteTask={}, taskTime={}", userSiteTask, taskTime);
        if (userSiteTask == null)
            throw new IllegalArgumentException("User task can't be null");
        this.userSiteTask = userSiteTask;
        this.taskTime = taskTime;
        if (taskTime != null) {
            Integer durationSec = getDurationSec();
            this.timeLeftSec = durationSec - timeSpentSeq;
            if (TaskType.getType(userSiteTask.getSiteTask().getTaskByTaskId().getType()) == TaskType.PROCESS)
                this.sum = durationSec != null ? getTaskPriceHour().multiply(new BigDecimal((double) durationSec / 3600)).setScale(2, RoundingMode.HALF_UP) : null;
            else
                this.sum = getTaskPriceHour();
            logger.debug("init UserTaskWrapper timeLeftSec={}, sum={}", this.timeLeftSec, this.sum);
        }
    }

    public String getUsername() {
        return userSiteTask.getUserByUsername().getUsername();
    }

    public String getTaskName() {
        return userSiteTask.getSiteTask().getTaskByTaskId().getName();
    }

    public String getTaskTypeFormatted() {
        return TaskType.getTypeFormatted(userSiteTask.getSiteTask().getTaskByTaskId().getType());
    }

    public String getTaskType() {
        return userSiteTask.getSiteTask().getTaskByTaskId().getType();
    }

    public BigDecimal getTaskPriceHour() {
        return userSiteTask.getSiteTask().getTaskByTaskId().getPriceHour();
    }

    public int getTimeLeftSec() {
        return timeLeftSec;
    }

    public void setTimeLeftSec(int timeLeftSec) {
        this.timeLeftSec = timeLeftSec;
    }

    public String getCurrentStatusFormatted() {
        return TaskStatus.getStatusFormatted(userSiteTask.getStatus());
    }

    public String getCurrentStatus() {
        return userSiteTask.getStatus();
    }

    public Long getTaskId() {
        return userSiteTask.getSiteTask().getTaskByTaskId().getId();
    }

    public Date getStartTime() {
        return taskTime != null ? taskTime.getStartTime() : null;
    }

    public Date getFinishTime() {
        return taskTime != null ? taskTime.getFinishTime() : null;
    }

    public Integer getDurationSec() {
        return taskTime != null ? taskTime.getDurationPlaySec() : null;
    }

    public String getDurationFormatted() {
        Integer durationSec = taskTime != null ? taskTime.getDurationPlaySec() : null;
        return durationSec != null ? TimeUtils.formatTimeSec(durationSec) : "";
    }

    public String getTimeLeftFormatted() {
        return TimeUtils.formatTimeSec(timeLeftSec);
    }

    public BigDecimal getSum() {
        return sum;
    }

    public UserTaskTime getTaskTime() {
        return taskTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTaskWrapper");
        sb.append("{userSiteTask=").append(userSiteTask);
        sb.append(", taskTime=").append(taskTime);
        sb.append(", timeLeftSec=").append(timeLeftSec);
        sb.append('}');
        return sb.toString();
    }

}
