package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_site_task", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "site_task_id"}))
@Entity
@NamedQueries({
        @NamedQuery(name = UserSiteTask.BY_USERNAME, query = "SELECT u FROM UserSiteTask u WHERE userByUsername.username = :username"),
        @NamedQuery(name = UserSiteTask.ALL_CURRENT_TIME, query = "SELECT u FROM UserSiteTask u WHERE u.currentTime is not null"),
        @NamedQuery(name = UserSiteTask.ALL_NOT_DELETED_BY_SITE_TASK, query = "SELECT u FROM UserSiteTask u WHERE u.deleted = false AND u.siteTask.id = :siteTaskId"),
        @NamedQuery(name = UserSiteTask.BY_USER_SITE_TASK, query = "SELECT u FROM UserSiteTask u WHERE u.userByUsername.username = :username AND u.siteTask.siteBySiteId.id = :siteId AND u.siteTask.taskByTaskId.id = :taskId")
})
public class UserSiteTask implements Serializable {
    public static final String BY_USERNAME = "UserSiteTask.BY_USERNAME";
    public static final String ALL_CURRENT_TIME = "UserSiteTask.ALL_CURRENT_TIME";
    public static final String ALL_NOT_DELETED_BY_SITE_TASK = "UserSiteTask.ALL_NOT_DELETED_BY_SITE_TASK";
    public static final String BY_USER_SITE_TASK = "UserSiteTask.BY_USER_SITE_TASK";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "status", nullable = false, length = 1, columnDefinition = "CHAR")
    private String status;
    @Column(name = "update_time", nullable = false)
    private Date updateTime;
    @Column(name = "create_time", nullable = false)
    private Date createTime;
    @Column(name = "deleted", nullable = false, columnDefinition = "BIT")
    private Boolean deleted;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_task_id", referencedColumnName = "id", nullable = false)
    private SiteTask siteTask;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private User userByUsername;
    @OneToMany(mappedBy = "userSiteTaskById", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserTaskTime> userTaskTimeList;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "`current_time`", referencedColumnName = "id", nullable = true)
    private UserTaskTime currentTime;

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public SiteTask getSiteTask() {
        return siteTask;
    }

    public void setSiteTask(SiteTask siteTask) {
        this.siteTask = siteTask;
    }

    public User getUserByUsername() {
        return userByUsername;
    }

    public void setUserByUsername(User userByUsername) {
        this.userByUsername = userByUsername;
    }

    public Collection<UserTaskTime> getUserTaskTimeList() {
        return userTaskTimeList;
    }

    public void setUserTaskTimeList(Collection<UserTaskTime> userTaskTimeList) {
        this.userTaskTimeList = userTaskTimeList;
    }

    public UserTaskTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(UserTaskTime currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSiteTask userSiteTask = (UserSiteTask) o;

        if (id != null ? !id.equals(userSiteTask.id) : userSiteTask.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserSiteTask");
        sb.append("{id=").append(id);
        sb.append(", status='").append(status).append('\'');
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", siteTask=").append(siteTask);
        sb.append(", userByUsername=").append(userByUsername);
        sb.append(", currentTime=").append(currentTime);
        sb.append('}');
        return sb.toString();
    }
}
