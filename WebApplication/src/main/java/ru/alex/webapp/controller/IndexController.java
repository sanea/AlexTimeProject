package ru.alex.webapp.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class IndexController {

    private static final Logger logger = Logger.getLogger(IndexController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        logger.info("index");

        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(@RequestParam(value = "login_error", required = false) String login_error,
                               @RequestParam(value = "forgot", required = false) String forgot, Model model) {
        logger.debug("getLoginPage error=" + login_error + ", forgot=" + forgot);
        if (forgot != null) {
            return "redirect:/";
        }
        if (login_error != null) {
            model.addAttribute("error", "true");
        }
        return "login";
    }
}
