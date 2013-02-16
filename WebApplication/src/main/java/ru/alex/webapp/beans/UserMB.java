package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Users;
import ru.alex.webapp.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.persistence.PostUpdate;
import java.io.Serializable;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@Component
@Scope(value = "view")
public class UserMB implements Serializable {
    static final long serialVersionUID = 1L;

    Logger logger = Logger.getLogger(UserMB.class);

    @Autowired
    UserService userService;

    List<Users> userList;
    private int counter = 0;

    @PostConstruct
    private void init() {
        logger.debug("init");
        userList = userService.getAllUsers();
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void increment() {
        counter++;
    }
}
