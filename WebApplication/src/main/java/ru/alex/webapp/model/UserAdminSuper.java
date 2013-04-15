package ru.alex.webapp.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Alexander.Isaenco
 */
@Entity
@DiscriminatorValue("a")
public class UserAdminSuper extends UserAdmin {
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserAdminTask");
        sb.append("{").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}

