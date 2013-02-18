package ru.alex.webapp.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_action")
@Entity
public class UserAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false, updatable = false)
    private Date timestamp;
    @Column(name = "action", nullable = false, updatable = false, length = 1)
    private char action;
    @Column(name = "delta_time", nullable = true, length = 11)
    private Integer deltaTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_time_id", referencedColumnName = "id", nullable = false)
    private UserTaskTime userTaskTimeById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public char getAction() {
        return action;
    }

    public void setAction(char action) {
        this.action = action;
    }

    public Integer getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(Integer deltaTime) {
        this.deltaTime = deltaTime;
    }

    public UserTaskTime getUserTaskTimeById() {
        return userTaskTimeById;
    }

    public void setUserTaskTimeById(UserTaskTime userTaskTimeById) {
        this.userTaskTimeById = userTaskTimeById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAction that = (UserAction) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public static enum Action {
        START('r'), PAUSE('p'), EXTEND('e'), FINISH('f'), STOP('s');
        private char action;

        private Action(char action) {
            this.action = action;
        }

        public static Action getAction(char action) {
            switch (action) {
                case 'r':
                    return START;
                case 'p':
                    return PAUSE;
                case 'e':
                    return EXTEND;
                case 'f':
                    return FINISH;
                case 's':
                    return STOP;
                default:
                    throw new IllegalArgumentException("wrong action");
            }
        }

        public char getAction() {
            return action;
        }

    }

}
