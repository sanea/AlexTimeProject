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

    @Override
    public UserTaskTime getCurrentTimeForUser(Long taskId, String username) throws Exception {
        List<UserTaskTime> currentTimeList = userTaskTimeDao.getCurrentTime(taskId, username);
        if (currentTimeList.size() > 1)
            throw new Exception("current time list should have size <= 1");
        return currentTimeList.size() == 1 ? currentTimeList.get(0) : null;
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
        UserTaskTime currentTime = getCurrentTimeForUser(task.getTaskByTaskId().getId(), task.getUserByUsername().getUsername());
        if (status == UserTask.TaskStatus.RUNNING) {
            if (currentTime == null)
                throw new Exception("Running task should have current time");
            Date now = new Date();
            if (now.after(currentTime.getFinishTime())) {
                endTask(task, currentTime.getFinishTime(), false);
                return true;
            }
        } else if (status != UserTask.TaskStatus.PAUSED && currentTime != null) {
            throw new Exception("Current time should be only for running and paused task");
        }
        return false;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    private void endTask(UserTask task, Date finishTime, boolean stop) throws Exception {
        UserTaskTime currentTime = getCurrentTimeForUser(task.getTaskByTaskId().getId(), task.getUserByUsername().getUsername());
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //calculate timeSpentSec and close last timeSec if Running
        List<UserTaskTimeSeq> timeSeqList = getAllTimeSeq(currentTime.getTimeSeq());
        if (timeSeqList.size() == 0)
            throw new Exception("timeSeqList should have size >= 1");
        UserTask.TaskStatus taskStatus = UserTask.TaskStatus.getStatus(task.getStatus().charAt(0));
        switch (taskStatus) {
            case RUNNING:
                UserTaskTimeSeq currentTimeSeq = userTaskTimeSeqDao.findById(timeSeqList.get(0).getId(), false);
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
        task.setStatus(stop ? UserTask.TaskStatus.STOPPED.getStatusStr() : UserTask.TaskStatus.COMPLETED.getStatusStr());
        task.setUpdateTime(finishTime);
        userTaskDao.merge(task);
        userTaskDao.flush();
        userTaskDao.clear();

        //change user_tak_time current value and if stop - duration
        if (stop) {
            int timeSpentSec = 0;
            for (UserTaskTimeSeq timeSeq : timeSeqList) {
                if (timeSeq.getEndTime() == null && timeSeq.getEndTime().after(timeSeq.getStartTime()))
                    throw new Exception("time sequence should have correct end time");
                long timeDif = (timeSeq.getEndTime().getTime() - timeSeq.getStartTime().getTime()) / 1000;
                timeSpentSec = (int) (timeSpentSec + timeDif);
            }
            currentTime.setDurationSec(timeSpentSec);
        }
        currentTime.setCurrent(false);
        currentTime.setFinishTime(finishTime);
        currentTime = userTaskTimeDao.merge(currentTime);
        userTaskTimeDao.flush();
        userTaskTimeDao.clear();

        //add user_action
        UserAction action = new UserAction();
        action.setTimestamp(finishTime);
        action.setAction(stop ? UserAction.Action.STOP.getActionStr() : UserAction.Action.FINISH.getActionStr());
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
        //only COMPLETED, UNKNOWN and STOPPED allowed
        if (!(userTask.getStatus().equals(UserTask.TaskStatus.COMPLETED.getStatusStr())
                || userTask.getStatus().equals(UserTask.TaskStatus.UNKNOWN.getStatusStr())
                || userTask.getStatus().equals(UserTask.TaskStatus.STOPPED.getStatusStr())))
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
                taskTime.setDurationSec(seconds);
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
                taskTime.setDurationSec(0);
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
        UserTaskTime currentTime = getCurrentTimeForUser(taskId, username);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //close task if time is finished
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
        UserTaskTimeSeq currentTimeSeq = userTaskTimeSeqDao.findById(timeSeqList.get(0).getId(), false);
        currentTimeSeq.setEndTime(now);
        userTaskTimeSeqDao.merge(currentTimeSeq);
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
            throw new Exception("can't resume Task");
        UserTaskTime currentTime = getCurrentTimeForUser(taskId, username);
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
        UserTaskTimeSeq prevTimeSeq = userTaskTimeSeqDao.findById(timeSeqList.get(0).getId(), false);
        timeSeq.setPrevTimeSeq(prevTimeSeq);
        prevTimeSeq.setNextTimeSeq(timeSeq);
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
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        if (userTask == null)
            throw new Exception("can't find user task");
        if (!userTask.getStatus().equals(UserTask.TaskStatus.RUNNING.getStatusStr()))
            throw new Exception("wrong status of task for user");
        if (userTask.getTaskByTaskId().getTaskType() == Task.TaskType.TASK.getType())
            throw new Exception("can't extend Task");
        UserTaskTime currentTime = getCurrentTimeForUser(taskId, username);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //close task if time is finished
        boolean isClosed = checkTask(userTask);
        if (isClosed)
            return;

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
        userAction.setAction(UserAction.Action.EXTEND.getActionStr());
        userAction.setTimestamp(now);
        currentTime.addUserAction(userAction);
        userActionDao.persist(userAction);
        userActionDao.flush();
        userActionDao.clear();
    }

    @Override
    public void stopTask(Long taskId, String username) throws Exception {
        if (taskId == null || username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        if (userTask == null)
            throw new Exception("can't find user task");
        //only PAUSED and RUNNING allowed
        if (!(userTask.getStatus().equals(UserTask.TaskStatus.PAUSED.getStatusStr())
                || userTask.getStatus().equals(UserTask.TaskStatus.RUNNING.getStatusStr())))
            throw new Exception("wrong status of task for user");
        if (userTask.getTaskByTaskId().getTaskType() == Task.TaskType.TASK.getType())
            throw new Exception("can't pause Task");
        UserTaskTime currentTime = getCurrentTimeForUser(taskId, username);
        if (currentTime == null || currentTime.getTimeSeq() == null)
            throw new Exception("task should have current time and time seq");

        //check if task should be ended
        boolean isClosed = checkTask(userTask);
        if (isClosed)
            return;

        //force ending task
        endTask(userTask, new Date(), true);
    }
}
