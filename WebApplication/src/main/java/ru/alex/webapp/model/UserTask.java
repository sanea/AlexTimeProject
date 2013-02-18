package ru.alex.webapp.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_task", uniqueConstraints=@UniqueConstraint(columnNames={"username", "task_id"}))
@Entity
public class UserTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "status", nullable = false, length = 1)
    private char status;

    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

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

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTask userTask = (UserTask) o;

        if (id != null ? !id.equals(userTask.id) : userTask.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public static enum TaskStatus {
        RUNNING('r'), COMPLETED('c'), PAUSED('p'), STOPPED('s');
        private char status;

        private TaskStatus(char status) {
            this.status = status;
        }

        public static TaskStatus getStatus(char status) {
            switch (status) {
                case 'r':
                    return RUNNING;
                case 'c':
                    return COMPLETED;
                case 'p':
                    return PAUSED;
                case 's':
                    return STOPPED;
                default:
                    throw new IllegalArgumentException("wrong status");
            }
        }

        public char getStatus() {
            return status;
        }

    }

}
