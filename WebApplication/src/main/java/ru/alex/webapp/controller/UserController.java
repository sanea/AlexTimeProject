package ru.alex.webapp.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.alex.webapp.model.Users;
import ru.alex.webapp.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    final static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String showUsers(Model model) {
        model.addAttribute("userList", userService.getAllUsers());
        return "user/list";
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET, headers = {"Accept=text/html"})
    public String showUser(@PathVariable String username, Model model) {
        Users user = userService.getUser(username);
        model.addAttribute(user);
        return "user/view";
    }

}