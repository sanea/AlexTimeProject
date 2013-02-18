package ru.alex.webapp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_task_time")
@Entity
public class UserTaskTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "time_spent", length = 11)
    private int timeSpent;
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    @Column(name = "finish_time", nullable = false)
    private Date finishTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_task_id", referencedColumnName = "id", nullable = false)
    private UserTask userTaskById;
    @OneToMany(mappedBy = "userTaskTimeById", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserAction> userActionsById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
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

    public UserTask getUserTaskById() {
        return userTaskById;
    }

    public void setUserTaskById(UserTask userTaskById) {
        this.userTaskById = userTaskById;
    }

    public Collection<UserAction> getUserActionsById() {
        return userActionsById;
    }

    public void setUserActionsById(Collection<UserAction> userActionsById) {
        this.userActionsById = userActionsById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTaskTime that = (UserTaskTime) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void addUserAction(UserAction userAction) {
        userAction.setUserTaskTimeById(this);
        if (userActionsById == null)
            userActionsById = new ArrayList<UserAction>();
        userActionsById.add(userAction);
    }
}
