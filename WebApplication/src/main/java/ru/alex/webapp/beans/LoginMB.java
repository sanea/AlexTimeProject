package ru.alex.webapp.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * @author Alexander.Isaenco
 */
@Component
@Scope(value = "session")
public class LoginMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LoginMB.class);
    private String userName;
    private String password;
    @Resource(name = "authenticationManager")
    private AuthenticationManager am;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication() != null ? String.valueOf(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) : "null";
    }

    public String login() throws java.io.IOException {
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(this.getUserName(), getPassword());
            Authentication result = am.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            logger.info("Login Success: {}", result.getName());
            return "/pages/index?faces-redirect=true";
        } catch (AuthenticationException ex) {
            System.out.println("Login Failed");
            FacesContext.getCurrentInstance().addMessage("formLogin", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "User Name and Password Not Match!"));
            return "/pages/login";
        }
    }

    public String getLogoutHidden() {
        SecurityContextHolder.getContext().setAuthentication(null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "logout";
    }

    public void setLogoutHidden(String logoutHidden) {
        //empty
    }
}
