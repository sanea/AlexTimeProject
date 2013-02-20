package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_task_time")
@Entity
public class UserTaskTime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "duration", length = 11, nullable = false)
    private int durationSec;
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    @Column(name = "finish_time", nullable = false)
    private Date finishTime;
    @Column(name = "current", nullable = false)
    private Boolean current;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_task_id", referencedColumnName = "id", nullable = false)
    private UserTask userTaskById;
    @OneToMany(mappedBy = "userTaskTimeById", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserAction> userActionsById;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "time_seq_id")
    private UserTaskTimeSeq timeSeq;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(int durationSec) {
        this.durationSec = durationSec;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public UserTask getUserTaskById() {
        return userTaskById;
    }

    public void setUserTaskById(UserTask userTaskById) {
        this.userTaskById = userTaskById;
    }

    public Collection<UserAction> getUserActionsById() {
        return userActionsById;
    }

    public void setUserActionsById(Collection<UserAction> userActionsById) {
        this.userActionsById = userActionsById;
    }

    public UserTaskTimeSeq getTimeSeq() {
        return timeSeq;
    }

    public void setTimeSeq(UserTaskTimeSeq timeSeq) {
        this.timeSeq = timeSeq;
    }

    public void addUserAction(UserAction userAction) {
        userAction.setUserTaskTimeById(this);
        if (userActionsById == null)
            userActionsById = new ArrayList<UserAction>();
        userActionsById.add(userAction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTaskTime)) return false;

        UserTaskTime taskTime = (UserTaskTime) o;

        if (durationSec != taskTime.durationSec) return false;
        if (finishTime != null ? !finishTime.equals(taskTime.finishTime) : taskTime.finishTime != null) return false;
        if (id != null ? !id.equals(taskTime.id) : taskTime.id != null) return false;
        if (startTime != null ? !startTime.equals(taskTime.startTime) : taskTime.startTime != null) return false;
        if (userTaskById != null ? !userTaskById.equals(taskTime.userTaskById) : taskTime.userTaskById != null)
            return false;
        if (current != null ? current.equals(taskTime.current) : taskTime.current != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + durationSec;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (finishTime != null ? finishTime.hashCode() : 0);
        result = 31 * result + (userTaskById != null ? userTaskById.hashCode() : 0);
        result = 31 * result + (current != null ? current.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTaskTime");
        sb.append("{id=").append(id);
        sb.append(", durationSec=").append(durationSec);
        sb.append(", startTime=").append(startTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", current=").append(current);
        sb.append(", userTaskById=").append(userTaskById == null ? null : userTaskById.getId());
        sb.append("userActionsById[");
        for (UserAction action : userActionsById)
            sb.append(", ").append(action.getId());
        sb.append("]");
        sb.append(", timeSeq=").append(timeSeq == null ? null : timeSeq.getId());
        sb.append('}');
        return sb.toString();
    }
}
