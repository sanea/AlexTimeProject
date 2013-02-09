package ru.alex.webapp.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.alex.webapp.service.TaskService;


@Controller
@RequestMapping(value = "/stat")
public class StatController {

    final static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    public String showTasks(Model model) {
        model.addAttribute("taskStatusList", taskService.getAllTaskStatus());
        return "stat/list";
    }

}
