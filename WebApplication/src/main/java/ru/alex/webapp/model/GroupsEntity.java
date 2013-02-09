package ru.alex.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 06.02.13
 * Time: 2:11
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "groups", schema = "", catalog = "webapp")
@Entity
public class GroupsEntity {
    private long id;

    @Column(name = "id")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String groupName;

    @Column(name = "group_name")
    @Basic
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupsEntity that = (GroupsEntity) o;

        if (id != that.id) return false;
        if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        return result;
    }

    private Collection<GroupAuthoritiesEntity> groupAuthoritiesesById;

    @OneToMany(mappedBy = "groupsByGroupId")
    public Collection<GroupAuthoritiesEntity> getGroupAuthoritiesesById() {
        return groupAuthoritiesesById;
    }

    public void setGroupAuthoritiesesById(Collection<GroupAuthoritiesEntity> groupAuthoritiesesById) {
        this.groupAuthoritiesesById = groupAuthoritiesesById;
    }

    private Collection<GroupMembersEntity> groupMembersesById;

    @OneToMany(mappedBy = "groupsByGroupId")
    public Collection<GroupMembersEntity> getGroupMembersesById() {
        return groupMembersesById;
    }

    public void setGroupMembersesById(Collection<GroupMembersEntity> groupMembersesById) {
        this.groupMembersesById = groupMembersesById;
    }
}
