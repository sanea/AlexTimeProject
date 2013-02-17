package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.beans.wrappers.TaskWrapper;
import ru.alex.webapp.model.Task;
import ru.alex.webapp.service.TaskService;
import ru.alex.webapp.service.UserService;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

/**
 * @author Alex
 */
@Component
@Scope(value = "view")
public class TaskMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UserMB.class);
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    private String userName;
    private List<TaskWrapper> assignedTasks;

    @PostConstruct
    private void init() {
        userName = SecurityContextHolder.getContext().getAuthentication().getName();
        assignedTasks = taskService.getTasksForUser(userName);
    }

    public List<TaskWrapper> getAssignedTasks() {
        return assignedTasks;
    }

}

