package com.example.banking_app.config;

import android.util.Log;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection connection;

    public DatabaseConnection() {}

    public ResultSet getCards() throws SQLException{
        //Connection con = getConnection();
        Properties databaseProp = new Properties();
        try {
            databaseProp.load(getClass().getClassLoader().getResourceAsStream("JDBCcredentials.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //lookup the mysql module
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("ClassTag", "Failed1");
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://" + databaseProp.getProperty("databaseIP") + ":" + databaseProp.getProperty("databasePort") +
                    "/" + databaseProp.getProperty("databaseName") + "?user=" + databaseProp.getProperty("databaseUsername") + "&password=" + databaseProp.getProperty("databasePassword"));
            Statement stmt = con.createStatement();
            return stmt.executeQuery(
                    "select * from CARD;"
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
            Log.d("SQLTag", "Failed to execute");
        }
        return null;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            Properties databaseProp = new Properties();
            try {
                databaseProp.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("JDBCcredentials.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                //lookup the mysql module
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }
            connection = DriverManager.getConnection("jdbc:mysql://" + databaseProp.getProperty("databaseIP") + ":" + databaseProp.getProperty("databasePort") +
                    "/" + databaseProp.getProperty("databaseName") + "?user=" + databaseProp.getProperty("databaseUsername") + "&password=" + databaseProp.getProperty("databasePassword"));
        }
        return connection;
    }
}
