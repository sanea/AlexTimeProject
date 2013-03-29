package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "site")
@Entity
@NamedQueries({
        @NamedQuery(name = Site.ALL_NOT_DELETED, query = "SELECT s FROM Site s WHERE s.deleted = false"),
        @NamedQuery(name = Site.BY_USER_NOT_DELETED, query = "SELECT DISTINCT ust.siteTask.siteBySiteId FROM UserSiteTask ust WHERE ust.deleted = false AND ust.siteTask.deleted = false  AND ust.siteTask.siteBySiteId.deleted = false AND ust.userByUsername.username = :username")
})
public class Site implements Serializable {
    public static final String ALL_NOT_DELETED = "Site.ALL_NOT_DELETED";
    public static final String BY_USER_NOT_DELETED = "Site.BY_USER_NOT_DELETED";

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "address", length = 255, nullable = true)
    private String address;
    @Column(name = "city", length = 255, nullable = true)
    private String city;
    @Column(name = "country", length = 255, nullable = true)
    private String country;
    @Column(name = "deleted", nullable = false, columnDefinition = "BIT")
    private Boolean deleted;
    @OneToMany(mappedBy = "siteBySiteId", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Collection<SiteTask> siteTaskList;

    public Site() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Collection<SiteTask> getSiteTaskList() {
        return siteTaskList;
    }

    public void setSiteTaskList(Collection<SiteTask> siteTaskList) {
        this.siteTaskList = siteTaskList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        if (id != null ? !id.equals(site.id) : site.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Site");
        sb.append("{deleted=").append(deleted);
        sb.append(", id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
