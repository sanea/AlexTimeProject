package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_change")
@Entity
public class UserChange implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    @Column(name = "end_time", nullable = false)
    private Date endTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private Site site;
    @OneToOne(mappedBy = "currentChange", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private User currentChangeUser;
    @OneToMany(mappedBy = "userChange", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Collection<UserTaskTime> userTaskTimeList;


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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public User getCurrentChangeUser() {
        return currentChangeUser;
    }

    public void setCurrentChangeUser(User user) {
        this.currentChangeUser = user;
    }

    public Collection<UserTaskTime> getUserTaskTimeList() {
        return userTaskTimeList;
    }

    public void setUserTaskTimeList(Collection<UserTaskTime> userTaskTimeList) {
        this.userTaskTimeList = userTaskTimeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserChange that = (UserChange) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserChange");
        sb.append("{id=").append(id);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", user=").append(user);
        sb.append(", site=").append(site);
        sb.append('}');
        return sb.toString();
    }
}