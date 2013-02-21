package ru.alex.webapp.beans;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Alex
 */
@Component
@Scope(value = "session")
public class MenuMB implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MenuMB.class);
    private String userName;
    private Collection<? extends GrantedAuthority> authorities;

    @PostConstruct
    private void init() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userName = auth.getName();
        authorities = auth.getAuthorities();
        logger.debug("userName= " + userName + ", authorities=" + authorities);
    }

    public String getUserName() {
        return userName;
    }

    public boolean hasRole(String roleName) {
        boolean result = false;
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(roleName)) {
                result = true;
                break;
            }
        }
        logger.debug("hasRole " + roleName + " " + result);
        return result;
    }

}
