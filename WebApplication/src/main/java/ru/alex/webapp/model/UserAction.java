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
    @Column(name = "action", nullable = false, updatable = false, length = 1, columnDefinition = "CHAR")
    private String action;
    @Column(name = "params", nullable = true, length = 255)
    private String params;
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserAction");
        sb.append("{id=").append(id);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", action='").append(action).append('\'');
        sb.append(", params='").append(params).append('\'');
        sb.append(", userTaskTimeById=").append(userTaskTimeById);
        sb.append('}');
        return sb.toString();
    }
}
