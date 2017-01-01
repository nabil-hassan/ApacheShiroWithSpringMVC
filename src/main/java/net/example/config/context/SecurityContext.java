package net.example.config.context;

import net.example.dao.UserDAO;
import net.example.service.UserService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;


/**
 * Spring context related to the application's security concerns.
 */
@Configuration
@Import(PersistenceContext.class)
public class SecurityContext {

    @Autowired
    private PersistenceContext persistenceContext;

    @Bean
    public AbstractShiroFilter shiroFilter(DataSource dataSource) throws Exception {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setFilterChainDefinitions(getFilterChainDefinitionList());
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setSecurityManager(securityManager(dataSource));
        shiroFilter.getFilters().put("logout", logout());
        return (AbstractShiroFilter) shiroFilter.getObject();
    }

    @Bean
    public LogoutFilter logout() {
        LogoutFilter logout = new LogoutFilter();
        logout.setRedirectUrl("/");
        return logout;
    }

    @Bean
    public SecurityManager securityManager(DataSource dataSource) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jdbcRealm(dataSource));
        return securityManager;
    }

    @Bean
    public Realm jdbcRealm(DataSource dataSource) {
        JdbcRealm realm = new JdbcRealm();
        realm.setDataSource(dataSource);
        return realm;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public UserService userService(UserDAO userDAO) {
        return new UserService(userDAO);
    }

    private String getFilterChainDefinitionList() {
        return new StringBuilder()
                .append("/logout = logout").append("\n")
                .append("/* = authc")
                .toString();
    }

}
