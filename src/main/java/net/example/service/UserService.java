package net.example.service;

import net.example.dao.UserDAO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;

public class UserService {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Confirms whether the user is authenticated.
     */
    public boolean userIsAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    /**
     * Attempts to login user; throws an Exception if unable to.
     */
    public void loginUser(String username, String password, Boolean rememberUser) throws Exception {
        LOG.debug("Attempting to login user: {}", username);

        if (username == null || password == null || username.length() == 0 || password.length() == 0) {
            throw new IllegalArgumentException("Username and password must be specified");
        }

        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, Boolean.TRUE.equals(rememberUser));

        try {
            user.login(token);
            user.getSession().setAttribute("username", username);
        } catch (CredentialsException|UnknownAccountException ex) {
            throw new Exception("Username/password not recognised");
        } catch (LockedAccountException ex) {
            throw new Exception("Account is locked");
        }
    }

    /**
     * Logs the current user out of the system.
     */
    public void logoutUser() {
        Subject user = SecurityUtils.getSubject();

        LOG.debug("Logging current user: {} out of system", user.getPrincipal());

        SecurityUtils.getSubject().logout();

        LOG.debug("User successfully logged out");
    }
}
