package ru.alex.webapp.model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 06.02.13
 * Time: 2:11
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "user_task", schema = "", catalog = "webapp")
@Entity
public class UserTaskEntity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTaskEntity that = (UserTaskEntity) o;

        if (id != that.id) return false;
//        if (taskId != that.taskId) return false;
//        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
//        result = 31 * result + (username != null ? username.hashCode() : 0);
//        result = 31 * result + (int) (taskId ^ (taskId >>> 32));
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

}
