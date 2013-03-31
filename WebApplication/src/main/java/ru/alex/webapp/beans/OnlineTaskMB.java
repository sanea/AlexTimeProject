package ru.alex.webapp.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.UserTaskWrapper;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.service.UserSiteTaskService;
import ru.alex.webapp.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class OnlineTaskMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(OnlineTaskMB.class);
    private static final int UPDATE_INTERVAL_SEC = 5;
    @Autowired
    private UserSiteTaskService userSiteTaskService;
    private List<UserTaskWrapper> onlineTasks;

    @PostConstruct
    private void init() {
        logger.debug("init");
        initOnlineTasks();
    }

    private void initOnlineTasks() {
        logger.debug("initOnlineTasks");
        try {
            List<UserSiteTask> tasks = userSiteTaskService.getAllCurrentTime();
            logger.debug("tasks={}", tasks);
            List<UserTaskWrapper> taskWrappers = new ArrayList<>(tasks.size());
            for (UserSiteTask ut : tasks) {
                taskWrappers.add(new UserTaskWrapper(ut));
            }
            onlineTasks = taskWrappers;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesUtil.getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
        }
    }

    public List<UserTaskWrapper> getOnlineTasks() {
        return onlineTasks;
    }

    public int getUpdateIntervalSec() {
        return UPDATE_INTERVAL_SEC;
    }

    public void refreshTable() {
        boolean needInit = false;
        for (UserTaskWrapper task : onlineTasks) {
            TaskStatus taskStatus = TaskStatus.getStatus(task.getCurrentStatus());
            if (taskStatus == TaskStatus.RUNNING || taskStatus == TaskStatus.CUSTOM1 || taskStatus == TaskStatus.CUSTOM2 || taskStatus == TaskStatus.CUSTOM3) {
                int timeLeft = task.getTimeLeftSec() - UPDATE_INTERVAL_SEC;
                if (timeLeft <= 0) {
                    needInit = true;
                    break;
                }
            }
        }
        if (needInit)
            initOnlineTasks();
    }

}

