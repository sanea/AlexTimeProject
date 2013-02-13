package ru.alex.webapp.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "task")
@Entity
public class Task {
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_name", nullable = false, length = 50)
    private String taskName;

    @Column(name = "task_type", nullable = false, length = 1)
    private int taskType;

    @Column(name = "task_price", nullable = false, updatable = false)
    private BigDecimal taskPrice;

    @Column(name = "task_enabled", nullable = false, length = 1)
    private String taskEnabled;

    @OneToMany(mappedBy = "taskByTaskId", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserTask> userTasksById;

    @OneToMany(mappedBy = "taskByTaskId", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserTaskStatus> userTaskStatusesById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }


    public BigDecimal getTaskPrice() {
        return taskPrice;
    }

    public void setTaskPrice(BigDecimal taskPrice) {
        this.taskPrice = taskPrice;
    }


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

        Task that = (Task) o;

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

    public Collection<UserTask> getUserTasksById() {
        return userTasksById;
    }

    public void setUserTasksById(Collection<UserTask> userTasksById) {
        this.userTasksById = userTasksById;
    }

    public Collection<UserTaskStatus> getUserTaskStatusesById() {
        return userTaskStatusesById;
    }

    public void setUserTaskStatusesById(Collection<UserTaskStatus> userTaskStatusesById) {
        this.userTaskStatusesById = userTaskStatusesById;
    }

}
