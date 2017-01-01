package net.example.config.web;

import net.example.config.context.PersistenceContext;
import net.example.config.context.SecurityContext;
import net.example.config.context.WebContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

/**
 * Initialiser for the web application - uses Java Servlet 3 approach to web app configuration.
 */
public class AppWebIntializer implements WebApplicationInitializer {

    /**
     * Programmatically defines the application's servlet context, as well as bootstrapping Spring IOC.
     *
     * @param servletContext the servlet context
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        // Bootstrap Spring IOC
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.setServletContext(servletContext);
        appContext.register(PersistenceContext.class);
        appContext.register(SecurityContext.class);
        appContext.register(WebContext.class);
        servletContext.addListener(new ContextLoaderListener(appContext));

        // Register DispatcherServlet.
        Servlet dispatcherServlet = new DispatcherServlet(appContext);
        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("dispatcher", dispatcherServlet);
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");

        // Register Apache Shiro Filter
        Filter shiroFilter = new DelegatingFilterProxy("shiroFilter", appContext);
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("shiroFilter", shiroFilter);
        filterRegistration.setInitParameter("targetFilterLifecycle", "true");
        filterRegistration.addMappingForUrlPatterns(null, false, "/*");
    }
}
