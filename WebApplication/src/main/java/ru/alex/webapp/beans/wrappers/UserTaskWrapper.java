package ru.alex.webapp.beans.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.webapp.model.UserTask;
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
    private UserTask userTask;
    private UserTaskTime taskTime;
    private int timeLeftSec;
    private BigDecimal sum;

    public UserTaskWrapper(UserTask userTask, UserTaskTime taskTime) {
        this(userTask, taskTime, 0);
    }

    public UserTaskWrapper(UserTask userTask, UserTaskTime taskTime, int timeSpentSeq) {
        logger.debug("init UserTaskWrapper userTask={}, taskTime={}", userTask, taskTime);
        if (userTask == null)
            throw new IllegalArgumentException("User task can't be null");
        this.userTask = userTask;
        this.taskTime = taskTime;
        if (taskTime != null) {
            Integer durationSec = getDurationSec();
            this.timeLeftSec = durationSec - timeSpentSeq;
            if (TaskType.getType(userTask.getTaskByTaskId().getType()) == TaskType.PROCESS)
                this.sum = durationSec != null ? getTaskPriceHour().multiply(new BigDecimal((double) durationSec / 3600)).setScale(2, RoundingMode.HALF_UP) : null;
            else
                this.sum = getTaskPriceHour();
            logger.debug("init UserTaskWrapper timeLeftSec={}, sum={}", this.timeLeftSec, this.sum);
        }
    }

    public String getUsername() {
        return userTask.getUserByUsername().getUsername();
    }

    public String getTaskName() {
        return userTask.getTaskByTaskId().getName();
    }

    public String getTaskTypeFormatted() {
        return TaskType.getTypeFormatted(userTask.getTaskByTaskId().getType());
    }

    public String getTaskType() {
        return userTask.getTaskByTaskId().getType();
    }

    public BigDecimal getTaskPriceHour() {
        return userTask.getTaskByTaskId().getPriceHour();
    }

    public int getTimeLeftSec() {
        return timeLeftSec;
    }

    public void setTimeLeftSec(int timeLeftSec) {
        this.timeLeftSec = timeLeftSec;
    }

    public String getCurrentStatusFormatted() {
        return TaskStatus.getStatusFormatted(userTask.getStatus());
    }

    public String getCurrentStatus() {
        return userTask.getStatus();
    }

    public Long getTaskId() {
        return userTask.getTaskByTaskId().getId();
    }

    public Date getStartTime() {
        return taskTime != null ? taskTime.getStartTime() : null;
    }

    public Date getFinishTime() {
        return taskTime != null ? taskTime.getFinishTime() : null;
    }

    public Integer getDurationSec() {
        return taskTime != null ? Integer.valueOf(taskTime.getDurationSec()) : null;
    }

    public String getDurationFormatted() {
        Integer durationSec = taskTime != null ? Integer.valueOf(taskTime.getDurationSec()) : null;
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
        sb.append("{userTask=").append(userTask);
        sb.append(", taskTime=").append(taskTime);
        sb.append(", timeLeftSec=").append(timeLeftSec);
        sb.append('}');
        return sb.toString();
    }

}
