package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.service.OnlineTaskService;
import ru.alex.webapp.service.UserSiteTaskService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Alex
 */
@Service
@Scope("singleton") //could be omitted, default is singleton
public class OnlineTaskServiceImpl implements OnlineTaskService {
    private static final Logger logger = LoggerFactory.getLogger(OnlineTaskServiceImpl.class);
    @Autowired
    UserSiteTaskService userSiteTaskService;

    private List<UserSiteTask> runningTasks;

    @PostConstruct
    public void init() {
        try {
            updateTasksStatus();
        } catch (Exception ex) {
            logger.error("init " + ex.getMessage(), ex);
        }

    }

    @Override
    @Scheduled(fixedDelay = 1000)
    synchronized public void checkAllTasks() {
        //logger.debug("checkAllTasks");
        try {
            for (UserSiteTask task : runningTasks) {
                userSiteTaskService.checkTask(task);
            }
        } catch (Exception ex) {
            logger.error("checkAllTasks " + ex.getMessage(), ex);
        }
    }


    /**
     * Update tasks from DB. (Can be used as AOP)
     * Is called, when user_site_task status is changed from userSiteTaskService:
     * startProcess, startTasks
     * endCustom, endTask,
     * extendProcess, switchProcess
     *
     * @throws Exception
     */
    @Override
    synchronized public void updateTasksStatus() throws Exception {
        logger.debug("updateTasksStatus");
        runningTasks = userSiteTaskService.getAllCurrentTime();
        checkAllTasks();
    }
}
