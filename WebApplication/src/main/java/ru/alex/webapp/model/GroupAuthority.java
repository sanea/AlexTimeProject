package ru.alex.webapp.model;


import javax.persistence.*;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "group_authorities")
@Entity
public class GroupAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "authority", nullable = false, length = 50, updatable = false)
    private String authority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group groupByGroupId;

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

    public Group getGroupByGroupId() {
        return groupByGroupId;
    }

    public void setGroupByGroupId(Group groupByGroupId) {
        this.groupByGroupId = groupByGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupAuthority that = (GroupAuthority) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
