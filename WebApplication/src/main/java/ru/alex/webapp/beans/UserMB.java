package ru.alex.webapp.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.User;
import ru.alex.webapp.service.UserService;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Component
@Scope(value = "view")
public class UserMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    @Autowired
    UserService userService;
    List<User> userList;

    @PostConstruct
    private void init() {
        logger.debug("init");
        userList = userService.getAllEnabledUsers();
        logger.debug("init userList={}", userList);
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }


}
