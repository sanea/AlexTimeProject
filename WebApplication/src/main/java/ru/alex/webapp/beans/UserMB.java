package ru.alex.webapp.beans;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alex.webapp.model.Users;
import ru.alex.webapp.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;

/**
 * @author Alexander.Isaenco
 */
@ManagedBean
@RequestScoped
public class UserMB {
    @Autowired
    UserService userService;

    List<Users> userList;

    @PostConstruct
    private void init() {
        userList = userService.getAllUsers();
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }
}
