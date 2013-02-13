package ru.alex.webapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.TaskDao;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTask;
import ru.alex.webapp.model.UserTaskStatus;
import ru.alex.webapp.model.Users;
import ru.alex.webapp.service.TaskService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<UserTask> getTasksForUser(String username) {
//        List<UserTask> tasks = taskDao.getTasksForUser(username);
//        for (UserTask task : tasks) {
//            List<UserTaskStatus> activeTasks = taskDao.getActiveTasks(username, task.getTaskByTaskId().getId());
//            if (activeTasks.size() > 0)
//                task.getTaskByTaskId().setActiveForUser(true);
//        }
//        return tasks;
        return null;
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void startTask(Long taskId, String username, int seconds) throws Exception {
//        if (taskId == null || taskId.equals("") || username == null || username.equals(""))
//            throw new Exception("Wrong input param");
//        Users user = userDao.getUserByUsername(username);
//        if (user == null)
//            throw new Exception("No user with username " + username);
//        Task task = taskDao.getTask(taskId);
//        if (task == null)
//            throw new Exception("No task with task id " + taskId);
//        UserTask userTask = taskDao.getTaskForUser(username, taskId);
//        if (userTask == null)
//            throw new Exception("User " + username + " doesn't have task " + taskId);
////        List<UserTaskStatus> activeTasks = taskDao.getActiveTasks(username, taskId);
//        if (activeTasks.size() > 0)
//            throw new Exception("Can't start tasks when there is task in progress");
//
//        Timestamp now = new Timestamp(new Date().getTime());
//        UserTaskStatus taskStatus = new UserTaskStatus();
//        taskStatus.setTaskByTaskId(task);
//        taskStatus.setUsersByUsername(user);
//        //taskStatus.setStartTimestamp(now);
//        if (task.getTaskType() == Task.TaskType.Process.getType()) {
//            taskStatus.setStatus(UserTaskStatus.TaskStatus.RUNNING.getStatus());
//        } else if (task.getTaskType() == Task.TaskType.Task.getType()) {
//            taskStatus.setStatus(UserTaskStatus.TaskStatus.COMPLETED.getStatus());
//            //taskStatus.setEndTimestamp(now);
//        }
//        taskDao.addTaskStatus(taskStatus);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void pauseTask(Long taskId, String username) throws Exception {
//        if (taskId == null || taskId.equals("") || username == null || username.equals(""))
//            throw new Exception("Wrong input param");
//        Users user = userDao.getUserByUsername(username);
//        if (user == null)
//            throw new Exception("No user with username " + username);
//        Task task = taskDao.getTask(taskId);
//        if (task == null)
//            throw new Exception("No task with task id " + taskId);
//        UserTask userTask = taskDao.getTaskForUser(username, taskId);
//        if (userTask == null)
//            throw new Exception("User " + username + " doesn't have task " + taskId);
//        List<UserTaskStatus> activeTasks = taskDao.getActiveTasks(username, taskId);
//        if (activeTasks.size() == 0)
//            throw new Exception("No task to end");
//
//        Timestamp now = new Timestamp(new Date().getTime());
//        for (UserTaskStatus activeTask : activeTasks) {
//            //activeTask.setEndTimestamp(now);
//            //long timeSpent = now.getTime() - activeTask.getStartTimestamp().getTime();
//            //activeTask.setTimeSpent((int) timeSpent / 60000);
//            activeTask.setStatus(UserTaskStatus.TaskStatus.COMPLETED.getStatus());
//            taskDao.updateTaskStatus(activeTask);
//        }
    }

    @Override
    public void extendTask(Long taskId, String username, int seconds) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<UserTaskStatus> getAllTaskStatus() {
//        return taskDao.getAllTaskStatus();
        return null;
    }
}
