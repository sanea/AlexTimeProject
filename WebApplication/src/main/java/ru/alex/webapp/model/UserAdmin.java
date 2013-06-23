package ru.alex.webapp.model;

import ru.alex.webapp.model.enums.AdminType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alexander.Isaenco
 */
@Entity
@Table(name = "user_admin")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "admin_type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = AdminType.TYPE_NONE)
public abstract class UserAdmin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    //    @Column(name = "admin_type", nullable = false, length = 1, columnDefinition = "CHAR")
//    protected String adminType;
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    @Column(name = "end_time", nullable = false)
    private Date endTime;
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    @Column(name = "value_type", nullable = false, length = 1, columnDefinition = "CHAR")
    private String valueType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private User user;

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
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
