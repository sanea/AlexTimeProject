package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.service.OnlineTaskService;
import ru.alex.webapp.service.UserSiteTaskService;

import java.util.List;

/**
 * @author Alex
 */
@Service
@Scope("singleton")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
//TODO make stateful, db request only when task_time changes
public class OnlineTaskServiceImpl implements OnlineTaskService {
    private static final Logger logger = LoggerFactory.getLogger(OnlineTaskServiceImpl.class);
    @Autowired
    UserSiteTaskService userSiteTaskService;

    @Override
    @Scheduled(fixedDelay = 1000)
    public void checkAllTasks() {
        //logger.debug("checkAllTasks");
        try {
            List<UserSiteTask> runningTasks = userSiteTaskService.getAllCurrentTime();
            for (UserSiteTask task : runningTasks) {
                userSiteTaskService.checkTask(task);
            }
        } catch (Exception ex) {
            logger.error("checkAllTasks " + ex.getMessage(), ex);
        }
    }
}
