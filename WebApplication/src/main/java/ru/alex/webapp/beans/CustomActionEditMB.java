package ru.alex.webapp.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.CustomAction;
import ru.alex.webapp.service.CustomActionService;
import ru.alex.webapp.util.CustomActionConfiguration;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class CustomActionEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CustomActionEditMB.class);
    @Autowired
    private CustomActionService customActionService;
    @Autowired
    private SessionMB sessionMB;
    private List<CustomAction> customActions;
    private CustomAction selectedCustomAction;

    @PostConstruct
    private void init() {
        try {
            customActions = CustomActionConfiguration.getInstance().getAll();
            logger.debug("init customActions={}", customActions);
            selectedCustomAction = customActions.get(0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of custom actions", e.toString()));
        }
    }

    public Collection<CustomAction> getCustomActions() {
        return customActions;
    }

    public CustomAction getSelectedCustomAction() {
        return selectedCustomAction;
    }

    public void setSelectedCustomAction(CustomAction selectedCustomAction) {
        this.selectedCustomAction = selectedCustomAction;
    }

    public void updateCustomAction() {
        logger.debug("updateCustomAction customAction={}", selectedCustomAction);
        try {
            customActionService.update(selectedCustomAction);
            String customActionName = sessionMB.getLocale().equals(Locale.ENGLISH) ? selectedCustomAction.getNameEn() : selectedCustomAction.getNameRu();
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage("Custom Action updated", customActionName));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in adding site", e.toString()));
        }
        CustomActionConfiguration.getInstance().reload();
        init();
    }
}

