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
@DiscriminatorValue("s")
public class UserAdminSite extends UserAdmin {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = true)
    private Site site;

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserAdminSite");
        sb.append("{site=").append(site);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}

