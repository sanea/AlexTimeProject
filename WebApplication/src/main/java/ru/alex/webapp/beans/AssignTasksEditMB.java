package ru.alex.webapp.beans;

import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.service.SiteService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class AssignTasksEditMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AssignTasksEditMB.class);


    @PostConstruct
    private void init() {
    }



}

