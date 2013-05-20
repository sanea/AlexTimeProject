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
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Alex
 */
public class UserTaskWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserTaskWrapper.class);
    private UserSiteTask userSiteTask;
    private Locale locale;
    private ResourceBundle resourceBundle;
    private Date startTime;
    private Date finishTime;
    private Integer durationSec;
    private String durationSecFormatted;


    public UserTaskWrapper(UserSiteTask userSiteTask, Locale locale, ResourceBundle resourceBundle) {
        logger.debug("init UserTaskWrapper userSiteTask={}", userSiteTask);
        if (userSiteTask == null)
            throw new IllegalArgumentException("User task can't be null");
        this.userSiteTask = userSiteTask;
        this.locale = locale;
        this.resourceBundle = resourceBundle;
        init();
    }

    private void init() {
        UserTaskTime currentTime = userSiteTask.getCurrentTime();
        if (currentTime != null) {
            startTime = currentTime.getStartTime();

            TaskStatus status = TaskStatus.getStatus(userSiteTask.getStatus());
            switch (status) {
                case RUNNING:
                    finishTime = currentTime.getFinishTimePlay();
                    durationSec = currentTime.getDurationPlaySec();
                    break;
                case CUSTOM1:
                    finishTime = currentTime.getFinishTimeCustom1();
                    durationSec = currentTime.getDurationCustom1Sec();
                    break;
                case CUSTOM2:
                    finishTime = currentTime.getFinishTimeCustom2();
                    durationSec = currentTime.getDurationCustom2Sec();
                    break;
                case CUSTOM3:
                    finishTime = currentTime.getFinishTimeCustom3();
                    durationSec = currentTime.getDurationCustom3Sec();
                    break;
                default:
                    throw new IllegalStateException("Unreachable case, current time should be null for " + status);
            }
            durationSecFormatted = TimeUtils.formatTimeSec(durationSec, resourceBundle);
        } else {
            startTime = null;
            finishTime = null;
            durationSec = null;
        }
    }

    public String getUsername() {
        return userSiteTask.getUserByUsername().getUsername();
    }

    public String getSiteName() {
        return userSiteTask.getSiteTask().getSiteBySiteId().getName();
    }

    public String getTaskName() {
        return userSiteTask.getSiteTask().getTaskByTaskId().getName();
    }

    public String getTaskType() {
        return userSiteTask.getSiteTask().getTaskByTaskId().getType();
    }

    public String getTaskTypeFormatted() {
        return TaskType.getTypeFormatted(userSiteTask.getSiteTask().getTaskByTaskId().getType(), resourceBundle);
    }

    public BigDecimal getTaskPriceHour() {
        UserTaskTime currentTime = userSiteTask.getCurrentTime();
        if (currentTime != null)
            return currentTime.getPriceHour();
        if (TaskType.getType(userSiteTask.getSiteTask().getTaskByTaskId().getType()) == TaskType.TASK_CUSTOM_PRICE)
            return null;
        return userSiteTask.getSiteTask().getTaskByTaskId().getPriceHour();
    }

    public String getComment() {
        UserTaskTime currentTime = userSiteTask.getCurrentTime();
        if (currentTime != null)
            return currentTime.getComment();
        else
            return null;
    }

    public int getTimeLeftSec() {
        int timeLeftSec = 0;
        Date now = new Date();
        UserTaskTime currentTime = userSiteTask.getCurrentTime();
        if (currentTime != null) {
            TaskStatus status = TaskStatus.getStatus(userSiteTask.getStatus());
            Date finishTime;
            switch (status) {
                case RUNNING:
                    finishTime = currentTime.getFinishTimePlay();
                    break;
                case CUSTOM1:
                    finishTime = currentTime.getFinishTimeCustom1();
                    break;
                case CUSTOM2:
                    finishTime = currentTime.getFinishTimeCustom2();
                    break;
                case CUSTOM3:
                    finishTime = currentTime.getFinishTimeCustom3();
                    break;
                default:
                    finishTime = null;
            }
            if (finishTime != null)
                timeLeftSec = (int) (finishTime.getTime() - now.getTime()) / 1000;
        }
        return timeLeftSec < 0 ? 0 : timeLeftSec;
    }

    public String getTimeLeftFormatted() {
        return TimeUtils.formatTimeSec(getTimeLeftSec(), resourceBundle);
    }

    public String getCurrentStatus() {
        return userSiteTask.getStatus();
    }

    public String getCurrentStatusFormatted() {
        return TaskStatus.getStatusFormatted(userSiteTask.getStatus(), locale);
    }

    public Long getTaskId() {
        return userSiteTask.getSiteTask().getTaskByTaskId().getId();
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public Integer getDurationSec() {
        return durationSec;
    }

    public String getDurationFormatted() {
        return durationSecFormatted;
    }

    public UserSiteTask getUserSiteTask() {
        return userSiteTask;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTaskWrapper");
        sb.append("{userSiteTask=").append(userSiteTask);
        sb.append("startTime=").append(startTime);
        sb.append("finishTime=").append(finishTime);
        sb.append("durationSec=").append(durationSec);
        sb.append("durationSecFormatted=").append(durationSecFormatted);
        sb.append('}');
        return sb.toString();
    }

}
