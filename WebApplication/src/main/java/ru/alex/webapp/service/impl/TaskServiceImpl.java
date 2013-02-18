package ru.alex.webapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.beans.wrappers.TaskWrapper;
import ru.alex.webapp.dao.*;
import ru.alex.webapp.dao.impl.GenericDaoImpl;
import ru.alex.webapp.dao.impl.UserTaskDaoImpl;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserAction;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskTime;
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
    private UserActionDao userActionDao;


    @Override
    public List<UserTask> getTasksForUser(String username) {
        if (username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong username");
        List<UserTask> tasks = userTaskDao.getTasksForUser(username);
        return tasks;
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void startTask(Long taskId, String username, int seconds) throws Exception {
        if (taskId == null || username == null || username.equals("") || seconds <= 0)
            throw new IllegalArgumentException("Wrong Input Params for starting task");
        UserTask userTask = userTaskDao.getTaskForUser(username, taskId);
        if (userTask == null)
            throw new Exception("can't find user task");
        if (!(userTask.getStatus().equals(UserTask.TaskStatus.COMPLETED.getStatusStr())
                || userTask.getStatus().equals(UserTask.TaskStatus.UNKNOWN.getStatusStr())))
            throw new Exception("wrong status of task for user");

        //change user_task status
        userTask.setStatus(UserTask.TaskStatus.RUNNING.getStatusStr());

        //add user_task_time
        UserTaskTime taskTime = new UserTaskTime();
        taskTime.setTimeSpent(0);
        Calendar now = Calendar.getInstance();
        taskTime.setStartTime(now.getTime());
        now.add(Calendar.SECOND, seconds);
        taskTime.setFinishTime(now.getTime());
        userTask.addUserTaskTime(taskTime);
        userTaskDao.makePersistent(userTask);

        //add user_action
        UserAction userAction = new UserAction();
        userAction.setAction(UserAction.Action.START.getActionStr());
        userAction.setTimeSeconds(seconds);
        userAction.setTimestamp(new Date());
        taskTime.addUserAction(userAction);
        userTaskTimeDao.makePersistent(taskTime);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void pauseTask(Long taskId, String username) throws Exception {
        //TODO
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void resumeTask(Long taskId, String username) throws Exception {
        //TODO
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
