package ru.alex.webapp.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import ru.alex.webapp.util.FacesUtil;

import java.util.Map;

/**
 * @author Alex
 */
public class ViewScope implements Scope {
    private static final Logger logger = LoggerFactory.getLogger(ViewScope.class);

    @Override
    public Object get(String name, ObjectFactory objectFactory) {
        Map<String, Object> viewMap = FacesUtil.getViewRoot().getViewMap();
        //logger.debug("get name={}, viewMap={}", name, viewMap);
        if (viewMap.containsKey(name)) {
            return viewMap.get(name);
        } else {
            Object object = objectFactory.getObject();
            viewMap.put(name, object);
            return object;
        }
    }

    @Override
    public Object remove(String name) {
        //logger.debug("remove name={}", name);
        return FacesUtil.getViewRoot().getViewMap().remove(name);
    }

    @Override
    public String getConversationId() {
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        //Not supported
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

}
