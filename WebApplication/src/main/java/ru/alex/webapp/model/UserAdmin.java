package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Entity
@Table(name = "user_admin")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "admin_type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "n")
public abstract class UserAdmin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;
//    @Column(name = "admin_type", nullable = false, length = 1, columnDefinition = "CHAR")
//    protected String adminType;
    @Column(name = "start_time", nullable = false)
    protected Date startTime;
    @Column(name = "end_time", nullable = false)
    protected Date endTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    protected User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getAdminType() {
//        return adminType;
//    }
//
//    public void setAdminType(String adminType) {
//        this.adminType = adminType;
//    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAdmin userAdmin = (UserAdmin) o;

        if (id != null ? !id.equals(userAdmin.id) : userAdmin.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserAdmin");
        sb.append("{id=").append(id);
//        sb.append(", adminType='").append(adminType).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}

