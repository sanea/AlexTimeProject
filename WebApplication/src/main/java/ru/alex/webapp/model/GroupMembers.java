package ru.alex.webapp.model;

import javax.persistence.*;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "group_members")
@Entity
public class GroupMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Groups groupsByGroupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private Users usersByUsername;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Groups getGroupsByGroupId() {
        return groupsByGroupId;
    }

    public void setGroupsByGroupId(Groups groupsByGroupId) {
        this.groupsByGroupId = groupsByGroupId;
    }

    public Users getUsersByUsername() {
        return usersByUsername;
    }

    public void setUsersByUsername(Users usersByUsername) {
        this.usersByUsername = usersByUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMembers that = (GroupMembers) o;
        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        return result;
    }
}
