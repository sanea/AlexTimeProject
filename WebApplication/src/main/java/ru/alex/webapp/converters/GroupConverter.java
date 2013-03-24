package ru.alex.webapp.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Group;
import ru.alex.webapp.service.GroupService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author Alex
 */
@Component
@FacesConverter("groupConverter")
public class GroupConverter implements Converter {

    @Autowired
    GroupService groupService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals("")) {
            return null;
        }
        Long id = Long.parseLong(value);

        return groupService.findById(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof Group ? ((Group) value).getId().toString() : "";
    }
}
