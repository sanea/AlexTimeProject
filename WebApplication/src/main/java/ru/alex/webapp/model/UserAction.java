package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "user_action")
@Entity
public class UserAction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false, updatable = false)
    private Date timestamp;
    @Column(name = "action", nullable = false, updatable = false, length = 1)
    private String action;
    @Column(name = "time_seconds", nullable = true, length = 11)
    private Integer timeSeconds;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(Integer timeSeconds) {
        this.timeSeconds = timeSeconds;
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
        if (!(o instanceof UserAction)) return false;

        UserAction that = (UserAction) o;

        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (timeSeconds != null ? !timeSeconds.equals(that.timeSeconds) : that.timeSeconds != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (userTaskTimeById != null ? !userTaskTimeById.equals(that.userTaskTimeById) : that.userTaskTimeById != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (timeSeconds != null ? timeSeconds.hashCode() : 0);
        result = 31 * result + (userTaskTimeById != null ? userTaskTimeById.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserAction");
        sb.append("{id=").append(id);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", action='").append(action).append('\'');
        sb.append(", timeSeconds=").append(timeSeconds);
        sb.append(", userTaskTimeById=").append(userTaskTimeById == null ? null : userTaskTimeById.getId());
        sb.append('}');
        return sb.toString();
    }

    public static enum Action {
        START('r'), PAUSE('p'), RESUME('c'), EXTEND('e'), FINISH('f'), STOP('s');
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
                case 'c':
                    return RESUME;
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

        public String getActionStr() {
            return String.valueOf(action);
        }

        public char getAction() {
            return action;
        }

    }

}
