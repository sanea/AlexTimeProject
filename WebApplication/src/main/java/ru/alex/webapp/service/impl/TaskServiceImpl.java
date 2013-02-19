package ru.alex.webapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.*;
import ru.alex.webapp.model.*;
import ru.alex.webapp.service.TaskService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class TaskServiceImpl implements TaskService {
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
    public List<UserTask> getTasksForUser(String username) throws Exception {
        if (username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong username");
        List<UserTask> tasks = userTaskDao.getTasksForUser(username);
        for (UserTask task : tasks)
            checkTask(task);
        return tasks;
    }

    /**
     * Check if task need to be ended
     *
     * @param task
     * @return if task was ended
     * @throws Exception
     */
    private boolean checkTask(UserTask task) throws Exception {
        UserTask.TaskStatus status = UserTask.TaskStatus.getStatus(task.getStatus().charAt(0));
        List<UserTaskTime> currentTimeList = userTaskTimeDao.getCurrentTime(task.getTaskByTaskId().getId(), task.getUserByUsername().getUsername());
        UserTaskTime currentTime = currentTimeList.size() == 1 ? currentTimeList.get(0) : null;
        if (status == UserTask.TaskStatus.RUNNING) {
            if (currentTime == null)
                throw new Exception("Running task should have current time");
            Date now = new Date();
            if (now.after(currentTime.getFinishTime())) {
                endTask(task);
                return true;
            }
        } else if (status != UserTask.TaskStatus.PAUSED && currentTime != null) {
            throw new Exception("Current time should be only for running and paused task");
        }
        return false;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    private void endTask(UserTask task) throws Exception {
        List<UserTaskTime> currentTimeList = userTaskTimeDao.getCurrentTime(task.getTaskByTaskId().getId(), task.getUserByUsername().getUsername());
        UserTaskTime currentTime = currentTimeList.size() == 1 ? currentTimeList.get(0) : null;
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        Date finishTime = currentTime.getFinishTime();

        //change user_task status
        task.setStatus(UserTask.TaskStatus.COMPLETED.getStatusStr());
        task.setUpdateTime(finishTime);
        userTaskDao.merge(task);
        userTaskDao.flush();
        userTaskDao.clear();

        //calculate timeSpentSec and close last timeSec
        int timeSpentSec = 0;
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        timeSeqList.get(0).setEndTime(finishTime);
        timeSeqList.set(0, userTaskTimeSeqDao.merge(timeSeqList.get(0)));
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        for (UserTaskTimeSeq timeSeq : timeSeqList) {
            if (timeSeq.getEndTime() == null && timeSeq.getEndTime().after(timeSeq.getStartTime()))
                throw new Exception("time sequence should have correct end time");
            long timeDif = (timeSeq.getEndTime().getTime() - timeSeq.getStartTime().getTime()) / 1000;
            timeSpentSec = (int) (timeSpentSec + timeDif);
        }

        //change user_tak_time time_spent and current value
        currentTime.setCurrent(false);
        currentTime.setTimeSpentSec(timeSpentSec);
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();
        //add user_action
        UserAction action = new UserAction();
        action.setTimestamp(finishTime);
        action.setAction(UserAction.Action.FINISH.getActionStr());
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
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        if (userTask == null)
            throw new Exception("can't find user task");
        if (!userTask.getStatus().equals(UserTask.TaskStatus.COMPLETED.getStatusStr())
                && !userTask.getStatus().equals(UserTask.TaskStatus.UNKNOWN.getStatusStr()))
            throw new Exception("wrong status of task for user");


        UserTaskTime taskTime = new UserTaskTime();
        UserTaskTimeSeq taskTimeSeq = new UserTaskTimeSeq();
        UserAction userAction = new UserAction();
        Date now = new Date();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND, seconds);
        switch (Task.TaskType.getType(userTask.getTaskByTaskId().getTaskType())) {
            case PROCESS:
                if (seconds < 0)
                    throw new IllegalArgumentException("Wrong Input Params for starting task");
                //change user_task status
                userTask.setStatus(UserTask.TaskStatus.RUNNING.getStatusStr());
                userTask.setUpdateTime(now);
                userTask = userTaskDao.merge(userTask);

                //add user_task_time
                taskTime.setTimeSpentSec(0);
                taskTime.setStartTime(now);
                taskTime.setFinishTime(endTime.getTime());
                taskTime.setCurrent(true);
                userTask.addUserTaskTime(taskTime);

                //add user_task_time_seq
                taskTimeSeq.setStartTime(now);
                taskTime.setTimeSeq(taskTimeSeq);

                //add user_action
                userAction.setAction(UserAction.Action.START.getActionStr());
                userAction.setTimeSeconds(seconds);
                userAction.setTimestamp(now);
                taskTime.addUserAction(userAction);
                break;
            case TASK:
                if (seconds != 0)
                    throw new IllegalArgumentException("Wrong Input Params for starting task");
                //change user_task status
                userTask.setStatus(UserTask.TaskStatus.COMPLETED.getStatusStr());
                userTask.setUpdateTime(now);
                userTask = userTaskDao.merge(userTask);

                //add user_task_time
                taskTime.setTimeSpentSec(0);
                taskTime.setStartTime(now);
                taskTime.setFinishTime(now);
                taskTime.setCurrent(false);
                userTask.addUserTaskTime(taskTime);

                //add user_action
                userAction.setAction(UserAction.Action.START.getActionStr());
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
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        if (userTask == null)
            throw new Exception("can't find user task");
        if (!userTask.getStatus().equals(UserTask.TaskStatus.RUNNING.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (userTask.getTaskByTaskId().getTaskType() == Task.TaskType.TASK.getType())
            throw new Exception("can't pause Task");
        List<UserTaskTime> currentTimeList = userTaskTimeDao.getCurrentTime(taskId, username);
        UserTaskTime currentTime = currentTimeList.size() == 1 ? currentTimeList.get(0) : null;
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        boolean isClosed = checkTask(userTask);
        if (isClosed)
            return;

        Date now = new Date();
        //change user_task status
        userTask.setUpdateTime(now);
        userTask.setStatus(UserTask.TaskStatus.PAUSED.getStatusStr());
        userTaskDao.merge(userTask);
        userTaskDao.flush();
        userTaskDao.clear();

        //set end time user_task_time_seq
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        timeSeqList.get(0).setEndTime(now);
        userTaskTimeSeqDao.merge(timeSeqList.get(0));
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        //add user action
        UserAction userAction = new UserAction();
        userAction.setAction(UserAction.Action.PAUSE.getActionStr());
        userAction.setTimestamp(now);
        currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void resumeTask(Long taskId, String username) throws Exception {
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        if (userTask == null)
            throw new Exception("can't find user task");
        if (!userTask.getStatus().equals(UserTask.TaskStatus.PAUSED.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (userTask.getTaskByTaskId().getTaskType() == Task.TaskType.TASK.getType())
            throw new Exception("can't pause Task");
        List<UserTaskTime> currentTimeList = userTaskTimeDao.getCurrentTime(taskId, username);
        UserTaskTime currentTime = currentTimeList.size() == 1 ? currentTimeList.get(0) : null;
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        Date now = new Date();
        //change user_task status
        userTask.setUpdateTime(now);
        userTask.setStatus(UserTask.TaskStatus.RUNNING.getStatusStr());
        userTaskDao.merge(userTask);
        userTaskDao.flush();
        userTaskDao.clear();

        //update finish_time of user_task_time
        int timeSpentSec = 0;
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        for (UserTaskTimeSeq ts : timeSeqList) {
            if (ts.getEndTime() == null && ts.getEndTime().after(ts.getStartTime()))
                throw new Exception("time sequence should have correct end time");
            long seqDif = (ts.getEndTime().getTime() - ts.getStartTime().getTime()) / 1000;
            timeSpentSec = (int) (timeSpentSec + seqDif);
        }
        long timeDif = (currentTime.getFinishTime().getTime() - currentTime.getStartTime().getTime()) / 1000;
        int secondToAdd = (int) (timeDif - timeSpentSec);
        Calendar finisTime = Calendar.getInstance();
        finisTime.setTime(now);
        finisTime.add(Calendar.SECOND, secondToAdd);
        currentTime.setFinishTime(finisTime.getTime());
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user_task_time_seq to end of current user_task_time
        List<UserTaskTimeSeq> timeSeqList2 = getAllTimeSeq(currentTime.getTimeSeq());
        if (timeSeqList2.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        UserTaskTimeSeq timeSeq = new UserTaskTimeSeq();
        timeSeq.setStartTime(now);
        timeSeq.setPrevTimeSeq(timeSeqList2.get(0));
        timeSeqList2.get(0).setNextTimeSeq(timeSeq);
        userTaskTimeSeqDao.persist(timeSeq);
        userTaskTimeSeqDao.flush();
        userTaskTimeSeqDao.clear();

        //add user action
        UserAction userAction = new UserAction();
        userAction.setAction(UserAction.Action.RESUME.getActionStr());
        userAction.setTimestamp(now);
        currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void extendTask(Long taskId, String username, int seconds) throws Exception {
        //TODO
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void stopTask(Long taskId, String username) throws Exception {
        //TODO
    }
}
