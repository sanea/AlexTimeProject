package ru.alex.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_task_status")
@Entity
public class UserTaskStatus {
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "status", length = 1, nullable = false)
    private String status;

    @Column(name = "time_spent", length = 11, nullable = true)
    private int timeSpent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private Task taskByTaskId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private Users usersByUsername;

    @OneToMany(mappedBy = "userTaskStatusByTaskStatusId", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserAction> userActionsById;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


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

        UserTaskStatus that = (UserTaskStatus) o;

        if (id != that.id) return false;
        if (timeSpent != that.timeSpent) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + timeSpent;
        return result;
    }


    public Task getTaskByTaskId() {
        return taskByTaskId;
    }

    public void setTaskByTaskId(Task taskByTaskId) {
        this.taskByTaskId = taskByTaskId;
    }

    public Users getUsersByUsername() {
        return usersByUsername;
    }

    public void setUsersByUsername(Users usersByUsername) {
        this.usersByUsername = usersByUsername;
    }

    public Collection<UserAction> getUserActionsById() {
        return userActionsById;
    }

    public void setUserActionsById(Collection<UserAction> userActionsById) {
        this.userActionsById = userActionsById;
    }
}
