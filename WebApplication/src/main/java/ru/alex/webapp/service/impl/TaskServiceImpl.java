package ru.alex.webapp.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.*;
import ru.alex.webapp.model.*;
import ru.alex.webapp.service.TaskService;

import javax.persistence.LockModeType;
import java.util.*;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = Logger.getLogger(TaskServiceImpl.class);
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserTaskDao userTaskDao;
    @Autowired
    private UserTaskTimeDao userTaskTimeDao;
    @Autowired
    private UserTaskTimeSeqDao userTaskTimeSeqDao;
    @Autowired
    private UserActionDao userActionDao;

    @Override
    public List<Task> getAllTasks() throws Exception {
        logger.debug("getAllTasks");
        return taskDao.findAll();
    }

    /**
     * Returns list of enable tasks for user
     * checks and ends tasks
     *
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    public List<UserTask> getTasksForUser(String username) throws Exception {
        logger.debug("getTasksForUser username=" + username);
        if (username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong username");
        List<UserTask> tasks = userTaskDao.getTasksForUser(username);
        logger.debug("getTasksForUser tasks=" + tasks);
//        for (UserTask task : tasks)
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
    public List<UserTask> getOnlineTasks() throws Exception {
        logger.debug("getOnlineTasks");
        List<UserTask> tasks = userTaskDao.getRunningTasks();
        logger.debug("getOnlineTasks runningTasks=" + tasks);
//        List<UserTask> result = new ArrayList<UserTask>(tasks.size());
//        for (UserTask task : tasks) {
//            boolean ended = checkTask(task);
//            if (!ended)
//                result.add(task);
//        }
//        return result;
        return tasks;
    }



    @Override
    public List<UserTask> getUsersForTask(Long taskId) throws Exception {
        logger.debug("getUsersForTask taskId=" + taskId);
        if (taskId == null)
            throw new IllegalArgumentException("Wrong taskId");
        List<UserTask> tasks = userTaskDao.getUsersForTask(taskId);
        logger.debug("getUsersForTask tasks=" + tasks);
        return tasks;
    }

    @Override
    public UserTaskTime getCurrentTimeForUserTask(Long taskId, String username) throws Exception {
        logger.debug("getCurrentTimeForUserTask taskId=" + taskId + ", username=" + username);
        List<UserTaskTime> currentTimeList = userTaskTimeDao.getCurrentTime(taskId, username);
        logger.debug("getCurrentTimeForUserTask currentTimeList=" + currentTimeList);
        if (currentTimeList.size() > 1)
            throw new Exception("current time list should have size <= 1");
        return currentTimeList.size() == 1 ? currentTimeList.get(0) : null;
    }


    /**
     * Returns list of task_time with user_task status not 'r'
     * @return
     * @throws Exception
     */
    @Override
    public List<UserTaskTime> getAllNotCurrentTime() throws Exception {
        logger.debug("getAllNotCurrentTime");
        List<UserTaskTime> tasks = userTaskTimeDao.getAllNotCurrentTime();
        logger.debug("getAllNotCurrentTime tasks=" + tasks);
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
        logger.debug("getTimeSpentSecForUserTask taskId=" + taskId + ", username=" + username);
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("getTimeSpentSecForUserTask currentTime=" + currentTime);
        int timeSpentSec = 0;
        if (currentTime != null) {
            List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
            logger.debug("getTimeSpentSecForUserTask timeSeqList=" + timeSeqList);
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
    private boolean checkTask(UserTask task) throws Exception {
        logger.debug("checkTask task=" + task);
        TaskStatus status = TaskStatus.getStatus(task.getStatus());
        UserTaskTime currentTime = getCurrentTimeForUserTask(task.getTaskByTaskId().getId(), task.getUserByUsername().getUsername());
        logger.debug("checkTask currentTime=" + currentTime);
        if (status == TaskStatus.RUNNING) {
            if (currentTime == null)
                throw new Exception("Running task should have current time");
            Date now = new Date();
            if (now.after(currentTime.getFinishTime())) {
                endTask(task, currentTime, currentTime.getFinishTime(), false);
                return true;
            }
        } else if (status != TaskStatus.PAUSED && currentTime != null) {
            throw new Exception("Current time should be only for running and paused task");
        }
        return false;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    private void endTask(UserTask task, UserTaskTime currentTime, Date finishTime, boolean stop) throws Exception {
        logger.debug("endTask task=" + task + ", finishTime=" + finishTime + ", stop=" + stop);
        logger.debug("endTask currentTime=" + currentTime);
        userTaskDao.lock(task, LockModeType.PESSIMISTIC_WRITE);
        userTaskTimeDao.lock(currentTime, LockModeType.PESSIMISTIC_WRITE);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //calculate timeSpentSec and close last timeSec if Running
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("endTask timeSeqList=" + timeSeqList);
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
            case PAUSED:
                if (timeSeqList.get(0).getEndTime() == null)
                    throw new Exception("Time seq should have end time in Paused task");
                break;
            default:
                throw new Exception("Wrong task status, can't end task");
        }

        //change user_task status
        task.setStatus(stop ? TaskStatus.STOPPED.getStatusStr() : TaskStatus.COMPLETED.getStatusStr());
        task.setUpdateTime(finishTime);
        userTaskDao.merge(task);
        userTaskDao.flush();
        userTaskDao.clear();

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
            currentTime.setDurationSec(timeSpentSec);
        }
        //change user_tak_time current value
        currentTime.setCurrent(false);
        currentTime.setFinishTime(finishTime);
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user_action
        UserAction action = new UserAction();
        action.setTimestamp(finishTime);
        action.setAction(stop ? Action.STOP.getActionStr() : Action.FINISH.getActionStr());
        currentTime.addUserAction(action);
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
        logger.debug("getAllTimeSeq timeSeq=" + timeSeq);
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
        logger.debug("startTask taskId=" + taskId + ", username=" + username + ", seconds=" + seconds);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        logger.debug("startTask userTask=" + userTask);
        if (userTask == null)
            throw new Exception("can't find user task");
        //only COMPLETED, UNKNOWN and STOPPED allowed
        if (!(userTask.getStatus().equals(TaskStatus.COMPLETED.getStatusStr())
                || userTask.getStatus().equals(TaskStatus.UNKNOWN.getStatusStr())
                || userTask.getStatus().equals(TaskStatus.STOPPED.getStatusStr())))
            throw new Exception("wrong status of task for user");


        UserTaskTime taskTime = new UserTaskTime();
        UserTaskTimeSeq taskTimeSeq = new UserTaskTimeSeq();
        UserAction userAction = new UserAction();
        Date now = new Date();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND, seconds);
        switch (TaskType.getType(userTask.getTaskByTaskId().getType())) {
            case PROCESS:
                if (seconds <= 0)
                    throw new IllegalArgumentException("Wrong Input Params for starting task");
                //change user_task status
                userTask.setStatus(TaskStatus.RUNNING.getStatusStr());
                userTask.setUpdateTime(now);
                userTask = userTaskDao.merge(userTask);

                //add user_task_time
                taskTime.setDurationSec(seconds);
                taskTime.setStartTime(now);
                taskTime.setFinishTime(endTime.getTime());
                taskTime.setCurrent(true);
                userTask.addUserTaskTime(taskTime);

                //add user_task_time_seq
                taskTimeSeq.setStartTime(now);
                taskTime.setTimeSeq(taskTimeSeq);

                //add user_action
                userAction.setAction(Action.START.getActionStr());
                userAction.setTimeSeconds(seconds);
                userAction.setTimestamp(now);
                taskTime.addUserAction(userAction);
                break;
            case TASK:
                if (seconds != 0)
                    throw new IllegalArgumentException("Wrong Input Params for starting task");
                //change user_task status
                userTask.setStatus(TaskStatus.COMPLETED.getStatusStr());
                userTask.setUpdateTime(now);
                userTask = userTaskDao.merge(userTask);

                //add user_task_time
                taskTime.setDurationSec(0);
                taskTime.setStartTime(now);
                taskTime.setFinishTime(now);
                taskTime.setCurrent(false);
                userTask.addUserTaskTime(taskTime);

                //add user_action
                userAction.setAction(Action.START.getActionStr());
                userAction.setTimestamp(now);
                taskTime.addUserAction(userAction);
                break;
            default:
                throw new Exception("Wrong task type");
        }
        userTaskTimeDao.persist(taskTime);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void pauseTask(Long taskId, String username) throws Exception {
        logger.debug("pauseTask taskId=" + taskId + ", username=" + username);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        logger.debug("pauseTask userTask=" + userTask);
        if (userTask == null)
            throw new Exception("can't find user task");
        if (!userTask.getStatus().equals(TaskStatus.RUNNING.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (TaskType.getType(userTask.getTaskByTaskId().getType()) == TaskType.TASK)
            throw new Exception("can't pause Task");
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("pauseTask currentTime=" + currentTime);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //close task if time is finished
//        boolean isClosed = checkTask(userTask);
//        logger.debug("pauseTask isClosed=" + isClosed);
//        if (isClosed)
//            return;

        Date now = new Date();
        //change user_task status
        userTask.setUpdateTime(now);
        userTask.setStatus(TaskStatus.PAUSED.getStatusStr());
        userTaskDao.merge(userTask);
        userTaskDao.flush();
        userTaskDao.clear();

        //set end time user_task_time_seq
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("pauseTask timeSeqList=" + timeSeqList);
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        UserTaskTimeSeq currentTimeSeq = userTaskTimeSeqDao.findById(timeSeqList.get(0).getId());
        currentTimeSeq.setEndTime(now);
        userTaskTimeSeqDao.merge(currentTimeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        //add user action
        UserAction userAction = new UserAction();
        userAction.setAction(Action.PAUSE.getActionStr());
        userAction.setTimestamp(now);
        currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void resumeTask(Long taskId, String username) throws Exception {
        logger.debug("resumeTask taskId=" + taskId + ", username=" + username);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        logger.debug("resumeTask userTask=" + userTask);
        if (userTask == null)
            throw new Exception("can't find user task");
        if (!userTask.getStatus().equals(TaskStatus.PAUSED.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (TaskType.getType(userTask.getTaskByTaskId().getType()) == TaskType.TASK)
            throw new Exception("can't resume Task");
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("resumeTask currentTime=" + currentTime);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        Date now = new Date();
        //change user_task status
        userTask.setUpdateTime(now);
        userTask.setStatus(TaskStatus.RUNNING.getStatusStr());
        userTaskDao.merge(userTask);
        userTaskDao.flush();
        userTaskDao.clear();

        //update finish_time of user_task_time
        int timeSpentSec = 0;
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        logger.debug("resumeTask timeSeqList=" + timeSeqList);
        for (UserTaskTimeSeq ts : timeSeqList) {
            if (ts.getEndTime() == null || ts.getEndTime().before(ts.getStartTime()))
                throw new Exception("time sequence should have correct end time");
            long seqDif = (ts.getEndTime().getTime() - ts.getStartTime().getTime()) / 1000;
            timeSpentSec = (int) (timeSpentSec + seqDif);
        }
        int secondToAdd = currentTime.getDurationSec() - timeSpentSec;
        Calendar finisTime = Calendar.getInstance();
        finisTime.setTime(now);
        finisTime.add(Calendar.SECOND, secondToAdd);
        currentTime.setFinishTime(finisTime.getTime());
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

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
        currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void extendTask(Long taskId, String username, int seconds) throws Exception {
        logger.debug("extendTask taskId=" + taskId + ", username=" + username + ", seconds=" + seconds);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        logger.debug("extendTask userTask=" + userTask);
        if (userTask == null)
            throw new Exception("can't find user task");
        if (!userTask.getStatus().equals(TaskStatus.RUNNING.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (TaskType.getType(userTask.getTaskByTaskId().getType()) == TaskType.TASK)
            throw new Exception("can't extend Task");
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("extendTask currentTime=" + currentTime);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //close task if time is finished
//        boolean isClosed = checkTask(userTask);
//        logger.debug("extendTask isClosed=" + isClosed);
//        if (isClosed)
//            return;

        Date now = new Date();
        //change user_task status no needed, has RUNNING status
        //extend finish_time of user_task_time and duration
        Calendar finisTime = Calendar.getInstance();
        finisTime.setTime(currentTime.getFinishTime());
        finisTime.add(Calendar.SECOND, seconds);
        currentTime.setFinishTime(finisTime.getTime());
        currentTime.setDurationSec(currentTime.getDurationSec() + seconds);
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user action
        UserAction userAction = new UserAction();
        userAction.setAction(Action.EXTEND.getActionStr());
        userAction.setTimestamp(now);
        currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    public void stopTask(Long taskId, String username) throws Exception {
        logger.debug("stopTask taskId=" + taskId + ", username=" + username);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        logger.debug("stopTask userTask=" + userTask);
        if (userTask == null)
            throw new Exception("can't find user task");
        //only PAUSED and RUNNING allowed
        if (!(userTask.getStatus().equals(TaskStatus.PAUSED.getStatusStr())
                || userTask.getStatus().equals(TaskStatus.RUNNING.getStatusStr())))
            throw new Exception("wrong status of task for user");
        if (TaskType.getType(userTask.getTaskByTaskId().getType()) == TaskType.TASK)
            throw new Exception("can't pause Task");
        UserTaskTime currentTime = getCurrentTimeForUserTask(taskId, username);
        logger.debug("stopTask currentTime=" + currentTime);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //check if task should be ended
//        boolean isClosed = checkTask(userTask);
//        logger.debug("stopTask isClosed=" + isClosed);
//        if (isClosed)
//            return;

        //force ending task
        endTask(userTask, currentTime, new Date(), true);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void addTask(Task task) throws Exception {
        logger.debug("addTask task=" + task);
        if (task == null)
            throw new IllegalArgumentException("Wrong task");
        if (task.getName() == null || task.getName().equals(""))
            throw new Exception("Name can't be empty");
        if (task.getType() == null || task.getType().equals(""))
            throw new Exception("Type can't be empty");
        if (task.getPriceHour() == null)
            throw new Exception("Price can't be empty");
        taskDao.persist(task);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void removeTask(Long taskId) throws Exception {
        logger.debug("removeTask taskId=" + taskId);
        if (taskId == null)
            throw new IllegalArgumentException("Wrong taskId");
        Task task = taskDao.findById(taskId);
        logger.debug("removeTask task=" + task);
        if (task == null)
            throw new Exception("Can't find task with id=" + taskId);
        if (!isTaskEditable(taskId)) {
            throw new Exception("Task is already stated, please disable this task, can't delete");
        }
        for (UserTask userTask : task.getUserTasksById())
            userTaskDao.makeTransient(userTask);
        taskDao.makeTransient(task);
    }

    /**
     * Returns false if task has status not UNKNOWN or has at least 1 userTaskTime record
     * (Doesn't need checkTask(userTask); call)!
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @Override
    public boolean isTaskEditable(Long taskId) throws Exception {
        logger.debug("removeTask taskId=" + taskId);
        boolean result = true;
        if (taskId == null)
            throw new IllegalArgumentException("Wrong taskId");
        Task task = taskDao.findById(taskId);
        logger.debug("removeTask task=" + task);
        if (task == null)
            throw new Exception("Can't find task with id=" + taskId);
        Collection<UserTask> userTasks = task.getUserTasksById();
        for (UserTask userTask : userTasks) {
            if (TaskStatus.getStatus(userTask.getStatus()) != TaskStatus.UNKNOWN
                    || userTask.getUserTaskTimeList().size() > 0) {
                result = false;
            }
        }
        logger.debug("isTaskEditable " + result);
        return result;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void editTask(Task task) throws Exception {
        logger.debug("editTask task=" + task);
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
        logger.debug("editTask merged_task=" + task);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void updateUserTask(Long taskId, String username, boolean assigned) throws Exception {
        logger.debug("updateUserTask taskId=" + taskId + ", username=" + username + ", assigned=" + assigned);
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong params for updateUserTask");
        List<UserTask> userTaskList = userTaskDao.getTaskForUserAll(username, taskId);
        logger.debug("updateUserTask userTaskList=" + userTaskList);
        if (userTaskList != null && userTaskList.size() > 1)
            throw new Exception("Smth is wrong with database");
        Date now = new Date();
        if (userTaskList != null && userTaskList.size() == 1) {
            if (assigned) {
                if (!userTaskList.get(0).getEnabled()) {
                    logger.debug("enable user task");
                    UserTask ut = userTaskList.get(0);
                    ut.setEnabled(true);
                    ut.setUpdateTime(now);
                    userTaskDao.merge(ut);
                }
            } else {
                if (userTaskList.get(0).getEnabled()) {
                    logger.debug("disable user task");
                    UserTask ut = userTaskList.get(0);
                    if(TaskStatus.getStatus(ut.getStatus()) == TaskStatus.RUNNING)
                        throw new Exception("Can't disable running task");
                    ut.setEnabled(false);
                    ut.setUpdateTime(now);
                    userTaskDao.merge(ut);
                }
            }
        } else if (assigned) {
            logger.debug("create new user task");
            UserTask ut = new UserTask();
            ut.setEnabled(true);
            ut.setCreateTime(now);
            ut.setUpdateTime(now);
            ut.setStatus(TaskStatus.UNKNOWN.getStatusStr());

            Task task = taskDao.findById(taskId);
            logger.debug("updateUserTask task=" + task);
            if (task == null)
                throw new Exception("wrong task id");
            ut.setTaskByTaskId(task);

            User user = userDao.findById(username);
            logger.debug("updateUserTask user=" + user);
            if (task == null)
                throw new Exception("wrong username");
            ut.setUserByUsername(user);

            userTaskDao.persist(ut);
        }
    }

    /**
     * Repeats every second
     */
    @Scheduled(fixedDelay = 10000)
    public void checkAllTasks() {
        logger.debug("checkAllTasks");
        List<UserTask> runningTasks = userTaskDao.getRunningTasks();
        logger.debug("checkAllTasks runningTasks=" + runningTasks);
        try {
            for (UserTask task : runningTasks) {
                checkTask(task);
            }
        } catch (Exception ex) {
            logger.error("checkAllTasks " + ex.getMessage(), ex);
        }
    }
}
