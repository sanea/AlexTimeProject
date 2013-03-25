package ru.alex.webapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.GenericDao;
import ru.alex.webapp.dao.TaskDao;
import ru.alex.webapp.dao.UserActionDao;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.dao.UserSiteTaskDao;
import ru.alex.webapp.dao.UserTaskTimeDao;
import ru.alex.webapp.dao.UserTaskTimeSeqDao;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserAction;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.model.UserTaskTimeSeq;
import ru.alex.webapp.model.enums.Action;
import ru.alex.webapp.model.enums.TaskStatus;
import ru.alex.webapp.model.enums.TaskType;
import ru.alex.webapp.service.TaskService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class TaskServiceImpl extends GenericServiceImpl<Task, Long> implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserSiteTaskDao userTaskDao;
    @Autowired
    private UserTaskTimeDao userTaskTimeDao;
    @Autowired
    private UserTaskTimeSeqDao userTaskTimeSeqDao;
    @Autowired
    private UserActionDao userActionDao;

    @Override
    protected GenericDao<Task, Long> getDao() {
        return taskDao;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void add(Task task) throws Exception {
        logger.debug("addTask task={}", task);
        if (task == null)
            throw new IllegalArgumentException("Wrong task");
        if (task.getName() == null || task.getName().equals(""))
            throw new Exception("Name can't be empty");
        if (task.getType() == null || task.getType().equals(""))
            throw new Exception("Type can't be empty");
        if (task.getPriceHour() == null)
            throw new Exception("Price can't be empty");
        if (task.getEnabled() == null || task.getIncome() == null)
            throw new Exception("enabled and income can't be empty");
        task.setDeleted(false);
        taskDao.persist(task);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void remove(Task task) throws Exception {
        //TODO
        logger.debug("removeTask task={}", task);
        if (task == null)
            throw new IllegalArgumentException("Wrong task");
        Task mergedTask = taskDao.findById(task.getId());
        logger.debug("removeTask task={}", task);
        if (mergedTask == null)
            throw new Exception("Can't find task with id=" + task.getId());
        if (!isTaskEditable(task.getId())) {
            throw new Exception("Task is already stated, please disable this task, can't delete");
        }
        taskDao.remove(mergedTask);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(Task task) throws Exception {
        logger.debug("editTask task={}", task);
        if (task == null)
            throw new IllegalArgumentException("Wrong task");
        Task taskEntity = taskDao.findById(task.getId());
        if (taskEntity == null)
            throw new Exception("Can't find task with id=" + task.getId());
        boolean isEditable = isTaskEditable(task.getId());
        if (!isEditable) {
            if (!taskEntity.getType().equals(task.getType()))
                throw new Exception("Can't change type of not editable task");
            if (!taskEntity.getPriceHour().equals(task.getPriceHour()))
                throw new Exception("Can't change price of not editable task");
        }
        task = taskDao.merge(task);
        logger.debug("editTask merged_task={}", task);
    }

    /**
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @Override
    public boolean isTaskEditable(Long taskId) throws Exception {
        logger.debug("isTaskEditable taskId={}", taskId);
        boolean result = true;
        if (taskId == null)
            throw new IllegalArgumentException("Wrong taskId");
        Task task = taskDao.findById(taskId);
        logger.debug("removeTask task={}", task);
        if (task == null)
            throw new Exception("Can't find task with id=" + taskId);
        //TODO
//        Collection<UserSiteTask> userTasks = task.getUserTasksById();
//        for (UserSiteTask userTask : userTasks) {
//            if (TaskStatus.getStatus(userTask.getStatus()) != TaskStatus.UNKNOWN
//                    || userTask.getUserTaskTimeList().size() > 0) {
//                result = false;
//            }
//        }
        logger.debug("isTaskEditable {}", result);
        return result;
    }

    //------------------------


    /**
     * Returns list of enable tasks for user
     * checks and ends tasks
     *
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    public List<UserSiteTask> getTasksForUser(String username) throws Exception {
        logger.debug("getTasksForUser username={}", username);
        if (username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong username");
        List<UserSiteTask> tasks = null; //userTaskDao.getTasksForUser(username);
        logger.debug("getTasksForUser tasks={}", tasks);
//        for (UserSiteTask task : tasks)
//            checkTask(task);
        return tasks;
    }

    /**
     * Returns list of task with status 'r'
     * checks and ends tasks
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<UserSiteTask> getOnlineTasks() throws Exception {
        logger.debug("getOnlineTasks");
        List<UserSiteTask> tasks = null; //userTaskDao.getRunningTasks();
        logger.debug("getOnlineTasks runningTasks={}", tasks);
//        List<UserSiteTask> result = new ArrayList<UserSiteTask>(tasks.size());
//        for (UserSiteTask task : tasks) {
//            boolean ended = checkTask(task);
//            if (!ended)
//                result.add(task);
//        }
//        return result;
        return tasks;
    }

    @Override
    public List<UserSiteTask> getUsersForTask(Long taskId) throws Exception {
        logger.debug("getUsersForTask taskId={}", taskId);
        if (taskId == null)
            throw new IllegalArgumentException("Wrong taskId");
        List<UserSiteTask> tasks = null; //userTaskDao.getUsersForTask(taskId);
        logger.debug("getUsersForTask tasks={}", tasks);
        return tasks;
    }

    @Override
    public UserTaskTime getCurrentTimeForUserTask(Long taskId, String username) throws Exception {
        logger.debug("getCurrentTimeForUserTask taskId={}, username={}", taskId, username);
        List<UserTaskTime> currentTimeList = null; //userTaskTimeDao.getCurrentTime(taskId, username);
        logger.debug("getCurrentTimeForUserTask currentTimeList={}", currentTimeList);
        if (currentTimeList.size() > 1)
            throw new Exception("current time list should have size <= 1");
        return currentTimeList.size() == 1 ? currentTimeList.get(0) : null;
    }

    /**
     * Returns list of task_time with user_task status not 'r'
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<UserTaskTime> getAllNotCurrentTime(Date from, Date to) throws Exception {
        logger.debug("getAllNotCurrentTime from={}, to={}", from, to);
        List<UserTaskTime> tasks;
        if (from == null && to == null)
            tasks = null; //userTaskTimeDao.getAllNotCurrentTime();
        else
            tasks = null; //userTaskTimeDao.getNotCurrentTime(from, to);
        logger.debug("getAllNotCurrentTime tasks={}", tasks);
        return tasks;
    }

    /**
     * Returns time spent for userTask, even if task is not paused or stopped
     *
     * @param taskId
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    public int getTimeSpentSecForUserTask(Long taskId, String username) throws Exception {
        logger.debug("getTimeSpentSecForUserTask taskId={}, username={}", taskId, username);
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("getTimeSpentSecForUserTask currentTime={}", currentTime);
        int timeSpentSec = 0;
        if (currentTime != null) {
            List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
            logger.debug("getTimeSpentSecForUserTask timeSeqList={}", timeSeqList);
            for (UserTaskTimeSeq ts : timeSeqList) {
                Date endTime = ts.getEndTime() == null ? new Date() : ts.getEndTime();
                if (endTime.before(ts.getStartTime()))
                    throw new Exception("time sequence should have correct end time");
                long seqDif = (endTime.getTime() - ts.getStartTime().getTime()) / 1000;
                timeSpentSec = (int) (timeSpentSec + seqDif);
            }
        }
        return timeSpentSec;
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
        TaskStatus status = TaskStatus.getStatus(task.getStatus());
        UserTaskTime currentTime = getCurrentTimeForUserTask(task.getSiteTask().getTaskByTaskId().getId(), task.getUserByUsername().getUsername());
        logger.debug("checkTask currentTime={}", currentTime);
        if (status == TaskStatus.RUNNING) {
            if (currentTime == null)
                throw new Exception("Running task should have current time");
            Date now = new Date();
            if (now.after(currentTime.getFinishTime())) {
                endTask(task, currentTime, currentTime.getFinishTime(), false);
                return true;
            }
        } else if (status != TaskStatus.CUSTOM1 && currentTime != null) {
            throw new Exception("Current time should be only for running and paused task");
        }
        return false;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    private void endTask(UserSiteTask task, UserTaskTime currentTime, Date finishTime, boolean stop) throws Exception {
        logger.debug("endTask task={}, finishTime={}, stop={}", task, finishTime, stop);
        logger.debug("endTask currentTime={}", currentTime);
        //userTaskDao.lock(task, LockModeType.PESSIMISTIC_WRITE);
        //userTaskTimeDao.lock(currentTime, LockModeType.PESSIMISTIC_WRITE);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //calculate timeSpentSec and close last timeSec if Running
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("endTask timeSeqList={}", timeSeqList);
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        TaskStatus taskStatus = TaskStatus.getStatus(task.getStatus());
        switch (taskStatus) {
            case RUNNING:
                //set the last time_seq end_time value
                UserTaskTimeSeq currentTimeSeq = userTaskTimeSeqDao.findById(timeSeqList.get(0).getId());
                currentTimeSeq.setEndTime(finishTime);
                currentTimeSeq = userTaskTimeSeqDao.merge(currentTimeSeq);
                userTaskTimeSeqDao.flush();
                userTaskTimeSeqDao.clear();
                timeSeqList.set(0, currentTimeSeq);
                break;
            case CUSTOM1:
                if (timeSeqList.get(0).getEndTime() == null)
                    throw new Exception("Time seq should have end time in Paused task");
                break;
            default:
                throw new Exception("Wrong task status, can't end task");
        }

        //change user_task status
        task.setStatus(stop ? TaskStatus.STOPPED.getStatusStr() : TaskStatus.COMPLETED.getStatusStr());
        task.setUpdateTime(finishTime);
        //userTaskDao.merge(task);
        //userTaskDao.flush();
        //userTaskDao.clear();

        //change user_tak_time duration
        if (stop) {
            int timeSpentSec = 0;
            //timeSeqList is updated
            for (UserTaskTimeSeq timeSeq : timeSeqList) {
                if (timeSeq.getEndTime() == null || timeSeq.getEndTime().before(timeSeq.getStartTime()))
                    throw new Exception("time sequence should have correct end time");
                long timeDif = (timeSeq.getEndTime().getTime() - timeSeq.getStartTime().getTime()) / 1000;
                timeSpentSec = (int) (timeSpentSec + timeDif);
            }
            currentTime.setDurationPlaySec(timeSpentSec);
        }
        //change user_tak_time current value
        //currentTime.setCurrent(false);
        currentTime.setFinishTime(finishTime);
        currentTime = null; //userTaskTimeDao.merge(currentTime);
        //userTaskTimeDao.flush();
        //userTaskTimeDao.clear();

        //add user_action
        UserAction action = new UserAction();
        action.setTimestamp(finishTime);
        action.setAction(stop ? Action.STOP.getActionStr() : Action.FINISH.getActionStr());
        //TODO addUserAction method add
        //currentTime.addUserAction(action);
        userActionDao.persist(action);
        userActionDao.flush();
        userActionDao.clear();
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

        List<UserTaskTimeSeq> timeSeqList = new ArrayList<UserTaskTimeSeq>();
        if (nextTimeSeqList != null)
            timeSeqList.addAll(nextTimeSeqList);
        timeSeqList.add(timeSeq);
        return timeSeqList;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void startTask(Long taskId, String username, int seconds) throws Exception {
        logger.debug("startTask taskId={}, username={}, seconds={}", taskId, username, seconds);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserSiteTask userSiteTask = null; //userTaskDao.getTaskForUser(username, taskId);
        logger.debug("startTask userSiteTask={}", userSiteTask);
        if (userSiteTask == null)
            throw new Exception("can't find user task");
        //only COMPLETED, UNKNOWN and STOPPED allowed
        if (!(userSiteTask.getStatus().equals(TaskStatus.COMPLETED.getStatusStr())
                || userSiteTask.getStatus().equals(TaskStatus.UNKNOWN.getStatusStr())
                || userSiteTask.getStatus().equals(TaskStatus.STOPPED.getStatusStr())))
            throw new Exception("wrong status of task for user");


        UserTaskTime taskTime = new UserTaskTime();
        UserTaskTimeSeq taskTimeSeq = new UserTaskTimeSeq();
        UserAction userAction = new UserAction();
        Date now = new Date();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND, seconds);
        switch (TaskType.getType(userSiteTask.getSiteTask().getTaskByTaskId().getType())) {
            case PROCESS:
                if (seconds <= 0)
                    throw new IllegalArgumentException("Wrong Input Params for starting task");
                //change user_task status
                userSiteTask.setStatus(TaskStatus.RUNNING.getStatusStr());
                userSiteTask.setUpdateTime(now);
                userSiteTask = null; //userTaskDao.merge(userSiteTask);

                //add user_task_time
                taskTime.setDurationPlaySec(seconds);
                taskTime.setStartTime(now);
                taskTime.setFinishTime(endTime.getTime());
                //TODO
                //taskTime.setCurrent(true);
                //userSiteTask.addUserTaskTime(taskTime);

                //add user_task_time_seq
                taskTimeSeq.setStartTime(now);
                taskTime.setTimeSeq(taskTimeSeq);

                //add user_action
                userAction.setAction(Action.START.getActionStr());
                userAction.setParams(String.valueOf(seconds));
                userAction.setTimestamp(now);
                //TODO
                //taskTime.addUserAction(userAction);
                break;
            case TASK:
                if (seconds != 0)
                    throw new IllegalArgumentException("Wrong Input Params for starting task");
                //change user_task status
                userSiteTask.setStatus(TaskStatus.COMPLETED.getStatusStr());
                userSiteTask.setUpdateTime(now);
                userSiteTask = null; //userTaskDao.merge(userSiteTask);

                //add user_task_time
                taskTime.setDurationPlaySec(0);
                taskTime.setStartTime(now);
                taskTime.setFinishTime(now);
                //TODO
                //taskTime.setCurrent(false);
                //userSiteTask.addUserTaskTime(taskTime);

                //add user_action
                userAction.setAction(Action.START.getActionStr());
                userAction.setTimestamp(now);
                //TODO
                //taskTime.addUserAction(userAction);
                break;
            default:
                throw new Exception("Wrong task type");
        }
        //userTaskTimeDao.persist(taskTime);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void pauseTask(Long taskId, String username) throws Exception {
        logger.debug("pauseTask taskId={}, username={}", taskId, username);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserSiteTask userSiteTask = null; //userTaskDao.getTaskForUser(username, taskId);
        logger.debug("pauseTask userSiteTask={}", userSiteTask);
        if (userSiteTask == null)
            throw new Exception("can't find user task");
        if (!userSiteTask.getStatus().equals(TaskStatus.RUNNING.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (TaskType.getType(userSiteTask.getSiteTask().getTaskByTaskId().getType()) == TaskType.TASK)
            throw new Exception("can't pause Task");
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("pauseTask currentTime={}", currentTime);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //close task if time is finished
//        boolean isClosed = checkTask(userSiteTask);
//        logger.debug("pauseTask isClosed={}",  isClosed);
//        if (isClosed)
//            return;

        Date now = new Date();
        //change user_task status
        userSiteTask.setUpdateTime(now);
        userSiteTask.setStatus(TaskStatus.CUSTOM1.getStatusStr());
        //userTaskDao.merge(userSiteTask);
        //userTaskDao.flush();
        //userTaskDao.clear();

        //set end time user_task_time_seq
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("pauseTask timeSeqList={}", timeSeqList);
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        UserTaskTimeSeq currentTimeSeq = userTaskTimeSeqDao.findById(timeSeqList.get(0).getId());
        currentTimeSeq.setEndTime(now);
        userTaskTimeSeqDao.merge(currentTimeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        //add user action
        UserAction userAction = new UserAction();
        userAction.setAction(Action.CUSTOM1.getActionStr());
        userAction.setTimestamp(now);
        //TODO
        //currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void resumeTask(Long taskId, String username) throws Exception {
        logger.debug("resumeTask taskId={}, username={}", taskId, username);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserSiteTask userSiteTask = null; //userTaskDao.getTaskForUser(username, taskId);
        logger.debug("resumeTask userSiteTask={}", userSiteTask);
        if (userSiteTask == null)
            throw new Exception("can't find user task");
        if (!userSiteTask.getStatus().equals(TaskStatus.CUSTOM1.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (TaskType.getType(userSiteTask.getSiteTask().getTaskByTaskId().getType()) == TaskType.TASK)
            throw new Exception("can't resume Task");
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("resumeTask currentTime={}", currentTime);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        Date now = new Date();
        //change user_task status
        userSiteTask.setUpdateTime(now);
        userSiteTask.setStatus(TaskStatus.RUNNING.getStatusStr());
        //userTaskDao.merge(userSiteTask);
        //userTaskDao.flush();
        //userTaskDao.clear();

        //update finish_time of user_task_time
        int timeSpentSec = 0;
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("resumeTask timeSeqList={}", timeSeqList);
        for (UserTaskTimeSeq ts : timeSeqList) {
            if (ts.getEndTime() == null || ts.getEndTime().before(ts.getStartTime()))
                throw new Exception("time sequence should have correct end time");
            long seqDif = (ts.getEndTime().getTime() - ts.getStartTime().getTime()) / 1000;
            timeSpentSec = (int) (timeSpentSec + seqDif);
        }
        int secondToAdd = currentTime.getDurationPlaySec() - timeSpentSec;
        Calendar finisTime = Calendar.getInstance();
        finisTime.setTime(now);
        finisTime.add(Calendar.SECOND, secondToAdd);
        currentTime.setFinishTime(finisTime.getTime());
        currentTime = null; //userTaskTimeDao.merge(currentTime);
        //userTaskTimeDao.flush();
        //userTaskTimeDao.clear();

        //add user_task_time_seq to end of current user_task_time
        UserTaskTimeSeq timeSeq = new UserTaskTimeSeq();
        timeSeq.setStartTime(now);
        UserTaskTimeSeq prevTimeSeq = userTaskTimeSeqDao.findById(timeSeqList.get(0).getId());
        timeSeq.setPrevTimeSeq(prevTimeSeq);
        prevTimeSeq.setNextTimeSeq(timeSeq);
        userTaskTimeSeqDao.persist(timeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        //add user action
        UserAction userAction = new UserAction();
        userAction.setAction(Action.RESUME.getActionStr());
        userAction.setTimestamp(now);
        //TODO
        //currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void extendTask(Long taskId, String username, int seconds) throws Exception {
        logger.debug("extendTask taskId={}, username={}, seconds={}", taskId, username, seconds);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserSiteTask userSiteTask = null; //userTaskDao.getTaskForUser(username, taskId);
        logger.debug("extendTask userSiteTask={}", userSiteTask);
        if (userSiteTask == null)
            throw new Exception("can't find user task");
        if (!userSiteTask.getStatus().equals(TaskStatus.RUNNING.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (TaskType.getType(userSiteTask.getSiteTask().getTaskByTaskId().getType()) == TaskType.TASK)
            throw new Exception("can't extend Task");
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("extendTask currentTime={}", currentTime);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //close task if time is finished
//        boolean isClosed = checkTask(userSiteTask);
//        logger.debug("extendTask isClosed={}",  isClosed);
//        if (isClosed)
//            return;

        Date now = new Date();
        //change user_task status no needed, has RUNNING status
        //extend finish_time of user_task_time and duration
        Calendar finisTime = Calendar.getInstance();
        finisTime.setTime(currentTime.getFinishTime());
        finisTime.add(Calendar.SECOND, seconds);
        currentTime.setFinishTime(finisTime.getTime());
        currentTime.setDurationPlaySec(currentTime.getDurationPlaySec() + seconds);
        currentTime = null; //userTaskTimeDao.merge(currentTime);
        //userTaskTimeDao.flush();
        //userTaskTimeDao.clear();

        //add user action
        UserAction userAction = new UserAction();
        userAction.setAction(Action.EXTEND.getActionStr());
        userAction.setTimestamp(now);
        //TODO
        //currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    public void stopTask(Long taskId, String username) throws Exception {
        logger.debug("stopTask taskId={}, username={}", taskId, username);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserSiteTask userSiteTask = null; //userTaskDao.getTaskForUser(username, taskId);
        logger.debug("stopTask userSiteTask={}", userSiteTask);
        if (userSiteTask == null)
            throw new Exception("can't find user task");
        //only CUSTOM1 and RUNNING allowed
        if (!(userSiteTask.getStatus().equals(TaskStatus.CUSTOM1.getStatusStr())
                || userSiteTask.getStatus().equals(TaskStatus.RUNNING.getStatusStr())))
            throw new Exception("wrong status of task for user");
        if (TaskType.getType(userSiteTask.getSiteTask().getTaskByTaskId().getType()) == TaskType.TASK)
            throw new Exception("can't pause Task");
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("stopTask currentTime={}", currentTime);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //check if task should be ended
//        boolean isClosed = checkTask(userSiteTask);
//        logger.debug("stopTask isClosed={}",  isClosed);
//        if (isClosed)
//            return;

        //force ending task
        endTask(userSiteTask, currentTime, new Date(), true);
    }




    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void updateUserTask(Long taskId, String username, boolean assigned) throws Exception {
        logger.debug("updateUserTask taskId={}, username={}, assigned={}", taskId, username, assigned);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong params for updateUserTask");
        List<UserSiteTask> userSiteTaskList = null; //userTaskDao.getTaskForUserAll(username, taskId);
        logger.debug("updateUserTask userSiteTaskList={}", userSiteTaskList);
        if (userSiteTaskList != null && userSiteTaskList.size() > 1)
            throw new Exception("Smth is wrong with database");
        Date now = new Date();
        //TODO
//        if (userSiteTaskList != null && userSiteTaskList.size() == 1) {
//            if (assigned) {
//                if (!userSiteTaskList.get(0).getEnabled()) {
//                    logger.debug("enable user task");
//                    UserSiteTask ut = userSiteTaskList.get(0);
//                    ut.setEnabled(true);
//                    ut.setUpdateTime(now);
//                    null; //userTaskDao.merge(ut);
//                }
//            } else {
//                if (userSiteTaskList.get(0).getEnabled()) {
//                    logger.debug("disable user task");
//                    UserSiteTask ut = userSiteTaskList.get(0);
//                    if (TaskStatus.getStatus(ut.getStatus()) == TaskStatus.RUNNING)
//                        throw new Exception("Can't disable running task");
//                    ut.setEnabled(false);
//                    ut.setUpdateTime(now);
//                    null; //userTaskDao.merge(ut);
//                }
//            }
//        } else if (assigned) {
//            logger.debug("create new user task");
//            UserSiteTask ut = new UserSiteTask();
//            ut.setEnabled(true);
//            ut.setCreateTime(now);
//            ut.setUpdateTime(now);
//            ut.setStatus(TaskStatus.UNKNOWN.getStatusStr());
//
//            Task task = taskDao.findById(taskId);
//            logger.debug("updateUserTask task={}", task);
//            if (task == null)
//                throw new Exception("wrong task id");
//            ut.setTaskByTaskId(task);
//
//            User user = userDao.findById(username);
//            logger.debug("updateUserTask user={}", user);
//            if (task == null)
//                throw new Exception("wrong username");
//            ut.setUserByUsername(user);
//
//            null; //userTaskDao.persist(ut);
//        }
    }

    /**
     * Repeats every second
     */
//    @Scheduled(fixedDelay = 10000)
    public void checkAllTasks() {
        logger.debug("checkAllTasks");
        List<UserSiteTask> runningTasks = null; //userTaskDao.getRunningTasks();
        logger.debug("checkAllTasks runningTasks={}", runningTasks);
        try {
            for (UserSiteTask task : runningTasks) {
                checkTask(task);
            }
        } catch (Exception ex) {
            logger.error("checkAllTasks " + ex.getMessage(), ex);
        }
    }
}
