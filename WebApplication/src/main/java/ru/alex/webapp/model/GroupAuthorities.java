package ru.alex.webapp.model;


import javax.persistence.*;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "group_authorities")
@Entity
public class GroupAuthorities {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "authority", nullable = false, length = 50, updatable = false)
    private String authority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Groups groupsByGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Groups getGroupsByGroupId() {
        return groupsByGroupId;
    }

    public void setGroupsByGroupId(Groups groupsByGroupId) {
        this.groupsByGroupId = groupsByGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupAuthorities that = (GroupAuthorities) o;

        if (id != that.id) return false;
        if (authority != null ? !authority.equals(that.authority) : that.authority != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        return result;
    }
}
