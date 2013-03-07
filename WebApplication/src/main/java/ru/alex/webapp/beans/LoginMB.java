package ru.alex.webapp.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
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

    public String login() {
        logger.debug("Login username={}, password={}", userName, password);
        try {
            Authentication authRequest = new UsernamePasswordAuthenticationToken(userName, password);
            Authentication auth = am.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.info("Login Success: {}", auth.getName());

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            return "/pages/index?faces-redirect=true";
        } catch (AuthenticationException ex) {
            logger.info("Login Failed");
            FacesContext.getCurrentInstance().addMessage("formLogin", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "User Name and Password Not Match!"));
            return null;
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
