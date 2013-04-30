package ru.alex.webapp.model;

import ru.alex.webapp.model.enums.AdminType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Alexander.Isaenco
 */
@Entity
@DiscriminatorValue(AdminType.TYPE_TASK)
public class UserAdminTask extends UserAdmin {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_task_id", referencedColumnName = "id", nullable = true)
    private SiteTask siteTask;

    public SiteTask getSiteTask() {
        return siteTask;
    }

    public void setSiteTask(SiteTask siteTask) {
        this.siteTask = siteTask;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserAdminTask");
        sb.append("{siteTask=").append(siteTask);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}

