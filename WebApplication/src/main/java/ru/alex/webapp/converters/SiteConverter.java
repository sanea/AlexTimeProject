package ru.alex.webapp.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.service.TaskService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Alex
 */
@Component
public class SiteConverter implements Converter {
    private static final Logger logger = LoggerFactory.getLogger(SiteConverter.class);

    @Autowired
    SiteService siteService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        logger.debug("getAsObject value={}", value);
        if (value == null || value.equals("")) {
            return null;
        }
        Long id = Long.parseLong(value);
        Object result = siteService.findById(id);
        logger.debug("getAsObject {}", result);
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = value instanceof Site ? ((Site) value).getId().toString() : "";
        logger.debug("getAsString {}", result);
        return result;
    }
}
