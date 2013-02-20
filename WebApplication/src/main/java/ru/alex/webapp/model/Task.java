package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "task")
@Entity
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public Collection<UserTask> getUserTasksById() {
        return userTasksById;
    }

    public void setUserTasksById(Collection<UserTask> userTasksById) {
        this.userTasksById = userTasksById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (taskType != task.taskType) return false;
        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (taskEnabled != null ? !taskEnabled.equals(task.taskEnabled) : task.taskEnabled != null) return false;
        if (taskName != null ? !taskName.equals(task.taskName) : task.taskName != null) return false;
        if (taskPrice != null ? !taskPrice.equals(task.taskPrice) : task.taskPrice != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (taskName != null ? taskName.hashCode() : 0);
        result = 31 * result + taskType;
        result = 31 * result + (taskPrice != null ? taskPrice.hashCode() : 0);
        result = 31 * result + (taskEnabled != null ? taskEnabled.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Task");
        sb.append("{id=").append(id);
        sb.append(", taskName='").append(taskName).append('\'');
        sb.append(", taskType=").append(taskType);
        sb.append(", taskPrice=").append(taskPrice);
        sb.append(", taskEnabled='").append(taskEnabled).append('\'');
        sb.append(", userTasksById=").append(userTasksById);
        sb.append('}');
        return sb.toString();
    }

    public static enum TaskType {
        PROCESS(1), TASK(2);
        private final int type;

        private TaskType(int type) {
            this.type = type;
        }

        public static TaskType getType(int type) {
            switch (type) {
                case 1:
                    return PROCESS;
                case 2:
                    return TASK;
                default:
                    throw new IllegalArgumentException("wrong task type");
            }
        }

        public static String getTypeStr(int type) {
            switch (type) {
                case 1:
                    return "Process";
                case 2:
                    return "Task";
                default:
                    throw new IllegalArgumentException("wrong task type");
            }
        }

        public int getType() {
            return type;
        }
    }


}
