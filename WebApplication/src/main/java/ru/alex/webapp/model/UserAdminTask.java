package ru.alex.webapp.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Alexander.Isaenco
 */
@Entity
@DiscriminatorValue("t")
public class UserAdminTask extends UserAdmin {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = true)
    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserAdminTask");
        sb.append("{task=").append(task);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}

