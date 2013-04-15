package ru.alex.webapp.service;

import ru.alex.webapp.model.Task;

import java.util.List;

public interface TaskService extends GenericService<Task, Long> {

    boolean isTaskEditable(Task task) throws Exception;

    List<Task> getEnabledNotDeletedTasks() throws Exception;

}
