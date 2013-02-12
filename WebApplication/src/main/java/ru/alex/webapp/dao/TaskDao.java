package ru.alex.webapp.dao;

import ru.alex.webapp.model.TaskEntity;
import ru.alex.webapp.model.UserTaskEntity;
import ru.alex.webapp.model.UserTaskStatusEntity;

import java.util.List;

public interface TaskDao {
    void addTak(TaskEntity task);

    void updateTask(TaskEntity task);

    void deleteTask(TaskEntity task);

    TaskEntity getTask(long id);

    List<UserTaskEntity> getTasksForUser(String username);

    UserTaskEntity getTaskForUser(String username, Long taskId);

    List<TaskEntity> getAllTasks();

    void addTaskStatus(UserTaskStatusEntity entity);

    void updateTaskStatus(UserTaskStatusEntity entity);

    List<UserTaskStatusEntity> getActiveTask(String username, Long taskId);

    List<UserTaskStatusEntity> getActiveTasks(String username);

    List<UserTaskStatusEntity> getAllTaskStatus();
}
