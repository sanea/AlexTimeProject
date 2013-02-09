package ru.alex.webapp.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

/**
 * User: Alexander.Isaenco
 * Date: 06.02.13
 * Time: 13:23
 */
@javax.persistence.Table(name = "user_task_status", schema = "", catalog = "webapp")
@Entity
public class UserTaskStatusEntity {
    public static enum TaskStatus {
        IN_PROGRESS("p"), COMPLETED("c");
        private String status;

        private TaskStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

    }

    private long id;

    @javax.persistence.Column(name = "id")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    private String username;
//
//    @javax.persistence.Column(name = "username")
//    @Basic
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    private long taskId;
//
//    @javax.persistence.Column(name = "task_id")
//    @Basic
//    public long getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(long taskId) {
//        this.taskId = taskId;
//    }

    private String status;

    @javax.persistence.Column(name = "status")
    @Basic
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private int timeSpent;

    @javax.persistence.Column(name = "time_spent")
    @Basic
    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    private Timestamp startTimestamp;

    @javax.persistence.Column(name = "start_timestamp")
    @Basic
    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Timestamp startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    private Timestamp endTimestamp;

    @javax.persistence.Column(name = "end_timestamp")
    @Basic
    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTaskStatusEntity that = (UserTaskStatusEntity) o;

        if (id != that.id) return false;
//        if (taskId != that.taskId) return false;
        if (timeSpent != that.timeSpent) return false;
        if (endTimestamp != null ? !endTimestamp.equals(that.endTimestamp) : that.endTimestamp != null) return false;
        if (startTimestamp != null ? !startTimestamp.equals(that.startTimestamp) : that.startTimestamp != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
//        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
//        result = 31 * result + (username != null ? username.hashCode() : 0);
//        result = 31 * result + (int) (taskId ^ (taskId >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + timeSpent;
        result = 31 * result + (startTimestamp != null ? startTimestamp.hashCode() : 0);
        result = 31 * result + (endTimestamp != null ? endTimestamp.hashCode() : 0);
        return result;
    }

    private TaskEntity taskByTaskId;

    @ManyToOne
    @javax.persistence.JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public TaskEntity getTaskByTaskId() {
        return taskByTaskId;
    }

    public void setTaskByTaskId(TaskEntity taskByTaskId) {
        this.taskByTaskId = taskByTaskId;
    }

    private UsersEntity usersByUsername;

    @ManyToOne
    @javax.persistence.JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UsersEntity getUsersByUsername() {
        return usersByUsername;
    }

    public void setUsersByUsername(UsersEntity usersByUsername) {
        this.usersByUsername = usersByUsername;
    }
}
