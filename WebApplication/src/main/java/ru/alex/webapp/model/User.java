package ru.alex.webapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Alexander.Isaenco
 */
@Table(name = "users")
@Entity
@NamedQueries({
        @NamedQuery(name = User.ALL_ENABLED_NOT_DELETED, query = "SELECT u FROM User u WHERE u.deleted = false AND u.enabled = true")
})
public class User implements Serializable {
    public static final String ALL_ENABLED_NOT_DELETED = "User.ALL_ENABLED_NOT_DELETED";
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "username", length = 50)
    private String username;
    @Column(name = "password", length = 50, nullable = false)
    private String password;
    @Column(name = "password_salt", length = 50, nullable = false)
    private String passwordSalt;
    @Column(name = "enabled", nullable = false, columnDefinition = "BIT")
    private boolean enabled;
    @Column(name = "deleted", nullable = false, columnDefinition = "BIT")
    private boolean deleted;
    @Column(name = "address", length = 255, nullable = true)
    private String address;
    @Column(name = "city", length = 255, nullable = true)
    private String city;
    @Column(name = "country", length = 255, nullable = true)
    private String country;
    @Column(name = "e_mail", length = 255, nullable = true)
    private String email;
    @Column(name = "first_name", length = 255, nullable = true)
    private String firstName;
    @Column(name = "last_name", length = 255, nullable = true)
    private String lastName;
    @Column(name = "middle_name", length = 255, nullable = true)
    private String middleName;
    @Column(name = "phone1", length = 20, nullable = true)
    private String phone1;
    @Column(name = "phone2", length = 20, nullable = true)
    private String phone2;
    @OneToOne(mappedBy = "userByUsername", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private GroupMember groupMemberByUsername;
    @OneToMany(mappedBy = "userByUsername", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Collection<UserSiteTask> userSiteTasksByUsername;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Collection<UserChange> userChanges;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "current_change", referencedColumnName = "id", nullable = true)
    private UserChange currentChange;

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

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public GroupMember getGroupMemberByUsername() {
        return groupMemberByUsername;
    }

    public void setGroupMemberByUsername(GroupMember groupMemberByUsername) {
        this.groupMemberByUsername = groupMemberByUsername;
    }

    public Collection<UserSiteTask> getUserSiteTasksByUsername() {
        return userSiteTasksByUsername;
    }

    public void setUserSiteTasksByUsername(Collection<UserSiteTask> userSiteTasksByUsername) {
        this.userSiteTasksByUsername = userSiteTasksByUsername;
    }

    public Collection<UserChange> getUserChanges() {
        return userChanges;
    }

    public void setUserChanges(Collection<UserChange> userChanges) {
        this.userChanges = userChanges;
    }

    public UserChange getCurrentChange() {
        return currentChange;
    }

    public void setCurrentChange(UserChange currentChange) {
        this.currentChange = currentChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("User");
        sb.append("{username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", passwordSalt='").append(passwordSalt).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append(", deleted=").append(deleted);
        sb.append(", address='").append(address).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", phone1='").append(phone1).append('\'');
        sb.append(", phone2='").append(phone2).append('\'');
        sb.append(", currentChange startTime=").append(currentChange != null ? currentChange.getStartTime() : "null");
        sb.append('}');
        return sb.toString();
    }

    public void addUserChange(UserChange userChange) {
        userChange.setUser(this);
        if (userChanges == null)
            userChanges = new ArrayList<>();
        userChanges.add(userChange);
    }

}
