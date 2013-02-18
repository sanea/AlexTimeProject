package ru.alex.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "groups")
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "groupByGroupId")
    private Collection<GroupAuthority> groupAuthorityById;

    @OneToMany(mappedBy = "groupByGroupId")
    private Collection<GroupMember> groupMemberById;

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

    public Collection<GroupAuthority> getGroupAuthorityById() {
        return groupAuthorityById;
    }

    public void setGroupAuthorityById(Collection<GroupAuthority> groupAuthorityById) {
        this.groupAuthorityById = groupAuthorityById;
    }


    public Collection<GroupMember> getGroupMemberById() {
        return groupMemberById;
    }

    public void setGroupMemberById(Collection<GroupMember> groupMemberById) {
        this.groupMemberById = groupMemberById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (id != null ? !id.equals(group.id) : group.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
