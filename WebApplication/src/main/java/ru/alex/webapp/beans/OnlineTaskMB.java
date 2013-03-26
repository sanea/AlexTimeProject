package ru.alex.webapp.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.UserTaskWrapper;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.service.TaskService;
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
    @Autowired
    private TaskService taskService;
    private List<UserTaskWrapper> onlineTasks;
    private final int updateIntervalSec = 5;

    @PostConstruct
    private void init() {
        logger.debug("init");
        initOnlineTasks();
    }

    private void initOnlineTasks() {
        logger.debug("initOnlineTasks");
        try {
            List<UserSiteTask> tasks = taskService.getOnlineTasks();
            logger.debug("tasks={}", tasks);
            List<UserTaskWrapper> taskWrappers = new ArrayList<>(tasks.size());
            for (UserSiteTask ut : tasks) {
                UserTaskTime currentTime = taskService.getCurrentTimeForUserTask(ut.getSiteTask().getTaskByTaskId().getId(), ut.getUserByUsername().getUsername());
                logger.debug("currentTime={}", currentTime);
                int timeSpentSec = taskService.getTimeSpentSecForUserTask(ut.getSiteTask().getTaskByTaskId().getId(), ut.getUserByUsername().getUsername());
                logger.debug("timeSpentSec={}", timeSpentSec);
                taskWrappers.add(new UserTaskWrapper(ut, currentTime, timeSpentSec));
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
        return updateIntervalSec;
    }

    public void refreshTable() {
        boolean needInit = false;
        for (UserTaskWrapper task : onlineTasks) {
            int timeLeft = task.getTimeLeftSec() - updateIntervalSec;
            if (timeLeft > 0)
                task.setTimeLeftSec(timeLeft);
            else
                needInit = true;
        }
        if (needInit)
            initOnlineTasks();
    }


}

