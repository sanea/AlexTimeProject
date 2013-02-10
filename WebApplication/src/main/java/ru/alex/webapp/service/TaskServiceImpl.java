package ru.alex.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.dao.TaskDao;
import ru.alex.webapp.dao.UserDao;
import ru.alex.webapp.model.TaskEntity;
import ru.alex.webapp.model.UserTaskEntity;
import ru.alex.webapp.model.UserTaskStatusEntity;
import ru.alex.webapp.model.UsersEntity;

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
    public List<UserTaskEntity> getTasksForUser(String username) {
        List<UserTaskEntity> tasks = taskDao.getTasksForUser(username);
        for (UserTaskEntity task : tasks) {
            List<UserTaskStatusEntity> activeTasks = taskDao.getActiveTasks(username, task.getTaskByTaskId().getId());
            if (activeTasks.size() > 0)
                task.getTaskByTaskId().setActiveForUser(true);
        }
        return tasks;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void startTask(Long taskId, String username) throws Exception {
        if (taskId == null || taskId.equals("") || username == null || username.equals(""))
            throw new Exception("Wrong input param");
        UsersEntity user = userDao.getUserByUsername(username);
        if (user == null)
            throw new Exception("No user with username " + username);
        TaskEntity task = taskDao.getTask(taskId);
        if (task == null)
            throw new Exception("No task with task id " + taskId);
        UserTaskEntity userTask = taskDao.getTaskForUser(username, taskId);
        if (userTask == null)
            throw new Exception("User " + username + " doesn't have task " + taskId);
        List<UserTaskStatusEntity> activeTasks = taskDao.getActiveTasks(username, taskId);
        if (activeTasks.size() > 0)
            throw new Exception("Can't start tasks when there is task in progress");

        Timestamp now = new Timestamp(new Date().getTime());
        UserTaskStatusEntity taskStatus = new UserTaskStatusEntity();
        taskStatus.setTaskByTaskId(task);
        taskStatus.setUsersByUsername(user);
        //taskStatus.setStartTimestamp(now);
        if (task.getTaskType() == TaskEntity.TaskType.Process.getType()) {
            taskStatus.setStatus(UserTaskStatusEntity.TaskStatus.RUNNING.getStatus());
        } else if (task.getTaskType() == TaskEntity.TaskType.Task.getType()) {
            taskStatus.setStatus(UserTaskStatusEntity.TaskStatus.COMPLETED.getStatus());
            //taskStatus.setEndTimestamp(now);
        }
        taskDao.addTaskStatus(taskStatus);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void endTask(Long taskId, String username) throws Exception {
        if (taskId == null || taskId.equals("") || username == null || username.equals(""))
            throw new Exception("Wrong input param");
        UsersEntity user = userDao.getUserByUsername(username);
        if (user == null)
            throw new Exception("No user with username " + username);
        TaskEntity task = taskDao.getTask(taskId);
        if (task == null)
            throw new Exception("No task with task id " + taskId);
        UserTaskEntity userTask = taskDao.getTaskForUser(username, taskId);
        if (userTask == null)
            throw new Exception("User " + username + " doesn't have task " + taskId);
        List<UserTaskStatusEntity> activeTasks = taskDao.getActiveTasks(username, taskId);
        if (activeTasks.size() == 0)
            throw new Exception("No task to end");

        Timestamp now = new Timestamp(new Date().getTime());
        for (UserTaskStatusEntity activeTask : activeTasks) {
            //activeTask.setEndTimestamp(now);
            //long timeSpent = now.getTime() - activeTask.getStartTimestamp().getTime();
            //activeTask.setTimeSpent((int) timeSpent / 60000);
            activeTask.setStatus(UserTaskStatusEntity.TaskStatus.COMPLETED.getStatus());
            taskDao.updateTaskStatus(activeTask);
        }
    }

    @Override
    public List<UserTaskStatusEntity> getAllTaskStatus() {
        return taskDao.getAllTaskStatus();
    }
}
