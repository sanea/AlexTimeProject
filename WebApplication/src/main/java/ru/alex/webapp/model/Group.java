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
@Table(name = "groups")
@Entity
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "group_name", nullable = false, length = 255)
    private String groupName;
    @OneToMany(mappedBy = "groupByGroupId", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Collection<GroupAuthority> groupAuthorityById;
    @OneToMany(mappedBy = "groupByGroupId", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Group");
        sb.append("{id=").append(id);
        sb.append(", groupName='").append(groupName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
