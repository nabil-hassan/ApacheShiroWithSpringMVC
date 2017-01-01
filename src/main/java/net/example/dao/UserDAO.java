package net.example.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Provides access to the application's datasource - specifically, data related to the system's users, and their roles
 * and permissions.
 */
public class UserDAO {

    private static final Logger LOG = LoggerFactory.getLogger(UserDAO.class);
    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, password, password_salt) VALUES(?, ?, ?)";
    private static final String USER_EXISTS_QUERY = " SELECT COUNT(*) FROM users WHERE username = ?";

    private DataSource dataSource;

    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createUser(String username, String password, String salt) throws SQLException {
        LOG.debug("Create user : '{}'", username);

        Connection connection = dataSource.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement(CREATE_USER_QUERY);
            query.setString(1, username);
            query.setString(2, password);
            query.setString(3, salt);

            query.executeUpdate();
        } catch(SQLException ex) {
            LOG.error("Exception encountered performing createUser() query", ex);
            throw ex;
        } finally {
            connection.close();
        }
    }

    public boolean userExists(String username) throws SQLException {
        LOG.debug("Checking if user with username: '{}' exists", username);

        Connection connection = dataSource.getConnection();

        try {
            PreparedStatement query = connection.prepareStatement(USER_EXISTS_QUERY);
            query.setString(1, username);

            ResultSet rs = query.executeQuery();
            rs.next();

            return rs.getInt(1) > 0;
        } catch(SQLException ex) {
            LOG.error("Exception encountered performing userExists() query", ex);
            throw ex;
        } finally {
            connection.close();
        }
    }




}
