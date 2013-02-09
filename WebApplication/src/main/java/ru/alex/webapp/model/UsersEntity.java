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
@Table(name = "users", schema = "", catalog = "webapp")
@Entity
public class UsersEntity {
    private String username;

    @Column(name = "username")
    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    @Column(name = "password")
    @Basic
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private boolean enabled;

    @Column(name = "enabled")
    @Basic
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (enabled != that.enabled) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }

    private Collection<GroupMembersEntity> groupMembersesByUsername;

    @OneToMany(mappedBy = "usersByUsername")
    public Collection<GroupMembersEntity> getGroupMembersesByUsername() {
        return groupMembersesByUsername;
    }

    public void setGroupMembersesByUsername(Collection<GroupMembersEntity> groupMembersesByUsername) {
        this.groupMembersesByUsername = groupMembersesByUsername;
    }

    private Collection<UserTaskEntity> userTasksByUsername;

    @OneToMany(mappedBy = "usersByUsername")
    public Collection<UserTaskEntity> getUserTasksByUsername() {
        return userTasksByUsername;
    }

    public void setUserTasksByUsername(Collection<UserTaskEntity> userTasksByUsername) {
        this.userTasksByUsername = userTasksByUsername;
    }

    private Collection<UserTaskStatusEntity> userTaskStatusesByUsername;

    @OneToMany(mappedBy = "usersByUsername")
    public Collection<UserTaskStatusEntity> getUserTaskStatusesByUsername() {
        return userTaskStatusesByUsername;
    }

    public void setUserTaskStatusesByUsername(Collection<UserTaskStatusEntity> userTaskStatusesByUsername) {
        this.userTaskStatusesByUsername = userTaskStatusesByUsername;
    }
}
