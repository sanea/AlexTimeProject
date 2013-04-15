package ru.alex.webapp.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import ru.alex.webapp.model.CustomAction;
import ru.alex.webapp.model.enums.CustomActionEnum;
import ru.alex.webapp.service.CustomActionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander.Isaenco
 */
public class CustomActionConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CustomActionConfiguration.class);
    private static volatile CustomActionConfiguration instance;

    Map<Long, CustomAction> customActionMap;

    private CustomActionConfiguration() throws Exception {
        logger.info("loading Custom Action Configuration");
        try {
            ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
            CustomActionService customActionService = ctx.getBean(CustomActionService.class);
            List<CustomAction> customActionList = customActionService.findAll();
            customActionMap = new HashMap<>(customActionList.size());
            for (CustomAction customAction : customActionList) {
                customActionMap.put(customAction.getId(), customAction);
            }
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

    public CustomAction getCustomAction(CustomActionEnum customActionEnum) {
        return customActionMap.get(customActionEnum.getId());
    }

}
