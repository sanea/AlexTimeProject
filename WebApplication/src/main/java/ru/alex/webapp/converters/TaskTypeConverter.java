package ru.alex.webapp.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.enums.TaskType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Alex
 */
@Component
public class TaskTypeConverter implements Converter {
    private static final Logger logger = LoggerFactory.getLogger(TaskTypeConverter.class);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        logger.debug("getAsObject value={}", value);
        if (value == null || value.equals("")) {
            return null;
        }
        Object result = TaskType.getType(value);
        logger.debug("getAsObject {}", result);
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = value instanceof TaskType ? ((TaskType) value).getTypeStr() : "";
        logger.debug("getAsString {}", result);
        return result;
    }
}
