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
import ru.alex.webapp.service.TaskService;


@Controller
@RequestMapping(value = "/task")
public class TaskController {

    final static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    public String showTasks(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("taskList", taskService.getTasksForUser(user.getUsername()));
        return "task/userTask";
    }

    @RequestMapping(value = "/start/{taskId}", method = RequestMethod.GET)
    public String startTask(@PathVariable String taskId, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            taskService.startTask(Long.parseLong(taskId), user.getUsername());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        model.addAttribute("taskList", taskService.getTasksForUser(user.getUsername()));
        return "task/userTask";
    }

    @RequestMapping(value = "/end/{taskId}", method = RequestMethod.GET)
    public String endTask(@PathVariable String taskId, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            taskService.endTask(Long.parseLong(taskId), user.getUsername());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        model.addAttribute("taskList", taskService.getTasksForUser(user.getUsername()));
        return "task/userTask";
    }

}
