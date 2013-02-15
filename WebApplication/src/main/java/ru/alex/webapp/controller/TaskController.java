package ru.alex.webapp.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alex.webapp.service.TaskService;


@Controller
@RequestMapping(value = "/task")
public class TaskController {

    final static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    public String showTasks(Model model) {
        logger.debug("showTasks for user ");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.debug(user);
        model.addAttribute("taskList", taskService.getTasksForUser(user.getUsername()));
        return "task/userTask";
    }

    @RequestMapping(value = "/start/{taskId}", method = RequestMethod.GET)
    public String startTask(@PathVariable String taskId, @RequestParam int timeMinutes, Model model) {
        logger.debug("startTask " + taskId + " " + timeMinutes);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            taskService.startTask(Long.parseLong(taskId), user.getUsername(), timeMinutes * 60);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        model.addAttribute("taskList", taskService.getTasksForUser(user.getUsername()));
        return "task/userTask";
    }

    @RequestMapping(value = "/pause/{taskId}", method = RequestMethod.GET)
    public String pauseTask(@PathVariable String taskId, Model model) {
        logger.debug("pauseTask " + taskId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            taskService.pauseTask(Long.parseLong(taskId), user.getUsername());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        model.addAttribute("taskList", taskService.getTasksForUser(user.getUsername()));
        return "task/userTask";
    }

    @RequestMapping(value = "/resume/{taskId}", method = RequestMethod.GET)
    public String resumeTask(@PathVariable String taskId, Model model) {
        logger.debug("resumeTask " + taskId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            taskService.pauseTask(Long.parseLong(taskId), user.getUsername());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        model.addAttribute("taskList", taskService.getTasksForUser(user.getUsername()));
        return "task/userTask";
    }

    @RequestMapping(value = "/extend/{taskId}", method = RequestMethod.GET)
    public String extendTask(@PathVariable String taskId, @RequestParam int timeMinutes, Model model) {
        logger.debug("extendTask " + taskId + " " + timeMinutes);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            taskService.extendTask(Long.parseLong(taskId), user.getUsername(), timeMinutes * 60);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        model.addAttribute("taskList", taskService.getTasksForUser(user.getUsername()));
        return "task/userTask";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String listEditTask(Model model) {
        logger.debug("listEditTask");
        try {

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "task/list";
    }

    @RequestMapping(value = "/edit/{taskId}", method = RequestMethod.GET)
    public String editTask(@PathVariable String taskId, Model model) {

        return "task/edit";
    }

}
