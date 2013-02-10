package ru.alex.webapp.model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 06.02.13
 * Time: 2:11
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "group_members", schema = "", catalog = "webapp")
@Entity
public class GroupMembersEntity {
    private long id;

    @Column(name = "id")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    private String username;
//
//    @javax.persistence.Column(name = "username")
//    @Basic
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMembersEntity that = (GroupMembersEntity) o;

//        if (groupId != that.groupId) return false;
        if (id != that.id) return false;
//        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
//        result = 31 * result + (username != null ? username.hashCode() : 0);
//        result = 31 * result + (int) (groupId ^ (groupId >>> 32));
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

    private UsersEntity usersByUsername;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UsersEntity getUsersByUsername() {
        return usersByUsername;
    }

    public void setUsersByUsername(UsersEntity usersByUsername) {
        this.usersByUsername = usersByUsername;
    }
}
