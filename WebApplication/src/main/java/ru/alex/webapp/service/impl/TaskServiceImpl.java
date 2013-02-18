package ru.alex.webapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.beans.wrappers.TaskWrapper;
import ru.alex.webapp.dao.*;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserTaskTime;
import ru.alex.webapp.service.TaskService;

import java.util.ArrayList;
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
    public List<TaskWrapper> getTasksForUser(String username) {
        if (username == null || username.equals(""))
            throw new IllegalArgumentException("Wrong username");
        List<Task> tasks = taskDao.getTasksForUser(username);
        List<TaskWrapper> taskWrappers = new ArrayList<TaskWrapper>(tasks.size());
        for (Task task : tasks) {
            taskWrappers.add(new TaskWrapper(task));
        }
        return taskWrappers;
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void startTask(Long taskId, String username, int seconds) throws Exception {
//        if (taskId == null || taskId.equals("") || username == null || username.equals(""))
//            throw new Exception("Wrong input param");
//        User user = userDao.getUserByUsername(username);
//        if (user == null)
//            throw new Exception("No user with username " + username);
//        TASK task = taskDao.getTask(taskId);
//        if (task == null)
//            throw new Exception("No task with task id " + taskId);
//        UserTask userTask = taskDao.getTaskForUser(username, taskId);
//        if (userTask == null)
//            throw new Exception("User " + username + " doesn't have task " + taskId);
////        List<UserTaskTime> activeTasks = taskDao.getActiveTasks(username, taskId);
//        if (activeTasks.size() > 0)
//            throw new Exception("Can't start tasks when there is task in progress");
//
//        Timestamp now = new Timestamp(new Date().getTime());
//        UserTaskTime taskStatus = new UserTaskTime();
//        taskStatus.setTaskByTaskId(task);
//        taskStatus.setUserByUsername(user);
//        //taskStatus.setStartTimestamp(now);
//        if (task.getTaskType() == TASK.TaskType.PROCESS.getType()) {
//            taskStatus.setStatus(UserTaskTime.TaskStatus.RUNNING.getStatus());
//        } else if (task.getTaskType() == TASK.TaskType.TASK.getType()) {
//            taskStatus.setStatus(UserTaskTime.TaskStatus.COMPLETED.getStatus());
//            //taskStatus.setEndTimestamp(now);
//        }
//        taskDao.addTaskStatus(taskStatus);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void pauseTask(Long taskId, String username) throws Exception {
//        if (taskId == null || taskId.equals("") || username == null || username.equals(""))
//            throw new Exception("Wrong input param");
//        User user = userDao.getUserByUsername(username);
//        if (user == null)
//            throw new Exception("No user with username " + username);
//        TASK task = taskDao.getTask(taskId);
//        if (task == null)
//            throw new Exception("No task with task id " + taskId);
//        UserTask userTask = taskDao.getTaskForUser(username, taskId);
//        if (userTask == null)
//            throw new Exception("User " + username + " doesn't have task " + taskId);
//        List<UserTaskTime> activeTasks = taskDao.getActiveTasks(username, taskId);
//        if (activeTasks.size() == 0)
//            throw new Exception("No task to end");
//
//        Timestamp now = new Timestamp(new Date().getTime());
//        for (UserTaskTime activeTask : activeTasks) {
//            //activeTask.setEndTimestamp(now);
//            //long timeSpent = now.getTime() - activeTask.getStartTimestamp().getTime();
//            //activeTask.setTimeSpent((int) timeSpent / 60000);
//            activeTask.setStatus(UserTaskTime.TaskStatus.COMPLETED.getStatus());
//            taskDao.updateTaskStatus(activeTask);
//        }
    }

    @Override
    public void extendTask(Long taskId, String username, int seconds) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<UserTaskTime> getAllTaskStatus() {
//        return taskDao.getAllTaskStatus();
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskDao.findAll();
    }

    @Override
    public void saveTask(Task task) throws Exception{
        taskDao.makePersistent(task);
    }

    @Override
    public void deleteTask(Task task) throws Exception{

        taskDao.makeTransient(task);
    }
}
