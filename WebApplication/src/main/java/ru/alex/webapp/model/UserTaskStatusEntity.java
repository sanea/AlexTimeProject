package ru.alex.webapp.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * User: Alexander.Isaenco
 * Date: 06.02.13
 * Time: 13:23
 */
@Table(name = "user_task_status", schema = "", catalog = "webapp")
@Entity
public class UserTaskStatusEntity {
    public static enum TaskStatus {
        RUNNING("r"), COMPLETED("c"), PAUSED("p");
        private String status;

        private TaskStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

    }

    private long id;

    @Column(name = "id")
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

    @Column(name = "status")
    @Basic
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private int timeSpent;

    @Column(name = "time_spent")
    @Basic
    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTaskStatusEntity that = (UserTaskStatusEntity) o;

        if (id != that.id) return false;
//        if (taskId != that.taskId) return false;
        if (timeSpent != that.timeSpent) return false;
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
        return result;
    }

    private TaskEntity taskByTaskId;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public TaskEntity getTaskByTaskId() {
        return taskByTaskId;
    }

    public void setTaskByTaskId(TaskEntity taskByTaskId) {
        this.taskByTaskId = taskByTaskId;
    }

    private UsersEntity usersByUsername;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UsersEntity getUsersByUsername() {
        return usersByUsername;
    }

    public void setUsersByUsername(UsersEntity usersByUsername) {
        this.usersByUsername = usersByUsername;
    }

    private Collection<UserActionEntity> userActionsById;

    @OneToMany(mappedBy = "userTaskStatusByTaskStatusId")
    public Collection<UserActionEntity> getUserActionsById() {
        return userActionsById;
    }

    public void setUserActionsById(Collection<UserActionEntity> userActionsById) {
        this.userActionsById = userActionsById;
    }
}
