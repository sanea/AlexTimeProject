package ru.alex.webapp.beans.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alex.webapp.model.UserChange;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.UserTaskTimeSeq;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.util.TimeUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alex
 */
public class UserTaskTimeWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserTaskTimeWrapper.class);
    private UserTaskTime userTaskTime;

    public UserTaskTimeWrapper(UserTaskTime userTaskTime) {
        logger.debug("init userTaskTime={}", userTaskTime);
        if (userTaskTime == null)
            throw new IllegalArgumentException("userTaskTime can't be null");
        this.userTaskTime = userTaskTime;
    }

    public Long getId() {
        return userTaskTime.getId();
    }

    public String getUsername() {
        return userTaskTime.getUserSiteTaskById().getUserByUsername().getUsername();
    }

    public String getSiteName() {
        return userTaskTime.getUserSiteTaskById().getSiteTask().getSiteBySiteId().getName();
    }

    public String getTaskName() {
        return userTaskTime.getUserSiteTaskById().getSiteTask().getTaskByTaskId().getName();
    }

    public String getTaskType() {
        return userTaskTime.getUserSiteTaskById().getSiteTask().getTaskByTaskId().getType();
    }

    public String getTaskTypeFormatted() {
        return TaskType.getTypeFormatted(getTaskType());
    }

    public BigDecimal getTaskPriceHour() {
        return userTaskTime.getPriceHour();
    }

    public boolean getTaskIncome() {
        return userTaskTime.getUserSiteTaskById().getSiteTask().getTaskByTaskId().getIncome();
    }

    public BigDecimal getTotal() {
        if (userTaskTime.getTotal() == null)
            return null;
        if (userTaskTime.getUserSiteTaskById().getSiteTask().getTaskByTaskId().getIncome())
            return userTaskTime.getTotal();
        else
            return userTaskTime.getTotal().negate();
    }

    public Date getStartTime() {
        return userTaskTime.getStartTime();
    }

    public Date getFinishTime() {
        return userTaskTime.getFinishTime();
    }

    public Integer getDurationPlaySec() {
        return userTaskTime.getDurationPlaySec();
    }

    public String getDurationPlayFormatted() {
        return TimeUtils.formatTimeSec(getDurationPlaySec());
    }

    public Integer getDurationCustom1Sec() {
        return userTaskTime.getDurationCustom1Sec();
    }

    public String getDurationCustom1Formatted() {
        return (getDurationCustom1Sec() != null) ? TimeUtils.formatTimeSec(getDurationCustom1Sec()) : null;
    }

    public Integer getDurationCustom2Sec() {
        return userTaskTime.getDurationCustom2Sec();
    }

    public String getDurationCustom2Formatted() {
        return (getDurationCustom2Sec() != null) ? TimeUtils.formatTimeSec(getDurationCustom2Sec()) : null;
    }

    public Integer getDurationCustom3Sec() {
        return userTaskTime.getDurationCustom3Sec();
    }

    public String getDurationCustom3Formatted() {
        return (getDurationCustom3Sec() != null) ? TimeUtils.formatTimeSec(getDurationCustom3Sec()) : null;
    }

    public UserTaskTimeSeq getUserTaskTimeSeq() {
        return userTaskTime.getTimeSeq();
    }

    public UserChange getUserChange() {
        return userTaskTime.getUserChange();
    }

    public UserTaskTime getUserTaskTime() {
        return userTaskTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTaskWrapper");
        sb.append("{userTaskTime=").append(userTaskTime);
        sb.append('}');
        return sb.toString();
    }

}
