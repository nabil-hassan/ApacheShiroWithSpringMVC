package net.example.service;

import net.example.dao.UserDAO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import java.math.BigInteger;
import java.security.SecureRandom;

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
            user.getSession().setAttribute("loginTime", user.getSession().getStartTimestamp());
        } catch (CredentialsException|UnknownAccountException ex) {
            throw new Exception("Username/password not recognised");
        } catch (LockedAccountException ex) {
            throw new Exception("Account is locked");
        }
    }

    /**
     * Creates a new user account with the specified credentials.
     */
    public void createUser(String username, String password) throws Exception {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password must be specified");
        }

        if (userDAO.userExists(username)) {
            throw new Exception("User: " + username +  "already exists");
        }

        String salt = new BigInteger(250, new SecureRandom()).toString(32);

        Sha256Hash hashedPassword = new Sha256Hash(password, (new SimpleByteSource(salt)).getBytes());

        userDAO.createUser(username, hashedPassword.toHex(), salt);
    }

}
