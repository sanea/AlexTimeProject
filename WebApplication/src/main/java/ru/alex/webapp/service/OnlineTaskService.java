package ru.alex.webapp.service;

/**
 * @author Alexander.Isaenco
 */
public interface OnlineTaskService {

    void checkAllTasks();

    void updateTasksStatus() throws Exception;

}
