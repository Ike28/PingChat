package com.map222.sfmc.socialnetworkfx.domain.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private Connection connection;
    private static ConnectionFactory instance;

    private ConnectionFactory() throws SQLException {
        connection = DriverManager.getConnection
                (DatabaseCredentials.getUrl(), DatabaseCredentials.getUsername(), DatabaseCredentials.getPassword());
    }

    /**
     * Returns the active instance of the app's SQL connection, or instantiates one if none exists yet
     * @return an instance of the connection singleton
     * @throws SQLException -thrown by java.sql Connection class in case of connection error
     */
    public static ConnectionFactory getInstance() throws SQLException {
        if(instance == null)
            instance = new ConnectionFactory();
        return instance;
    }

    /**
     * Returns the active SQL connection
     * @return a Connection object providing access to the DB
     */
    public Connection getConnection() {
        return this.connection;
    }
}
