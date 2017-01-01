package net.example.config.context;

import liquibase.integration.spring.SpringLiquibase;
import net.example.dao.UserDAO;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Spring context configuration, related to the application's persistence concerns.
 */
@Configuration
public class PersistenceContext {

    private static final String DATA_SOURCE_NAME = "h2Db";
    private static final String DATA_SOURCE_URL =
            "jdbc:h2:mem:public;LOCK_MODE=0;DB_CLOSE_DELAY=-1;MODE=Oracle;INIT=CREATE SCHEMA IF NOT EXISTS public";

    @Bean
    public DataSource dataSource()  {
         JdbcDataSource ds = new JdbcDataSource();
         ds.setURL(DATA_SOURCE_URL);
         ds.setUser("sa");
         ds.setPassword("sa");

         try {
             Context ctx = new InitialContext();
             ctx.bind(DATA_SOURCE_NAME, ds);
         } catch (NamingException nex) {
             throw new RuntimeException("Could not bind datasource to JNDI context", nex);
         }

         return ds;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "8043");
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server webServer() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8091");
    }

    @Bean
    public SpringLiquibase liquibase() throws NamingException {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setBeanName("liquibase");
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:liquibase/changelog.xml");
        return liquibase;
    }

    @Bean
    public UserDAO userDAO() {
        return new UserDAO(dataSource());
    }


}