package ru.alex.webapp.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Group;
import ru.alex.webapp.service.GroupService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Alex
 */
@Component
public class GroupConverter implements Converter {
    private static final Logger logger = LoggerFactory.getLogger(GroupConverter.class);

    @Autowired
    GroupService groupService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        logger.debug("getAsObject value={}", value);
        if (value == null || value.equals("")) {
            return null;
        }
        Long id = Long.parseLong(value);
        Object result = groupService.findById(id);
        logger.debug("getAsObject {}", result);
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = value instanceof Group ? ((Group) value).getId().toString() : "";
        logger.debug("getAsString {}", result);
        return result;
    }
}
