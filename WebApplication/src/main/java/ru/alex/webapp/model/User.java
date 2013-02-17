package ru.alex.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "users")
@Entity
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "userByUsername", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<GroupMember> groupMemberByUsername;

    @OneToMany(mappedBy = "userByUsername", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserTask> userTasksByUsername;

    @OneToMany(mappedBy = "userByUsername", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<UserTaskStatus> userTaskStatusesByUsername;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<GroupMember> getGroupMemberByUsername() {
        return groupMemberByUsername;
    }

    public void setGroupMemberByUsername(Collection<GroupMember> groupMemberByUsername) {
        this.groupMemberByUsername = groupMemberByUsername;
    }


    public Collection<UserTask> getUserTasksByUsername() {
        return userTasksByUsername;
    }

    public void setUserTasksByUsername(Collection<UserTask> userTasksByUsername) {
        this.userTasksByUsername = userTasksByUsername;
    }


    public Collection<UserTaskStatus> getUserTaskStatusesByUsername() {
        return userTaskStatusesByUsername;
    }

    public void setUserTaskStatusesByUsername(Collection<UserTaskStatus> userTaskStatusesByUsername) {
        this.userTaskStatusesByUsername = userTaskStatusesByUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

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


}