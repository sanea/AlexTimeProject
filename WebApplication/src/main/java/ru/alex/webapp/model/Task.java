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
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "type", nullable = false, length = 1)
    private String type;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
    @OneToMany(mappedBy = "taskByTaskId", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserTask> userTasksById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String taskName) {
        this.name = taskName;
    }

    public String getType() {
        return type;
    }

    public void setType(String taskType) {
        this.type = taskType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal taskPrice) {
        this.price = taskPrice;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean taskEnabled) {
        this.enabled = taskEnabled;
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

        if (type != task.type) return false;
        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (enabled != null ? !enabled.equals(task.enabled) : task.enabled != null) return false;
        if (name != null ? !name.equals(task.name) : task.name != null) return false;
        if (price != null ? !price.equals(task.price) : task.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Task");
        sb.append("{id=").append(id);
        sb.append(", taskName='").append(name).append('\'');
        sb.append(", taskType=").append(type);
        sb.append(", taskPrice=").append(price);
        sb.append(", taskEnabled='").append(enabled).append('\'');
        //sb.append(", userTasksById=").append(userTasksById); - LAZY
        sb.append('}');
        return sb.toString();
    }

}
