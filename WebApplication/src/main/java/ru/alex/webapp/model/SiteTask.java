package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "site_task", uniqueConstraints = @UniqueConstraint(columnNames = {"site_id", "task_id"}))
@Entity
public class SiteTask implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private Site siteBySiteId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private Task taskByTaskId;
    @OneToMany(mappedBy = "siteTask", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserTask> userTaskList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Site getSiteBySiteId() {
        return siteBySiteId;
    }

    public void setSiteBySiteId(Site siteBySiteId) {
        this.siteBySiteId = siteBySiteId;
    }

    public Task getTaskByTaskId() {
        return taskByTaskId;
    }

    public void setTaskByTaskId(Task taskByTaskId) {
        this.taskByTaskId = taskByTaskId;
    }

    public Collection<UserTask> getUserTaskList() {
        return userTaskList;
    }

    public void setUserTaskList(Collection<UserTask> userTaskList) {
        this.userTaskList = userTaskList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteTask siteTask = (SiteTask) o;

        if (id != null ? !id.equals(siteTask.id) : siteTask.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SiteTask");
        sb.append("{taskByTaskId=").append(taskByTaskId);
        sb.append(", siteBySiteId=").append(siteBySiteId);
        sb.append(", deleted=").append(deleted);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
