package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.UserTaskWrapper;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.service.TaskService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private static final Logger logger = Logger.getLogger(OnlineTaskMB.class);
    @Autowired
    private TaskService taskService;
    private List<UserTaskWrapper> onlineTasks;
    private int updateIntervalSec = 5;

    @PostConstruct
    private void init() {
        logger.debug("init");
        initOnlineTasks();
    }

    private void initOnlineTasks() {
        logger.debug("initOnlineTasks");
        try {
            List<UserTask> tasks = taskService.getOnlineTasks();
            logger.debug("tasks=" + tasks);
            List<UserTaskWrapper> taskWrappers = new ArrayList<UserTaskWrapper>(tasks.size());
            for (UserTask ut : tasks) {
                UserTaskTime currentTime = taskService.getCurrentTimeForUserTask(ut.getTaskByTaskId().getId(), ut.getUserByUsername().getUsername());
                logger.debug("currentTime=" + currentTime);
                int timeSpent = taskService.getTimeSpentForUserTask(ut.getTaskByTaskId().getId(), ut.getUserByUsername().getUsername());
                logger.debug("timeSpent=" + timeSpent);
                taskWrappers.add(new UserTaskWrapper(ut, currentTime, timeSpent));
            }
            onlineTasks = taskWrappers;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in initialization of tasks", e.toString()));
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
            int timeLeft = task.getTimeLeft() - updateIntervalSec;
            if (timeLeft > 0)
                task.setTimeLeft(timeLeft);
            else
                needInit = true;
        }
        if (needInit)
            initOnlineTasks();
    }


}

