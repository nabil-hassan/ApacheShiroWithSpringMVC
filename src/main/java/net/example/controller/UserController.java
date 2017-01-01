package net.example.controller;

import net.example.dao.UserDAO;
import net.example.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Handles login, logout, and user creation requests.
 */
@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final String CREATE_ERROR = "createUserErrorMsg";
    private static final String LOGIN_ERROR = "loginErrorMsg";

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    /**
     * Shows the login page, if the user is not authenticated; otherwise, shows the index page.
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage() {
        LOG.debug("Request received for login page.");

//        Subject user = SecurityUtils.getSubject();
//        if (user.isAuthenticated()) {
//            LOG.debug("User: {} is already authenticated. Returning index.", user.getPrincipal());
//            return "index";
//        }

        // TODO: implement properly using UserService.isLoggedIn()
        return "login";
    }

    /**
     * Logs the user out and returns them to the login screen.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String doLogout() {
        LOG.debug("Received logout request");

        userService.logoutUser();

        return "redirect:/";
    }

    /**
     * Log the user in. If a login error occurs, display to the user.
     */
    @RequestMapping(value = "/loginForm/submit", method = RequestMethod.POST)
    public String doLoginUser(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam(required = false) Boolean rememberMe,
                              RedirectAttributes redirectAttributes) {
        LOG.debug("Login request received for user: {}", username);

        try {
            userService.loginUser(username, password, rememberMe);
        } catch (Exception ex) {
            LOG.debug("Login error for user: " + username, ex);
            redirectAttributes.addFlashAttribute(LOGIN_ERROR, ex.getMessage());
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("password", password);
            return "redirect:/login";
        }

        return "redirect:/";
    }

    /**
     * Create the specified user. If an error occurs, display to the user, otherwise redirect to welcome page.
     */
    @RequestMapping(value = "/createUserForm/submit", method = RequestMethod.POST)
    public String doCreateUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(required = false) Boolean rememberMe,
                               RedirectAttributes redirectAttributes) throws SQLException {
        LOG.debug("Create user request received for user: {}", username);

        if (userDAO.userExists(username)) {
            LOG.debug("User: '{}' already exists. Issuing error message.", username);
            redirectAttributes.addFlashAttribute(CREATE_ERROR, "User: '" + username + "' already exists.");
            redirectAttributes.addFlashAttribute("newUsrUsername", username);
            redirectAttributes.addFlashAttribute("newUsrPassword", password);
            return "redirect:/login";
        }

        userDAO.createUser(username, password);

        try {
            userService.loginUser(username, password, rememberMe);
            return "redirect:/";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(CREATE_ERROR, "Error logging created user in:" + ex.getMessage());
            redirectAttributes.addFlashAttribute("newUsrUsername", username);
            redirectAttributes.addFlashAttribute("newUsrPassword", password);
            return "redirect:/login";
        }

        // TODO: spring mvc session attribute for username
    }

}
