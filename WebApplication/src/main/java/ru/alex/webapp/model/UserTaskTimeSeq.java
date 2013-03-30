package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_task_time_seq")
@Entity
public class UserTaskTimeSeq implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "task_status", nullable = false, length = 1, columnDefinition = "CHAR")
    private String taskStatus;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "next_id")
    private UserTaskTimeSeq nextTimeSeq;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "prev_id")
    private UserTaskTimeSeq prevTimeSeq;
    @OneToOne(mappedBy = "timeSeq", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private UserTaskTime userTaskTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date finishTime) {
        this.endTime = finishTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public UserTaskTimeSeq getNextTimeSeq() {
        return nextTimeSeq;
    }

    public void setNextTimeSeq(UserTaskTimeSeq nextTimeSeq) {
        this.nextTimeSeq = nextTimeSeq;
    }

    public UserTaskTimeSeq getPrevTimeSeq() {
        return prevTimeSeq;
    }

    public void setPrevTimeSeq(UserTaskTimeSeq prevTimeSeq) {
        this.prevTimeSeq = prevTimeSeq;
    }

    public UserTaskTime getUserTaskTime() {
        return userTaskTime;
    }

    public void setUserTaskTime(UserTaskTime userTaskTime) {
        this.userTaskTime = userTaskTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTaskTimeSeq that = (UserTaskTimeSeq) o;

        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTaskTimeSeq");
        sb.append("{id=").append(id);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", taskStatus=").append(taskStatus);
        sb.append(", nextTimeSeq=").append(nextTimeSeq == null ? null : nextTimeSeq.getId());
        sb.append(", prevTimeSeq=").append(prevTimeSeq == null ? null : prevTimeSeq.getId());
        sb.append('}');
        return sb.toString();
    }
}
