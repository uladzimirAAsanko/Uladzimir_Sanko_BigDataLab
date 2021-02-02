package by.sanko.epamLab.task3.dao.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionPool {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    private static final String FILE_DATABASE_CONFIG = "database";
    private static final String DriverName = "DriverClassName";
    private static final String JdbcUrl = "JdbcUrl";
    private static final String UserName = "UserName";
    private static final String password = "Password";

    static {
        ResourceBundle bundle = ResourceBundle.getBundle(FILE_DATABASE_CONFIG);
        config.setDriverClassName(bundle.getString(DriverName));
        config.setJdbcUrl(bundle.getString(JdbcUrl));
        config.setUsername(bundle.getString(UserName));
        config.setPassword(bundle.getString(password));
        ds = new HikariDataSource( config );
    }

    private ConnectionPool(){}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
