package com.example.schedule.connection;

import java.sql.*;

public class SqliteConnection {

    public static Connection Connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:schedule.db");
            return connection;
        } catch (Exception e) {
            return null;
//            e.getCause();
        }
    }
}
