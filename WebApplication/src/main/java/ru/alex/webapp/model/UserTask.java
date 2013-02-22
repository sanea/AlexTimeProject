package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_task", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "task_id"}))
@Entity
public class UserTask implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "status", nullable = false, length = 1)
    private String status;
    @Column(name = "update_time", nullable = false)
    private Date updateTime;
    @Column(name = "create_time", nullable = false)
    private Date createTime;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private Task taskByTaskId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private User userByUsername;
    @OneToMany(mappedBy = "userTaskById", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserTaskTime> userTaskTimeList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Task getTaskByTaskId() {
        return taskByTaskId;
    }

    public void setTaskByTaskId(Task taskByTaskId) {
        this.taskByTaskId = taskByTaskId;
    }

    public User getUserByUsername() {
        return userByUsername;
    }

    public void setUserByUsername(User userByUsername) {
        this.userByUsername = userByUsername;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<UserTaskTime> getUserTaskTimeList() {
        return userTaskTimeList;
    }

    public void setUserTaskTimeList(Collection<UserTaskTime> userTaskTimeList) {
        this.userTaskTimeList = userTaskTimeList;
    }

    public void addUserTaskTime(UserTaskTime taskTime) {
        taskTime.setUserTaskById(this);
        if (userTaskTimeList == null)
            userTaskTimeList = new ArrayList<UserTaskTime>();
        userTaskTimeList.add(taskTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTask)) return false;

        UserTask userTask = (UserTask) o;

        if (createTime != null ? !createTime.equals(userTask.createTime) : userTask.createTime != null) return false;
        if (id != null ? !id.equals(userTask.id) : userTask.id != null) return false;
        if (status != null ? !status.equals(userTask.status) : userTask.status != null) return false;
        if (taskByTaskId != null ? !taskByTaskId.equals(userTask.taskByTaskId) : userTask.taskByTaskId != null)
            return false;
        if (updateTime != null ? !updateTime.equals(userTask.updateTime) : userTask.updateTime != null) return false;
        if (enabled != null ? !enabled.equals(userTask.updateTime) : userTask.enabled != null) return false;
        if (userByUsername != null ? !userByUsername.equals(userTask.userByUsername) : userTask.userByUsername != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (taskByTaskId != null ? taskByTaskId.hashCode() : 0);
        result = 31 * result + (userByUsername != null ? userByUsername.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTask");
        sb.append("{id=").append(id);
        sb.append(", status='").append(status).append('\'');
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", enabled=").append(enabled);
        sb.append(", taskByTaskId=").append(taskByTaskId == null ? null : taskByTaskId.getName());
        sb.append(", userByUsername=").append(userByUsername == null ? null : userByUsername.getUsername());
        //sb.append(", userTaskTimeList=").append(userTaskTimeList); - LAZY
        sb.append('}');
        return sb.toString();
    }

}
