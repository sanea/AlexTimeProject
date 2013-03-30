package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.*;
import ru.alex.webapp.model.*;
import ru.alex.webapp.model.enums.Action;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.service.SiteTaskService;
import ru.alex.webapp.service.UserSiteTaskService;

import java.math.BigDecimal;
import java.util.*;

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
        logger.debug("getAllCurrentTime getAllCurrentTime userSiteTaskList={}", userSiteTaskList);
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
        logger.debug("checkAllTasks");
        try {
            List<UserSiteTask> runningTasks = getAllCurrentTime();
            logger.debug("checkAllTasks runningTasks={}", runningTasks);
            for (UserSiteTask task : runningTasks) {
                checkTask(task);
            }
        } catch (Exception ex) {
            logger.error("checkAllTasks " + ex.getMessage(), ex);
        }
    }

    @Override
    public void startTask(UserSiteTask userSiteTask, BigDecimal customPrice) throws Exception {
        //TODO implement Method
    }

    @Override
    public void startProcess(UserSiteTask userSiteTask, int seconds, Action action) throws Exception {
        //TODO implement Method
    }

    @Override
    public void extendProcess(UserSiteTask userSiteTask, int seconds) throws Exception {
        //TODO implement Method
    }

    @Override
    public void stopProcess(UserSiteTask userSiteTask) throws Exception {
        //TODO implement Method
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
                        entTask(task, currentTime.getFinishTimePlay(), false);
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
    private void entTask(UserSiteTask task, Date finishTime, boolean stop) throws Exception {
        logger.debug("entTask task={}, finishTime={}, stop={}", task, finishTime, stop);
        if (task == null)
            throw new IllegalArgumentException("UserSiteTask is null");
        UserTaskTime currentTime = task.getCurrentTime();
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("Can't end process without current time and without timeSeq");
        if (TaskStatus.getStatus(task.getStatus()) != TaskStatus.RUNNING)
            throw new Exception("Only running process can be finished");

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
        BigDecimal total = currentTime.getPriceHour().multiply(new BigDecimal(currentTime.getDurationPlaySec() / 3600));
        currentTime.setTotal(total);
        currentTime.setFinishTime(finishTime);
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user_action
        addUserAction(stop ? Action.STOP : Action.FINISH, finishTime, currentTime);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    private void endCustom(UserSiteTask task, Date finishTime, boolean force) throws Exception {
        logger.debug("entTask task={}, finishTime={}, force={}", task, finishTime, force);
        if (task == null)
            throw new IllegalArgumentException("UserSiteTask is null");
        UserTaskTime currentTime = task.getCurrentTime();
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("Can't end process without current time and without timeSeq");
        TaskStatus customTaskStatus = TaskStatus.getStatus(task.getStatus());
        if (customTaskStatus != TaskStatus.CUSTOM1 || customTaskStatus != TaskStatus.CUSTOM2 || customTaskStatus != TaskStatus.CUSTOM3)
            throw new Exception("Only Custom process can be finished");

        //get Time Seq in reverse order
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("endTask timeSeqList={}", timeSeqList);
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        UserTaskTimeSeq currentTimeSeq = timeSeqList.get(0);
        TaskStatus currentTimeSeqStatus = TaskStatus.getStatus(currentTimeSeq.getTaskStatus());
        if (currentTimeSeqStatus != TaskStatus.CUSTOM1 || currentTimeSeqStatus != TaskStatus.CUSTOM2 || currentTimeSeqStatus != TaskStatus.CUSTOM3)
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

        //add user_task_time_seq to the end of current user_task_time with status running
        UserTaskTimeSeq timeSeq = new UserTaskTimeSeq();
        timeSeq.setTaskStatus(TaskStatus.RUNNING.getStatusStr());
        timeSeq.setStartTime(finishTime);
        timeSeq.setPrevTimeSeq(currentTimeSeq);
        currentTimeSeq.setNextTimeSeq(timeSeq);
        userTaskTimeSeqDao.persist(timeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        //change user_site_task status and remove current_time
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
