package com.example.banking_app.config;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            Properties databaseProp = new Properties();
            try {
                databaseProp.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("JDBCcredentials.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection("jdbc:mysql://" + databaseProp.getProperty("databaseIP") + ":" + databaseProp.getProperty("databasePort") +
                    "/" + databaseProp.getProperty("databaseName") + "?user=" + databaseProp.getProperty("databaseUsername") + "&password=" + databaseProp.getProperty("databasePassword"));
        }
        return connection;
    }
}