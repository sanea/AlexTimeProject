package ru.alex.webapp.model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 06.02.13
 * Time: 2:11
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "group_authorities", schema = "", catalog = "webapp")
@Entity
public class GroupAuthoritiesEntity {
    private long id;

    @Column(name = "id")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    private long groupId;
//
//    @javax.persistence.Column(name = "group_id")
//    @Basic
//    public long getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(long groupId) {
//        this.groupId = groupId;
//    }

    private String authority;

    @Column(name = "authority")
    @Basic
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupAuthoritiesEntity that = (GroupAuthoritiesEntity) o;

//        if (groupId != that.groupId) return false;
        if (id != that.id) return false;
        if (authority != null ? !authority.equals(that.authority) : that.authority != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
//        result = 31 * result + (int) (groupId ^ (groupId >>> 32));
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        return result;
    }

    private GroupsEntity groupsByGroupId;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    public GroupsEntity getGroupsByGroupId() {
        return groupsByGroupId;
    }

    public void setGroupsByGroupId(GroupsEntity groupsByGroupId) {
        this.groupsByGroupId = groupsByGroupId;
    }

}
