package ru.alex.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "groups")
@Entity
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "groupsByGroupId")
    private Collection<GroupAuthorities> groupAuthoritiesById;

    @OneToMany(mappedBy = "groupsByGroupId")
    private Collection<GroupMembers> groupMembersById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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

        Groups that = (Groups) o;

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


    public Collection<GroupAuthorities> getGroupAuthoritiesById() {
        return groupAuthoritiesById;
    }

    public void setGroupAuthoritiesById(Collection<GroupAuthorities> groupAuthoritiesById) {
        this.groupAuthoritiesById = groupAuthoritiesById;
    }


    public Collection<GroupMembers> getGroupMembersById() {
        return groupMembersById;
    }

    public void setGroupMembersById(Collection<GroupMembers> groupMembersById) {
        this.groupMembersById = groupMembersById;
    }
}
