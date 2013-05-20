package ru.alex.webapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.webapp.model.GroupAuthority;
import ru.alex.webapp.model.User;
import ru.alex.webapp.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author Alex
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    /**
     * Retrieve an account depending on its login this method is not case sensitive.<br>
     * use <code>obtainAccount</code> to match the login to either email, login or whatever is your login logic
     *
     * @param username the account login
     * @return a Spring Security userdetails object that matches the login
     * @throws UsernameNotFoundException when the user could not be found
     * @throws org.springframework.dao.DataAccessException
     *                                   when an error occured while retrieving the account
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername username={}", username);
        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("Empty login");
        }

        User user;
        try {
            user = userService.findById(username);
        } catch (Exception e) {
            logger.info("Can't find user {}", username);
            throw new UsernameNotFoundException("Can't find user '" + username + "'", e);
        }
        logger.debug("loadUserByUsername user={}", user);
        if (user == null || user.isDeleted()) {
            logger.info("Can't find user {}", username);
            throw new UsernameNotFoundException("Can't find user '" + username + "'");
        }

        Collection<GroupAuthority> groupAuthorities = null;
        if (user.getGroupMemberByUsername() != null && user.getGroupMemberByUsername().getGroupByGroupId() != null) {
            groupAuthorities = user.getGroupMemberByUsername().getGroupByGroupId().getGroupAuthorityById();
        }
        logger.debug("loadUserByUsername groupAuthorities={}", groupAuthorities);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (groupAuthorities != null) {
            for (GroupAuthority groupAuthority : groupAuthorities) {
                grantedAuthorities.add(new SimpleGrantedAuthority(groupAuthority.getAuthority()));
            }
        }
        logger.debug("loadUserByUsername grantedAuthorities={}", grantedAuthorities);

        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
                accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
    }

}
