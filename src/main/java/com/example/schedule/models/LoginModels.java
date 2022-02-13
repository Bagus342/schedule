package com.example.schedule.models;

import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.session.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModels {
    Connection connection;

    public LoginModels () {
        connection = SqliteConnection.Connector();
        if (connection == null) {
            System.out.println("connection not successful");
            System.exit(1);
        }
    }

    public boolean isLogin(String nis, String pass) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from tb_user where nis = ? and password = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nis);
            preparedStatement.setString(2, pass);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                preparedStatement.close();
                resultSet.close();
                return true;
            } else {
                preparedStatement.close();
                resultSet.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
            return false;
        }
    }

    public User session(String nis) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from tb_user where nis = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nis);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var nama_user = resultSet.getString("nama_user");
                var kelas = resultSet.getString("kelas");
                preparedStatement.close();
                resultSet.close();
                return new User(nama_user, kelas);
            } else {
                preparedStatement.close();
                resultSet.close();
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getCause());
            return null;
        }
    }

    public boolean role(String nis, String role) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from tb_user where nis = ? and role = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nis);
            preparedStatement.setString(2, role);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                preparedStatement.close();
                resultSet.close();
                return true;
            } else {
                preparedStatement.close();
                resultSet.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
            return false;
        }
    }

}
