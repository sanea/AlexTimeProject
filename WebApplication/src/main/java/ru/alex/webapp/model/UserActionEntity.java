package ru.alex.webapp.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 10.02.13
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "user_action", schema = "", catalog = "webapp")
@Entity
public class UserActionEntity {
    private long id;

    @javax.persistence.Column(name = "id")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long taskStatusId;

    @javax.persistence.Column(name = "task_status_id")
    @Basic
    public long getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(long taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    private Timestamp timestamp;

    @javax.persistence.Column(name = "timestamp")
    @Basic
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    private String action;

    @javax.persistence.Column(name = "action")
    @Basic
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private int deltaTime;

    @javax.persistence.Column(name = "delta_time")
    @Basic
    public int getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(int deltaTime) {
        this.deltaTime = deltaTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserActionEntity that = (UserActionEntity) o;

        if (deltaTime != that.deltaTime) return false;
        if (id != that.id) return false;
        if (taskStatusId != that.taskStatusId) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (taskStatusId ^ (taskStatusId >>> 32));
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + deltaTime;
        return result;
    }

    private UserTaskStatusEntity userTaskStatusByTaskStatusId;

    @ManyToOne
    @javax.persistence.JoinColumn(name = "task_status_id", referencedColumnName = "id", nullable = false)
    public UserTaskStatusEntity getUserTaskStatusByTaskStatusId() {
        return userTaskStatusByTaskStatusId;
    }

    public void setUserTaskStatusByTaskStatusId(UserTaskStatusEntity userTaskStatusByTaskStatusId) {
        this.userTaskStatusByTaskStatusId = userTaskStatusByTaskStatusId;
    }
}
