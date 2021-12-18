package MTG.MTG.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnector {
    private Connection connection;
    private static DbConnector dbConnectorInstance;

    private DbConnector() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "lug4ru");
        connectionProps.put("password", "molina12");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase?serverTimezone=Europe/Warsaw&useSSL=False"
                ,connectionProps);
    }
    public static DbConnector getInstance() throws SQLException {
        if(dbConnectorInstance==null) {
            dbConnectorInstance = new DbConnector();
        }
        return dbConnectorInstance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static DbConnector getDbConnectorInstance() {
        return dbConnectorInstance;
    }
}
