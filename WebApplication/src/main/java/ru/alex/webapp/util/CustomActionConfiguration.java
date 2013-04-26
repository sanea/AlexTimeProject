package ru.alex.webapp.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import ru.alex.webapp.model.CustomAction;
import ru.alex.webapp.model.enums.CustomActionEnum;
import ru.alex.webapp.service.CustomActionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Alexander.Isaenco
 */
public class CustomActionConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CustomActionConfiguration.class);
    private static volatile CustomActionConfiguration instance;

    CustomActionService customActionService;
    Map<Long, CustomAction> customActionMap;

    private CustomActionConfiguration() throws Exception {
        logger.info("loading Custom Action Configuration");
        try {
            ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
            customActionService = ctx.getBean(CustomActionService.class);
            loadFromDb();
            logger.info("loaded success! customActionMap={}", customActionMap);
        } catch (Exception e) {
            logger.error("Can't load CustomActionConfiguration", e);
            throw new Exception(e);
        }
    }

    public static CustomActionConfiguration getInstance() {
        CustomActionConfiguration localInstance = instance;
        if (localInstance == null) {
            synchronized (CustomActionConfiguration.class) {
                localInstance = instance;
                if (localInstance == null) {
                    try {
                        instance = localInstance = new CustomActionConfiguration();
                    } catch (Exception e) {
                        logger.error("Can't get instance", e);
                    }
                }
            }
        }
        return localInstance;
    }

    private void loadFromDb() {
        List<CustomAction> customActionList = customActionService.findAll();
        customActionMap = new HashMap<>(customActionList.size());
        for (CustomAction customAction : customActionList) {
            customActionMap.put(customAction.getId(), customAction);
        }
    }

    public CustomAction getCustomAction(CustomActionEnum customActionEnum) {
        if (customActionEnum == null)
            throw new IllegalArgumentException("Wrong customActionEnum = null");
        return customActionMap.get(new Long(customActionEnum.getId()));
    }

    public List<CustomAction> getAll() {
        List<CustomAction> customActionList = new ArrayList<>(customActionMap.size());
        for (CustomAction customAction : customActionMap.values())
            customActionList.add(customAction);
        return customActionList;
    }

    public synchronized void reload() {
        loadFromDb();
    }

    public static String getCustomActionFormatted(CustomActionEnum customActionEnum, Locale locale) {
        CustomAction customAction = CustomActionConfiguration.getInstance().getCustomAction(customActionEnum);
        if (locale.equals(Locale.ENGLISH))
            return customAction.getNameEn();
        else
            return customAction.getNameRu();
    }

}
