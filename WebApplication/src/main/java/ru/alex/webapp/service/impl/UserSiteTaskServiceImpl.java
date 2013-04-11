package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.SiteTaskDao;
import ru.alex.webapp.dao.UserActionDao;
import ru.alex.webapp.dao.UserSiteTaskDao;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.dao.UserTaskTimeSeqDao;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.SiteTask;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.UserAction;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.UserTaskTimeSeq;
import ru.alex.webapp.model.enums.Action;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.SiteTaskService;
import ru.alex.webapp.service.UserSiteTaskService;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserSiteTaskServiceImpl extends GenericServiceImpl<UserSiteTask, Long> implements UserSiteTaskService {
    private static final Logger logger = LoggerFactory.getLogger(UserSiteTaskServiceImpl.class);
    @Autowired
    UserSiteTaskDao userSiteTaskDao;
    @Autowired
    UserTaskTimeDao userTaskTimeDao;
    @Autowired
    SiteTaskDao siteTaskDao;
    @Autowired
    SiteTaskService siteTaskService;
    @Autowired
    UserTaskTimeSeqDao userTaskTimeSeqDao;
    @Autowired
    UserActionDao userActionDao;

    @Override
    protected GenericDao<UserSiteTask, Long> getDao() {
        return userSiteTaskDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(UserSiteTask entity) throws Exception {
        logger.debug("update userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());
        userSiteTaskDao.merge(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(UserSiteTask entity) throws Exception {
        logger.debug("add userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        if (entity.getSiteTask() == null || entity.getUserByUsername() == null
                || entity.getStatus() == null || entity.getUpdateTime() == null
                || entity.getCreateTime() == null || entity.getDeleted() == null)
            throw new Exception("Wrong params");
        entity.setDeleted(false);
        userSiteTaskDao.persist(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(UserSiteTask entity) throws Exception {
        logger.debug("remove userSiteTask={}", entity);
        if (entity == null)
            throw new IllegalArgumentException("Wrong entity");
        throwExceptionIfNotExists(entity.getId());

        Map<String, Object> params = new HashMap<>(1);
        params.put("userSiteTaskId", entity.getId());
        Collection<UserTaskTime> userTaskTimeList = userTaskTimeDao.findWithNamedQuery(UserTaskTime.BY_USER_SITE_TASK, params);
        logger.debug("remove BY_USER_SITE_TASK userTaskTimeList={}", userTaskTimeList);
        if (userTaskTimeList == null || userTaskTimeList.size() == 0) {
            entity = userSiteTaskDao.merge(entity);
            entity.getSiteTask().getUserSiteTaskList().remove(entity);
            entity.getUserByUsername().getUserSiteTasksByUsername().remove(entity);
            userSiteTaskDao.remove(entity);
            userSiteTaskDao.flush();
        } else {
            if (entity.getCurrentTime() != null)
                throw new Exception("Can't remove userSiteTask, has current time");
            entity.setDeleted(true);
            userSiteTaskDao.merge(entity);
        }
    }

    @Override
    public List<UserSiteTask> getAllCurrentTime() throws Exception {
        List<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.ALL_CURRENT_TIME);
        logger.debug("getAllCurrentTime userSiteTaskList={}", userSiteTaskList);
        return userSiteTaskList;
    }

    @Override
    public List<UserSiteTask> getAllCurrentTime(Site site) throws Exception {
        logger.debug("getAllCurrentTime site={}", site);
        if (site == null || site.getId() == null)
            throw new IllegalArgumentException("Wrong site");
        Map<String, Object> params = new HashMap<>(1);
        params.put("siteId", site.getId());
        List<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.ALL_CURRENT_TIME_BY_SITE, params);
        logger.debug("getAllCurrentTime userSiteTaskList={}", userSiteTaskList);
        return userSiteTaskList;
    }

    @Override
    public List<UserSiteTask> getNotDeletedUserSiteTasks(Site site, Task task) throws Exception {
        logger.debug("getNotDeletedUserSiteTasks site={}, task={}", site, task);
        if (site == null || site.getId() == null)
            throw new IllegalArgumentException("Wrong site");
        if (task == null || task.getId() == null)
            throw new IllegalArgumentException("Wrong task");
        Map<String, Object> params = new HashMap<>(2);
        params.put("siteId", site.getId());
        params.put("taskId", task.getId());
        List<SiteTask> siteTaskList = siteTaskDao.findWithNamedQuery(SiteTask.BY_SITE_TASK_NOT_DELETED, params);
        logger.debug("getNotDeletedUserSiteTasks siteTaskList={}", siteTaskList);
        if (siteTaskList == null || siteTaskList.size() == 0)
            throw new Exception("Can't find SiteTask with siteId=" + site.getId() + ", taskId=" + task.getId());
        SiteTask siteTask = siteTaskList.get(0);
        params = new HashMap<>(1);
        params.put("siteTaskId", siteTask.getId());
        List<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.ALL_NOT_DELETED_BY_SITE_TASK, params);
        logger.debug("getNotDeletedUserSiteTasks userSiteTaskList={}", userSiteTaskList);
        return userSiteTaskList;
    }

    @Override
    public List<UserSiteTask> getNotDeletedUserSiteTasks(Site site, User user) throws Exception {
        logger.debug("getNotDeletedUserSiteTasks site={}, user={}", site, user);
        if (site == null || site.getId() == null)
            throw new IllegalArgumentException("Wrong site");
        if (user == null || user.getUsername() == null || user.getUsername().equals(""))
            throw new IllegalArgumentException("Wrong user");
        Map<String, Object> params = new HashMap<>(2);
        params.put("siteId", site.getId());
        params.put("username", user.getUsername());
        List<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.ALL_NOT_DELETED_BY_SITE_USER, params);
        logger.debug("getNotDeletedUserSiteTasks userSiteTaskList={}", userSiteTaskList);
        return userSiteTaskList;
    }

    @Override
    public UserSiteTask getByUserSiteTask(User user, Site site, Task task) throws Exception {
        logger.debug("getByUserSiteTask user={}, site={}, task={}", user, site, task);
        if (user == null || user.getUsername() == null || user.getUsername().equals(""))
            throw new IllegalArgumentException("Wrong user");
        if (site == null || site.getId() == null)
            throw new IllegalArgumentException("Wrong site");
        if (task == null || task.getId() == null)
            throw new IllegalArgumentException("Wrong task");
        Map<String, Object> params = new HashMap<>(3);
        params.put("username", user.getUsername());
        params.put("siteId", site.getId());
        params.put("taskId", task.getId());
        List<UserSiteTask> userSiteTaskList = userSiteTaskDao.findWithNamedQuery(UserSiteTask.BY_USER_SITE_TASK, params);
        logger.debug("getByUserSiteTask userSiteTaskList={}", userSiteTaskList);
        return userSiteTaskList == null || userSiteTaskList.size() == 0 ? null : userSiteTaskList.get(0);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void addUserSiteTask(User user, Site site, Task task) throws Exception {
        logger.debug("addUserSiteTask user={}, site={}, task={}", user, site, task);
        SiteTask siteTask = siteTaskService.getBySiteTask(site, task);
        logger.debug("addUserSiteTask siteTask={}", siteTask);
        if (siteTask == null)
            throw new Exception("Site task doesn't exist with siteId=" + site.getId() + " taskId=" + task.getId());
        UserSiteTask userSiteTask = getByUserSiteTask(user, site, task);
        Date now = new Date();
        if (userSiteTask == null) {
            userSiteTask = new UserSiteTask();
            userSiteTask.setSiteTask(siteTask);
            userSiteTask.setUserByUsername(user);
            userSiteTask.setUpdateTime(now);
            userSiteTask.setCreateTime(now);
            userSiteTask.setStatus(TaskStatus.UNKNOWN.getStatusStr());
            userSiteTask.setDeleted(false);
            add(userSiteTask);
        } else {
            if (userSiteTask.getDeleted()) {
                userSiteTask.setUpdateTime(now);
                userSiteTask.setDeleted(true);
                userSiteTaskDao.merge(userSiteTask);
            } else {
                throw new Exception("Can't add, userSiteTask exists already with siteId=" + site.getId() + ", taskId=" + task.getId() + ", username" + user.getUsername());
            }
        }
    }

    @Override
    public void removeUserSiteTask(User user, Site site, Task task) throws Exception {
        logger.debug("removeUserSiteTask user={}, site={}, task={}", user, site, task);
        UserSiteTask userSiteTask = getByUserSiteTask(user, site, task);
        if (userSiteTask == null || userSiteTask.getDeleted()) {
            throw new Exception("Can't remove, userSiteTask is removed, siteId=" + site.getId() + ", taskId=" + task.getId() + ", username" + user.getUsername());
        }
        remove(userSiteTask);
    }

    @Override
    @Scheduled(fixedDelay = 1000)
    public void checkAllTasks() {
        //logger.debug("checkAllTasks");
        try {
            List<UserSiteTask> runningTasks = getAllCurrentTime();
            for (UserSiteTask task : runningTasks) {
                checkTask(task);
            }
        } catch (Exception ex) {
            logger.error("checkAllTasks " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void startTask(UserSiteTask task, BigDecimal customPrice) throws Exception {
        logger.debug("startTask task={}, customPrice={}", task, customPrice);
        if (task == null)
            throw new IllegalArgumentException("task is null");
        TaskType taskType = TaskType.getType(task.getSiteTask().getTaskByTaskId().getType());
        if (taskType == TaskType.PROCESS)
            throw new Exception("Can't start Process, only task");
        if (taskType == TaskType.TASK_CUSTOM_PRICE && (customPrice == null || customPrice.compareTo(new BigDecimal(0)) < 0))
            throw new IllegalArgumentException("wrong custom price");
        TaskStatus taskStatus = TaskStatus.getStatus(task.getStatus());
        if (taskStatus != TaskStatus.COMPLETED && taskStatus != TaskStatus.STOPPED && taskStatus != TaskStatus.UNKNOWN)
            throw new Exception("Can't start task with status " + taskStatus.getStatusFormatted());
        if (task.getCurrentTime() != null)
            throw new Exception("Task can't have current time");
        if (task.getUserByUsername().getCurrentChange() == null)
            throw new Exception("Can't start task without change");

        Date now = new Date();

        //change user_task status
        task.setStatus(TaskStatus.COMPLETED.getStatusStr());
        task.setUpdateTime(now);
        task = userSiteTaskDao.merge(task);

        //add user_task_time
        UserTaskTime userTaskTime = new UserTaskTime();
        userTaskTime.setDurationPlaySec(0);
        userTaskTime.setFinishTimePlay(now);
        userTaskTime.setStartTime(now);
        userTaskTime.setFinishTime(now);
        if (taskType == TaskType.TASK_CUSTOM_PRICE) {
            userTaskTime.setPriceHour(customPrice);
            userTaskTime.setTotal(customPrice);
        } else {
            userTaskTime.setPriceHour(task.getSiteTask().getTaskByTaskId().getPriceHour());
            userTaskTime.setTotal(task.getSiteTask().getTaskByTaskId().getPriceHour());
        }
        userTaskTime.setUserChange(task.getUserByUsername().getCurrentChange());
        task.addUserTaskTime(userTaskTime);
        userTaskTimeDao.persist(userTaskTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user_action
        addUserAction(Action.START, now, userTaskTime);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void startProcess(UserSiteTask task, int seconds, Action action) throws Exception {
        logger.debug("startProcess task={}, seconds={}, action={}", task, seconds, action);
        if (task == null)
            throw new IllegalArgumentException("task is null");
        TaskType taskType = TaskType.getType(task.getSiteTask().getTaskByTaskId().getType());
        if (taskType != TaskType.PROCESS)
            throw new Exception("Can't start Task, only process");
        if (seconds <= 0)
            throw new IllegalArgumentException("wrong seconds param: " + seconds);
        if (action != Action.START && action != Action.CUSTOM1 && action != Action.CUSTOM2 && action != Action.CUSTOM3)
            throw new IllegalArgumentException("wrong action param: " + action.getActionFormatted());
        TaskStatus taskStatus = TaskStatus.getStatus(task.getStatus());
        if (taskStatus != TaskStatus.COMPLETED && taskStatus != TaskStatus.STOPPED && taskStatus != TaskStatus.UNKNOWN)
            throw new Exception("Can't start process with status " + taskStatus.getStatusFormatted());
        if (task.getCurrentTime() != null)
            throw new Exception("Process can't have current time on start");
        if (task.getUserByUsername().getCurrentChange() == null)
            throw new Exception("Can't start process without change");

        Date now = new Date();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND, seconds);

        TaskStatus newTaskStatus = null;
        switch (action) {
            case START:
                newTaskStatus = TaskStatus.RUNNING;
                break;
            case CUSTOM1:
                newTaskStatus = TaskStatus.CUSTOM1;
                break;
            case CUSTOM2:
                newTaskStatus = TaskStatus.CUSTOM2;
                break;
            case CUSTOM3:
                newTaskStatus = TaskStatus.CUSTOM3;
                break;
        }

        //change user_task status
        task.setStatus(newTaskStatus.getStatusStr());
        task.setUpdateTime(now);
        task = userSiteTaskDao.merge(task);

        //add user_task_time
        UserTaskTime userTaskTime = new UserTaskTime();
        if (action == Action.START) {
            userTaskTime.setDurationPlaySec(seconds);
            userTaskTime.setFinishTimePlay(endTime.getTime());
        } else if (action == Action.CUSTOM1) {
            userTaskTime.setDurationPlaySec(0);
            userTaskTime.setFinishTimePlay(endTime.getTime());
            userTaskTime.setDurationCustom1Sec(seconds);
            userTaskTime.setFinishTimeCustom1(endTime.getTime());
        } else if (action == Action.CUSTOM2) {
            userTaskTime.setDurationPlaySec(0);
            userTaskTime.setFinishTimePlay(endTime.getTime());
            userTaskTime.setDurationCustom2Sec(seconds);
            userTaskTime.setFinishTimeCustom2(endTime.getTime());
        } else if (action == Action.CUSTOM3) {
            userTaskTime.setDurationPlaySec(0);
            userTaskTime.setFinishTimePlay(endTime.getTime());
            userTaskTime.setDurationCustom3Sec(seconds);
            userTaskTime.setFinishTimeCustom3(endTime.getTime());
        }
        userTaskTime.setStartTime(now);
        userTaskTime.setPriceHour(task.getSiteTask().getTaskByTaskId().getPriceHour());
        userTaskTime.setUserChange(task.getUserByUsername().getCurrentChange());
        task.addUserTaskTime(userTaskTime);
        task.setCurrentTime(userTaskTime);

        //add user_task_time_seq
        UserTaskTimeSeq timeSeq = new UserTaskTimeSeq();
        timeSeq.setStartTime(now);
        timeSeq.setUserTaskTime(userTaskTime);
        timeSeq.setTaskStatus(newTaskStatus.getStatusStr());
        userTaskTime.setTimeSeq(timeSeq);

        userTaskTimeDao.persist(userTaskTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user_action
        addUserAction(Action.START, now, userTaskTime);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    //running->custom
    public void switchProcess(UserSiteTask task, int seconds, Action action) throws Exception {
        logger.debug("switchProcess task={}, seconds={}, action={}", task, seconds, action);
        if (task == null)
            throw new IllegalArgumentException("task is null");
        TaskType taskType = TaskType.getType(task.getSiteTask().getTaskByTaskId().getType());
        if (taskType != TaskType.PROCESS)
            throw new Exception("Can't start Task, only process");
        if (seconds <= 0)
            throw new IllegalArgumentException("wrong seconds param: " + seconds);
        if (action != Action.CUSTOM1 && action != Action.CUSTOM2 && action != Action.CUSTOM3)
            throw new IllegalArgumentException("wrong action param: " + action.getActionFormatted());
        TaskStatus taskStatus = TaskStatus.getStatus(task.getStatus());
        if (taskStatus != TaskStatus.RUNNING)
            throw new Exception("Can't switch process with status " + taskStatus.getStatusFormatted());
        UserTaskTime currentTime = task.getCurrentTime();
        if (currentTime == null)
            throw new Exception("Process should have current time on start");

        Date now = new Date();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND, seconds);

        //get Time Seq in reverse order
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("endTask timeSeqList={}", timeSeqList);
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        UserTaskTimeSeq currentTimeSeq = timeSeqList.get(0);
        TaskStatus currentTimeSeqStatus = TaskStatus.getStatus(currentTimeSeq.getTaskStatus());
        if (currentTimeSeqStatus != TaskStatus.RUNNING)
            throw new Exception("last timeSeq should have status RUNNING");

        //change user_task status
        TaskStatus newTaskStatus = null;
        switch (action) {
            case CUSTOM1:
                newTaskStatus = TaskStatus.CUSTOM1;
                break;
            case CUSTOM2:
                newTaskStatus = TaskStatus.CUSTOM2;
                break;
            case CUSTOM3:
                newTaskStatus = TaskStatus.CUSTOM3;
                break;
        }
        task.setStatus(newTaskStatus.getStatusStr());
        task.setUpdateTime(now);
        task = userSiteTaskDao.merge(task);
        userSiteTaskDao.flush();
        userSiteTaskDao.clear();

        //change duration_custom
        if (action == Action.CUSTOM1) {
            currentTime.setDurationCustom1Sec(seconds);
            currentTime.setFinishTimeCustom1(endTime.getTime());
        } else if (action == Action.CUSTOM2) {
            currentTime.setDurationCustom2Sec(seconds);
            currentTime.setFinishTimeCustom2(endTime.getTime());
        } else if (action == Action.CUSTOM3) {
            currentTime.setDurationCustom3Sec(seconds);
            currentTime.setFinishTimeCustom3(endTime.getTime());
        }
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();


        //set the last time_seq end_time value
        currentTimeSeq = userTaskTimeSeqDao.findById(currentTimeSeq.getId());
        currentTimeSeq.setEndTime(now);
        //add user_task_time_seq to the end of current user_task_time with status running
        UserTaskTimeSeq timeSeq = new UserTaskTimeSeq();
        timeSeq.setTaskStatus(newTaskStatus.getStatusStr());
        timeSeq.setStartTime(now);
        timeSeq.setPrevTimeSeq(currentTimeSeq);
        currentTimeSeq.setNextTimeSeq(timeSeq);
        userTaskTimeSeqDao.merge(currentTimeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        //add user_action
        addUserAction(action, now, currentTime);
    }

    @Override
    //custom -> running
    public void resumeProcess(UserSiteTask task) throws Exception {
        logger.debug("resumeProcess task={}", task);
        if (task == null)
            throw new IllegalArgumentException("task is null");
        TaskType taskType = TaskType.getType(task.getSiteTask().getTaskByTaskId().getType());
        if (taskType != TaskType.PROCESS)
            throw new Exception("Can't start Task, only process");
        TaskStatus taskStatus = TaskStatus.getStatus(task.getStatus());
        if (taskStatus != TaskStatus.CUSTOM1 && taskStatus != TaskStatus.CUSTOM2 && taskStatus != TaskStatus.CUSTOM3)
            throw new Exception("Can't only Custom process can be processed: " + taskStatus.getStatusFormatted());
        if (task.getCurrentTime() == null)
            throw new Exception("Process should have current time on resume");
        //force ending custom
        endCustom(task, new Date(), true);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    //custom->custom
    public void switchCustom(UserSiteTask task, int seconds, Action action) throws Exception {
        logger.debug("switchCustom task={}, seconds={}, action={}", task, seconds, action);
        if (task == null)
            throw new IllegalArgumentException("task is null");
        TaskType taskType = TaskType.getType(task.getSiteTask().getTaskByTaskId().getType());
        if (taskType != TaskType.PROCESS)
            throw new Exception("Can't start Task, only process");
        if (seconds <= 0)
            throw new IllegalArgumentException("wrong seconds param: " + seconds);
        if (action != Action.CUSTOM1 && action != Action.CUSTOM2 && action != Action.CUSTOM3)
            throw new IllegalArgumentException("wrong action param: " + action.getActionFormatted());
        TaskStatus taskStatus = TaskStatus.getStatus(task.getStatus());
        if (taskStatus != TaskStatus.CUSTOM1 && taskStatus != TaskStatus.CUSTOM2 && taskStatus != TaskStatus.CUSTOM3)
            throw new Exception("Can't switchCustom process with status " + taskStatus.getStatusFormatted());
        if (task.getCurrentTime() == null)
            throw new Exception("Process should have current time on resume");

        throw new UnsupportedOperationException("switching between custom tasks isn't permited!");
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void extendProcess(UserSiteTask task, int seconds) throws Exception {
        logger.debug("extendProcess task={}, seconds={}", task, seconds);
        if (task == null)
            throw new IllegalArgumentException("task is null");
        TaskType taskType = TaskType.getType(task.getSiteTask().getTaskByTaskId().getType());
        if (taskType != TaskType.PROCESS)
            throw new Exception("Can't extend Task, only process");
        if (seconds <= 0)
            throw new IllegalArgumentException("wrong seconds param: " + seconds);
        TaskStatus taskStatus = TaskStatus.getStatus(task.getStatus());
        if (taskStatus != TaskStatus.RUNNING && taskStatus != TaskStatus.CUSTOM1 && taskStatus != TaskStatus.CUSTOM2 && taskStatus != TaskStatus.CUSTOM3)
            throw new Exception("Can't extend process with status " + taskStatus.getStatusFormatted());
        UserTaskTime currentTime = task.getCurrentTime();
        if (currentTime == null)
            throw new Exception("Process should have current time on extend");


        Date now = new Date();

        //change user_task status no needed, has RUNNING or CUSTOM status
        //extend finish_time of user_task_time and duration
        Calendar finisTime = Calendar.getInstance();
        switch (taskStatus) {
            case RUNNING:
                finisTime.setTime(currentTime.getFinishTimePlay());
                finisTime.add(Calendar.SECOND, seconds);
                currentTime.setDurationPlaySec(currentTime.getDurationPlaySec() + seconds);
                currentTime.setFinishTimePlay(finisTime.getTime());
                break;
            case CUSTOM1:
                finisTime.setTime(currentTime.getFinishTimeCustom1());
                finisTime.add(Calendar.SECOND, seconds);
                currentTime.setDurationCustom1Sec(currentTime.getDurationCustom1Sec() + seconds);
                currentTime.setFinishTimeCustom1(finisTime.getTime());
                break;
            case CUSTOM2:
                finisTime.setTime(currentTime.getFinishTimeCustom2());
                finisTime.add(Calendar.SECOND, seconds);
                currentTime.setDurationCustom2Sec(currentTime.getDurationCustom2Sec() + seconds);
                currentTime.setFinishTimeCustom2(finisTime.getTime());
                break;
            case CUSTOM3:
                finisTime.setTime(currentTime.getFinishTimeCustom3());
                finisTime.add(Calendar.SECOND, seconds);
                currentTime.setDurationCustom3Sec(currentTime.getDurationCustom3Sec() + seconds);
                currentTime.setFinishTimeCustom3(finisTime.getTime());
                break;
        }
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user action
        addUserAction(Action.EXTEND, now, currentTime);
    }

    @Override
    public void stopProcess(UserSiteTask task) throws Exception {
        logger.debug("stopProcess task={}", task);
        if (task == null)
            throw new IllegalArgumentException("task is null");
        TaskType taskType = TaskType.getType(task.getSiteTask().getTaskByTaskId().getType());
        if (taskType != TaskType.PROCESS)
            throw new Exception("Can't extend Task, only process");
        TaskStatus taskStatus = TaskStatus.getStatus(task.getStatus());
        if (taskStatus != TaskStatus.RUNNING && taskStatus != TaskStatus.CUSTOM1 && taskStatus != TaskStatus.CUSTOM2 && taskStatus != TaskStatus.CUSTOM3)
            throw new Exception("Can't stop process with status " + taskStatus.getStatusFormatted());
        UserTaskTime currentTime = task.getCurrentTime();
        if (currentTime == null)
            throw new Exception("Process should have current time on stop");

        if (taskStatus == TaskStatus.RUNNING) {
            //force ending task
            endTask(task, new Date(), true);
        } else {
            //force ending custom
            endCustom(task, new Date(), true);
        }
    }

    /**
     * Check if task need to be ended
     *
     * @param task
     * @return if task was ended
     * @throws Exception
     */
    private boolean checkTask(UserSiteTask task) throws Exception {
        logger.debug("checkTask task={}", task);
        if (task == null)
            throw new IllegalArgumentException("UserSiteTask is null");
        TaskStatus status = TaskStatus.getStatus(task.getStatus());
        UserTaskTime currentTime = task.getCurrentTime();
        if (status == TaskStatus.RUNNING || status == TaskStatus.CUSTOM1 || status == TaskStatus.CUSTOM2 || status == TaskStatus.CUSTOM3) {
            if (currentTime == null)
                throw new Exception("Running and Custom status task should have current time");
            Date now = new Date();
            switch (status) {
                case RUNNING:
                    if (now.after(currentTime.getFinishTimePlay()))
                        endTask(task, currentTime.getFinishTimePlay(), false);
                    break;
                case CUSTOM1:
                    if (now.after(currentTime.getFinishTimeCustom1()))
                        endCustom(task, currentTime.getFinishTimeCustom1(), false);
                    break;
                case CUSTOM2:
                    if (now.after(currentTime.getFinishTimeCustom2()))
                        endCustom(task, currentTime.getFinishTimeCustom2(), false);
                    break;
                case CUSTOM3:
                    if (now.after(currentTime.getFinishTimeCustom3()))
                        endCustom(task, currentTime.getFinishTimeCustom3(), false);
                    break;
            }
            return true;
        } else if (currentTime != null) {
            throw new Exception("Current time should be only for running and custom status task");
        }
        return false;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    private void endTask(UserSiteTask task, Date finishTime, boolean stop) throws Exception {
        logger.debug("entTask task={}, finishTime={}, stop={}", task, finishTime, stop);
        if (task == null)
            throw new IllegalArgumentException("UserSiteTask is null");
        UserTaskTime currentTime = task.getCurrentTime();
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("Can't end process without current time and without timeSeq");
        if (TaskStatus.getStatus(task.getStatus()) != TaskStatus.RUNNING)
            throw new Exception("Only running process can be finished");

        task = userSiteTaskDao.merge(task);
        currentTime = task.getCurrentTime();
        userSiteTaskDao.lock(task, LockModeType.PESSIMISTIC_WRITE);
        userTaskTimeDao.lock(currentTime, LockModeType.PESSIMISTIC_WRITE);

        //get Time Seq in reverse order
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("endTask timeSeqList={}", timeSeqList);
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        UserTaskTimeSeq currentTimeSeq = timeSeqList.get(0);
        if (TaskStatus.getStatus(currentTimeSeq.getTaskStatus()) != TaskStatus.RUNNING)
            throw new Exception("last timeSeq should have status RUNNING");

        //set the last time_seq end_time value
        currentTimeSeq = userTaskTimeSeqDao.findById(currentTimeSeq.getId());
        currentTimeSeq.setEndTime(finishTime);
        currentTimeSeq = userTaskTimeSeqDao.merge(currentTimeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();
        timeSeqList.set(0, currentTimeSeq);

        //change user_site_task status and remove current_time
        task.setStatus(stop ? TaskStatus.STOPPED.getStatusStr() : TaskStatus.COMPLETED.getStatusStr());
        task.setUpdateTime(finishTime);
        task.setCurrentTime(null);
        task = userSiteTaskDao.merge(task);
        userSiteTaskDao.flush();
        userSiteTaskDao.clear();

        //change user_tak_time duration
        if (stop) {
            int timeSpentSec = 0;
            //timeSeqList is updated
            for (UserTaskTimeSeq timeSeq : timeSeqList) {
                if (timeSeq.getEndTime() == null || timeSeq.getEndTime().before(timeSeq.getStartTime()))
                    throw new Exception("time sequence should have correct end time");
                if (TaskStatus.getStatus(timeSeq.getTaskStatus()) == TaskStatus.RUNNING) {
                    long timeDif = (timeSeq.getEndTime().getTime() - timeSeq.getStartTime().getTime()) / 1000;
                    timeSpentSec += timeDif;
                }
            }
            currentTime.setDurationPlaySec(timeSpentSec);
        }

        //set user_tak_time finishTime
        BigDecimal total = currentTime.getPriceHour().multiply(new BigDecimal((double) currentTime.getDurationPlaySec() / 3600));
        currentTime.setTotal(total);
        currentTime.setFinishTime(finishTime);
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user_action
        addUserAction(stop ? Action.STOP : Action.FINISH, finishTime, currentTime);
    }

    //end custom and resume to play
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    private void endCustom(UserSiteTask task, Date finishTime, boolean force) throws Exception {
        logger.debug("endCustom task={}, finishTime={}, force={}", task, finishTime, force);
        if (task == null)
            throw new IllegalArgumentException("UserSiteTask is null");
        UserTaskTime currentTime = task.getCurrentTime();
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("Can't end process without current time and without timeSeq");
        TaskStatus customTaskStatus = TaskStatus.getStatus(task.getStatus());
        if (customTaskStatus != TaskStatus.CUSTOM1 && customTaskStatus != TaskStatus.CUSTOM2 && customTaskStatus != TaskStatus.CUSTOM3)
            throw new Exception("Only Custom process can be finished");

        task = userSiteTaskDao.merge(task);
        currentTime = task.getCurrentTime();
        userSiteTaskDao.lock(task, LockModeType.PESSIMISTIC_WRITE);
        userTaskTimeDao.lock(currentTime, LockModeType.PESSIMISTIC_WRITE);

        //get Time Seq in reverse order
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("endTask timeSeqList={}", timeSeqList);
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        UserTaskTimeSeq currentTimeSeq = timeSeqList.get(0);
        TaskStatus currentTimeSeqStatus = TaskStatus.getStatus(currentTimeSeq.getTaskStatus());
        if (currentTimeSeqStatus != TaskStatus.CUSTOM1 && currentTimeSeqStatus != TaskStatus.CUSTOM2 && currentTimeSeqStatus != TaskStatus.CUSTOM3)
            throw new Exception("last timeSeq should have status CUSTOM");

        //set the last time_seq end_time value
        currentTimeSeq = userTaskTimeSeqDao.findById(currentTimeSeq.getId());
        currentTimeSeq.setEndTime(finishTime);
        currentTimeSeq = userTaskTimeSeqDao.merge(currentTimeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();
        timeSeqList.set(0, currentTimeSeq);

        //calculate new finish_time_now for user_task_time
        int timeSpentSec = 0;
        int timeSpentCustomSec = 0;
        for (UserTaskTimeSeq ts : timeSeqList) {
            if (ts.getEndTime() == null || ts.getEndTime().before(ts.getStartTime()))
                throw new Exception("time sequence should have correct end time");
            long seqDif = (ts.getEndTime().getTime() - ts.getStartTime().getTime()) / 1000;
            TaskStatus timeSeqStatus = TaskStatus.getStatus(ts.getTaskStatus());
            if (timeSeqStatus == TaskStatus.RUNNING) {
                timeSpentSec += seqDif;
            } else if (timeSeqStatus == customTaskStatus) {
                timeSpentCustomSec += seqDif;
            }
        }
        int secondToAdd = currentTime.getDurationPlaySec() - timeSpentSec;
        Calendar newFinishTimePlay = Calendar.getInstance();
        newFinishTimePlay.setTime(finishTime);
        newFinishTimePlay.add(Calendar.SECOND, secondToAdd);

        //change user_site_task status
        task.setStatus(TaskStatus.RUNNING.getStatusStr());
        task.setUpdateTime(finishTime);
        task = userSiteTaskDao.merge(task);
        userSiteTaskDao.flush();
        userSiteTaskDao.clear();

        //change duration_custom
        if (force) {
            if (customTaskStatus == TaskStatus.CUSTOM1)
                currentTime.setDurationCustom1Sec(timeSpentCustomSec);
            else if (customTaskStatus == TaskStatus.CUSTOM2)
                currentTime.setDurationCustom2Sec(timeSpentCustomSec);
            else if (customTaskStatus == TaskStatus.CUSTOM3)
                currentTime.setDurationCustom3Sec(timeSpentCustomSec);
        }

        //set user_tak_time new finishTimePlay
        currentTime.setFinishTimePlay(newFinishTimePlay.getTime());
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user_task_time_seq to the end of current user_task_time with status running
        UserTaskTimeSeq timeSeq = new UserTaskTimeSeq();
        timeSeq.setTaskStatus(TaskStatus.RUNNING.getStatusStr());
        timeSeq.setStartTime(finishTime);
        UserTaskTimeSeq prevTimeSeq = userTaskTimeSeqDao.findById(currentTimeSeq.getId());
        timeSeq.setPrevTimeSeq(prevTimeSeq);
        prevTimeSeq.setNextTimeSeq(timeSeq);
        userTaskTimeSeqDao.merge(prevTimeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        //add user_action
        addUserAction(Action.RESUME, finishTime, currentTime);
    }

    /**
     * Returns list of timeSeq for userTaskStatus in reverse order
     * Last timeSeq is the first
     *
     * @param timeSeq
     * @return
     * @throws Exception
     */
    private List<UserTaskTimeSeq> getAllTimeSeq(UserTaskTimeSeq timeSeq) throws Exception {
        logger.debug("getAllTimeSeq timeSeq={}", timeSeq);
        List<UserTaskTimeSeq> nextTimeSeqList = null;
        if (timeSeq.equals(timeSeq.getNextTimeSeq()))
            throw new Exception("Unlimited recursion in getAllTimeSeq for task");

        if (timeSeq.getNextTimeSeq() != null)
            nextTimeSeqList = getAllTimeSeq(timeSeq.getNextTimeSeq());

        if (nextTimeSeqList != null && nextTimeSeqList.contains(timeSeq))
            throw new Exception("Unlimited recursion in getAllTimeSeq for task");

        List<UserTaskTimeSeq> timeSeqList = new ArrayList<>();
        if (nextTimeSeqList != null)
            timeSeqList.addAll(nextTimeSeqList);
        timeSeqList.add(timeSeq);
        return timeSeqList;
    }

    private void addUserAction(Action action, Date date, UserTaskTime userTaskTime) {
        UserAction userAction = new UserAction();
        userAction.setTimestamp(date);
        userAction.setAction(action.getActionStr());
        userTaskTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();

    }

}
