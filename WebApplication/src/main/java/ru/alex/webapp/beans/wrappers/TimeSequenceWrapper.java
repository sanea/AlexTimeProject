package ru.alex.webapp.beans.wrappers;

import ru.alex.webapp.model.UserTaskTimeSeq;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.util.TimeUtils;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Alexander.Isaenco
 */
public class TimeSequenceWrapper {
    private Date startTime;
    private Date endTime;
    private int durationSec;
    private String durationFormatted;
    private String statusFormatted;


    public TimeSequenceWrapper(UserTaskTimeSeq timeSeq, Locale locale, ResourceBundle resourceBundle) {
        this.startTime = timeSeq.getStartTime();
        this.endTime = timeSeq.getEndTime();
        this.durationSec = (int) ((endTime.getTime() - startTime.getTime()) / 1000);
        this.durationFormatted = TimeUtils.formatTimeSec(durationSec, resourceBundle);
        this.statusFormatted = TaskStatus.getStatusFormatted(timeSeq.getTaskStatus(), locale);
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
