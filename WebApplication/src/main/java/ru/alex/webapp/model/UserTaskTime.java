package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_task_time")
@Entity
@NamedQueries({
        @NamedQuery(name = UserTaskTime.BY_SITE_ID, query = "SELECT u FROM UserTaskTime u WHERE u.userSiteTaskById.siteTask.siteBySiteId.id = :siteId"),
        @NamedQuery(name = UserTaskTime.CURRENT_BY_SITE_ID, query = "SELECT u.currentTime FROM UserSiteTask u WHERE u.siteTask.siteBySiteId.id = :siteId"),
        @NamedQuery(name = UserTaskTime.BY_TASK_ID, query = "SELECT u FROM UserTaskTime u WHERE u.userSiteTaskById.siteTask.taskByTaskId.id = :taskId"),
        @NamedQuery(name = UserTaskTime.CURRENT_BY_TASK_ID, query = "SELECT u.currentTime FROM UserSiteTask u WHERE u.siteTask.taskByTaskId.id = :taskId"),
        @NamedQuery(name = UserTaskTime.BY_USERNAME, query = "SELECT u FROM UserTaskTime u WHERE u.userSiteTaskById.userByUsername.username = :username"),
        @NamedQuery(name = UserTaskTime.CURRENT_BY_USER_ID, query = "SELECT u.currentTime FROM UserSiteTask u WHERE u.userByUsername.username = :username"),
        @NamedQuery(name = UserTaskTime.BY_SITE_TASK, query = "SELECT u FROM UserTaskTime u WHERE u.userSiteTaskById.siteTask.id = :siteTaskId"),
        @NamedQuery(name = UserTaskTime.BY_USER_SITE_TASK, query = "SELECT u FROM UserTaskTime u WHERE u.userSiteTaskById.id = :userSiteTaskId"),
        @NamedQuery(name = UserTaskTime.CURRENT_BY_SITE_TASK_ID, query = "SELECT u.currentTime FROM UserSiteTask u WHERE u.siteTask.id = :siteTaskId"),
        @NamedQuery(name = UserTaskTime.ALL, query = "SELECT u FROM UserTaskTime u"),
        @NamedQuery(name = UserTaskTime.ALL_NOT_DELETED, query = "SELECT u FROM UserTaskTime u WHERE u.deleted = false"),
        @NamedQuery(name = UserTaskTime.COUNT_NOT_DELETED, query = "SELECT COUNT(u) FROM UserTaskTime u WHERE u.deleted = false")
})
public class UserTaskTime implements Serializable {
    public static final String BY_SITE_ID = "UserTaskTime.BY_SITE_ID";
    public static final String CURRENT_BY_SITE_ID = "UserTaskTime.CURRENT_BY_SITE_ID";
    public static final String BY_TASK_ID = "UserTaskTime.BY_TASK_ID";
    public static final String CURRENT_BY_TASK_ID = "UserTaskTime.CURRENT_BY_TASK_ID";
    public static final String BY_USERNAME = "UserTaskTime.BY_USERNAME";
    public static final String CURRENT_BY_USER_ID = "UserTaskTime.CURRENT_BY_USER_ID";
    public static final String BY_SITE_TASK = "UserTaskTime.BY_SITE_TASK";
    public static final String BY_USER_SITE_TASK = "UserTaskTime.BY_USER_SITE_TASK";
    public static final String CURRENT_BY_SITE_TASK_ID = "UserTaskTime.CURRENT_BY_SITE_TASK_ID";
    public static final String ALL = "UserTaskTime.ALL";
    public static final String ALL_NOT_DELETED = "UserTaskTime.ALL_NOT_DELETED";
    public static final String COUNT_NOT_DELETED = "UserTaskTime.COUNT_NOT_DELETED";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    @Column(name = "finish_time", nullable = true)
    private Date finishTime;
    @Column(name = "finish_time_play", nullable = false)
    private Date finishTimePlay;
    @Column(name = "finish_time_custom1", nullable = true)
    private Date finishTimeCustom1;
    @Column(name = "finish_time_custom2", nullable = true)
    private Date finishTimeCustom2;
    @Column(name = "finish_time_custom3", nullable = true)
    private Date finishTimeCustom3;
    @Column(name = "duration_play", length = 11, nullable = false)
    private Integer durationPlaySec;
    @Column(name = "duration_custom1", length = 11, nullable = true)
    private Integer durationCustom1Sec;
    @Column(name = "duration_custom2", length = 11, nullable = true)
    private Integer durationCustom2Sec;
    @Column(name = "duration_custom3", length = 11, nullable = true)
    private Integer durationCustom3Sec;
    @Column(name = "price", nullable = false)
    private BigDecimal priceHour;
    @Column(name = "comment", nullable = true, length = 255)
    private String comment;
    @Column(name = "total", nullable = true)
    private BigDecimal total;
    @Column(name = "deleted", nullable = false, columnDefinition = "BIT")
    private Boolean deleted;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_task_id", referencedColumnName = "id", nullable = false)
    private UserSiteTask userSiteTaskById;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_change_id", referencedColumnName = "id", nullable = false)
    private UserChange userChange;
    @OneToMany(mappedBy = "userTaskTimeById", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Collection<UserAction> userActionsById;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "time_seq_id", referencedColumnName = "id", nullable = true)
    private TaskTimeSeq timeSeq;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getFinishTimePlay() {
        return finishTimePlay;
    }

    public void setFinishTimePlay(Date finishTimePlay) {
        this.finishTimePlay = finishTimePlay;
    }

    public Date getFinishTimeCustom1() {
        return finishTimeCustom1;
    }

    public void setFinishTimeCustom1(Date finishTimeCustom1) {
        this.finishTimeCustom1 = finishTimeCustom1;
    }

    public Date getFinishTimeCustom2() {
        return finishTimeCustom2;
    }

    public void setFinishTimeCustom2(Date finishTimeCustom2) {
        this.finishTimeCustom2 = finishTimeCustom2;
    }

    public Date getFinishTimeCustom3() {
        return finishTimeCustom3;
    }

    public void setFinishTimeCustom3(Date finishTimeCustom3) {
        this.finishTimeCustom3 = finishTimeCustom3;
    }

    public Integer getDurationPlaySec() {
        return durationPlaySec;
    }

    public void setDurationPlaySec(Integer durationPlaySec) {
        this.durationPlaySec = durationPlaySec;
    }

    public Integer getDurationCustom1Sec() {
        return durationCustom1Sec;
    }

    public void setDurationCustom1Sec(Integer durationCustom1Sec) {
        this.durationCustom1Sec = durationCustom1Sec;
    }

    public Integer getDurationCustom2Sec() {
        return durationCustom2Sec;
    }

    public void setDurationCustom2Sec(Integer durationCustom2Sec) {
        this.durationCustom2Sec = durationCustom2Sec;
    }

    public Integer getDurationCustom3Sec() {
        return durationCustom3Sec;
    }

    public void setDurationCustom3Sec(Integer durationCustom3Sec) {
        this.durationCustom3Sec = durationCustom3Sec;
    }

    public BigDecimal getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(BigDecimal priceHour) {
        this.priceHour = priceHour;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public UserSiteTask getUserSiteTaskById() {
        return userSiteTaskById;
    }

    public void setUserSiteTaskById(UserSiteTask userSiteTaskById) {
        this.userSiteTaskById = userSiteTaskById;
    }

    public UserChange getUserChange() {
        return userChange;
    }

    public void setUserChange(UserChange userChange) {
        this.userChange = userChange;
    }

    public Collection<UserAction> getUserActionsById() {
        return userActionsById;
    }

    public void setUserActionsById(Collection<UserAction> userActionsById) {
        this.userActionsById = userActionsById;
    }

    public TaskTimeSeq getTimeSeq() {
        return timeSeq;
    }

    public void setTimeSeq(TaskTimeSeq timeSeq) {
        this.timeSeq = timeSeq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTaskTime taskTime = (UserTaskTime) o;

        if (id != null ? !id.equals(taskTime.id) : taskTime.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserTaskTime");
        sb.append("{id=").append(id);
        sb.append(", startTime=").append(startTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", finishTimePlay=").append(finishTimePlay);
        sb.append(", finishTimeCustom1=").append(finishTimeCustom1);
        sb.append(", finishTimeCustom2=").append(finishTimeCustom2);
        sb.append(", finishTimeCustom3=").append(finishTimeCustom3);
        sb.append(", durationPlaySec=").append(durationPlaySec);
        sb.append(", durationCustom1Sec=").append(durationCustom1Sec);
        sb.append(", durationCustom2Sec=").append(durationCustom2Sec);
        sb.append(", durationCustom3Sec=").append(durationCustom3Sec);
        sb.append(", priceHour=").append(priceHour);
        sb.append(", comment=").append(comment);
        sb.append(", total=").append(total);
        sb.append(", deleted=").append(deleted);
        sb.append(", userSiteTaskById=").append(userSiteTaskById);
        sb.append(", userChange=").append(userChange);
        sb.append(", timeSeq=").append(timeSeq);
        sb.append('}');
        return sb.toString();
    }

    public void addUserAction(UserAction userAction) {
        userAction.setUserTaskTimeById(this);
        if (userActionsById == null)
            userActionsById = new ArrayList<>();
        userActionsById.add(userAction);
    }
}
