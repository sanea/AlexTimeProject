package ru.alex.webapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "site")
@Entity
public class Site implements Serializable {
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
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    @OneToMany(mappedBy = "siteBySiteId", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Collection<SiteTask> siteTaskList;
    @OneToMany(mappedBy = "siteBySiteId", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Collection<UserChange> userChangeList;

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

    public Collection<UserChange> getUserChangeList() {
        return userChangeList;
    }

    public void setUserChangeList(Collection<UserChange> userChangeList) {
        this.userChangeList = userChangeList;
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
