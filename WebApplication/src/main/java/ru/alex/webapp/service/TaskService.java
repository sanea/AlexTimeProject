package ru.alex.webapp.service;

import ru.alex.webapp.model.Task;
import ru.alex.webapp.model.UserSiteTask;
import ru.alex.webapp.model.UserTaskTime;

import java.util.Date;
import java.util.List;

public interface TaskService extends GenericService<Task, Long> {

    boolean isTaskEditable(Task task) throws Exception;

    List<Task> getEnabledNotDeletedTasks() throws Exception;

    //------------------------

    List<UserSiteTask> getTasksForUser(String username) throws Exception;

    List<UserSiteTask> getOnlineTasks() throws Exception;

    List<UserSiteTask> getUsersForTask(Long taskId) throws Exception;

    UserTaskTime getCurrentTimeForUserTask(Long taskId, String username) throws Exception;

    List<UserTaskTime> getAllNotCurrentTime(Date from, Date to) throws Exception;

    int getTimeSpentSecForUserTask(Long taskId, String username) throws Exception;

    void startTask(Long taskId, String username, int seconds) throws Exception;

    void pauseTask(Long taskId, String username) throws Exception;

    void resumeTask(Long taskId, String username) throws Exception;

    void extendTask(Long taskId, String username, int seconds) throws Exception;

    void stopTask(Long taskId, String username) throws Exception;

    void updateUserTask(Long taskId, String username, boolean assigned) throws Exception;

    void checkAllTasks();
}
