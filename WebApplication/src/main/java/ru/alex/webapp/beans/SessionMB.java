package ru.alex.webapp.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alex.webapp.model.Site;
import ru.alex.webapp.model.User;
import ru.alex.webapp.model.enums.CustomActionEnum;
import ru.alex.webapp.service.UserService;
import ru.alex.webapp.util.CustomActionConfiguration;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alex
 */
@Component
@Scope(value = "session")
public class SessionMB implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SessionMB.class);
    @Autowired
    private UserService userService;
    private Set<String> authorities;
    private Site selectedSite;
    private User currentUser;

    @PostConstruct
    private void init() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String userName = auth.getName();
            Collection<? extends GrantedAuthority> grantedAuthorities = auth.getAuthorities();
            authorities = new HashSet<>(grantedAuthorities.size());
            for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                authorities.add(grantedAuthority.getAuthority());
            }
            currentUser = userService.findById(userName);
            if (currentUser.getCurrentChange() != null)
                selectedSite = currentUser.getCurrentChange().getSite();
        }
        logger.debug("currentUser={}, authorities={}", currentUser, authorities);
    }

    public boolean hasRole(String roleName) {
        boolean result = false;
        if (authorities != null) {
            result = authorities.contains(roleName);
        }
        logger.debug("hasRole {} {}", roleName, result);
        return result;
    }

    public Site getSelectedSite() {
        return selectedSite;
    }

    public void setSelectedSite(Site selectedSite) {
        this.selectedSite = selectedSite;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getCustomActionName(int id) {
        CustomActionEnum customActionEnum = CustomActionEnum.getCustomAction(id);
        return CustomActionConfiguration.getInstance().getCustomAction(customActionEnum).getName();
    }

    public boolean getCustomActionEnabled(int id) {
        CustomActionEnum customActionEnum = CustomActionEnum.getCustomAction(id);
        return CustomActionConfiguration.getInstance().getCustomAction(customActionEnum).getEnabled();
    }
}
