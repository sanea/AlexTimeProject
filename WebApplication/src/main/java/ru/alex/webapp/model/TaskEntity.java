package ru.alex.webapp.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 06.02.13
 * Time: 2:11
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "task", schema = "", catalog = "webapp")
@Entity
public class TaskEntity {
    public static enum TaskType {
        Process(1), Task(2);
        private int type;

        private TaskType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
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

    private String taskName;

    @Column(name = "task_name")
    @Basic
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    private int taskType;

    @Column(name = "task_type")
    @Basic
    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    private BigDecimal taskPrice;

    @Column(name = "task_price")
    @Basic
    public BigDecimal getTaskPrice() {
        return taskPrice;
    }

    public void setTaskPrice(BigDecimal taskPrice) {
        this.taskPrice = taskPrice;
    }

    private String taskEnabled;

    @Column(name = "task_enabled")
    @Basic
    public String getTaskEnabled() {
        return taskEnabled;
    }

    public void setTaskEnabled(String taskEnabled) {
        this.taskEnabled = taskEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskEntity that = (TaskEntity) o;

        if (id != that.id) return false;
        if (taskPrice != that.taskPrice) return false;
        if (taskType != that.taskType) return false;
        if (taskName != null ? !taskName.equals(that.taskName) : that.taskName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (taskName != null ? taskName.hashCode() : 0);
        result = 31 * result + taskType;
        result = 31 * result + taskPrice.intValue();
        return result;
    }

    private Collection<UserTaskEntity> userTasksById;

    @OneToMany(mappedBy = "taskByTaskId")
    public Collection<UserTaskEntity> getUserTasksById() {
        return userTasksById;
    }

    public void setUserTasksById(Collection<UserTaskEntity> userTasksById) {
        this.userTasksById = userTasksById;
    }

    private Collection<UserTaskStatusEntity> userTaskStatusesById;

    @OneToMany(mappedBy = "taskByTaskId")
    public Collection<UserTaskStatusEntity> getUserTaskStatusesById() {
        return userTaskStatusesById;
    }

    public void setUserTaskStatusesById(Collection<UserTaskStatusEntity> userTaskStatusesById) {
        this.userTaskStatusesById = userTaskStatusesById;
    }


    private boolean activeForUser;

    @Transient
    public boolean isActiveForUser() {
        return activeForUser;
    }

    public void setActiveForUser(boolean activeForUser) {
        this.activeForUser = activeForUser;
    }

}
