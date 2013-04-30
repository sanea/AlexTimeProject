package ru.alex.webapp.model;

import ru.alex.webapp.model.enums.AdminType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Alexander.Isaenco
 */
@Entity
@DiscriminatorValue(AdminType.TYPE_SUPER)
public class UserAdminSuper extends UserAdmin {
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserAdminSuper");
        sb.append("{").append(super.toString());
        sb.append('}');
        return sb.toString();
    }

}

