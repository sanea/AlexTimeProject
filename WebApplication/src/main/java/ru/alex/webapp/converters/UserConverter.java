package ru.alex.webapp.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.User;
import ru.alex.webapp.service.UserService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Alex
 */
@Component
public class UserConverter implements Converter {
    private static final Logger logger = LoggerFactory.getLogger(UserConverter.class);

    @Autowired
    UserService userService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        logger.debug("getAsObject value={}", value);
        if (value == null || value.equals("")) {
            return null;
        }
        Object result = userService.findById(value);
        logger.debug("getAsObject {}", result);
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = value instanceof User ? ((User) value).getUsername() : "";
        logger.debug("getAsString {}", result);
        return result;
    }
}
