package ru.alex.webapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "type", nullable = false, length = 1, columnDefinition = "CHAR")
    private String type;
    @Column(name = "price", nullable = false)
    private BigDecimal priceHour;
    @Column(name = "enabled", nullable = false, columnDefinition = "BIT")
    private Boolean enabled;
    @Column(name = "deleted", nullable = false, columnDefinition = "BIT")
    private Boolean deleted;
    @Column(name = "income", nullable = false, columnDefinition = "BIT")
    private Boolean income;
    @Column(name = "repeat_task", nullable = true, length = 11)
    private Integer repeat;
    @OneToMany(mappedBy = "taskByTaskId", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Collection<SiteTask> siteTasksById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(BigDecimal priceHour) {
        this.priceHour = priceHour;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getIncome() {
        return income;
    }

    public void setIncome(Boolean income) {
        this.income = income;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Collection<SiteTask> getSiteTasksById() {
        return siteTasksById;
    }

    public void setSiteTasksById(Collection<SiteTask> siteTasksById) {
        this.siteTasksById = siteTasksById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != null ? !id.equals(task.id) : task.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Task");
        sb.append("{id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", priceHour=").append(priceHour);
        sb.append(", enabled=").append(enabled);
        sb.append(", deleted=").append(deleted);
        sb.append(", income=").append(income);
        sb.append(", repeat=").append(repeat);
        sb.append('}');
        return sb.toString();
    }
}
