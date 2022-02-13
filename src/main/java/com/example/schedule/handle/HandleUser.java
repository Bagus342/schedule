package com.example.schedule.handle;

import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.DataUser;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class HandleUser {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet =null;

    public void setUpdateUser(DataUser user) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", user.id);
        preferences.put("nama_user", user.nama_user);
        preferences.put("nis", user.nis);
        preferences.put("kelas", user.kelas);
        preferences.put("password", user.password);
        preferences.put("role", user.role);
    }

    public static DataUser getUpdateUser() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var nama_user = preferences.get("nama_user", "String");
        var nis = preferences.get("nis", "String");
        var kelas = preferences.get("kelas", "String");
        var password = preferences.get("password", "String");
        var role = preferences.get("role", "String");
        return new DataUser(id, nama_user, nis, kelas, password, role);
    }

    public boolean checkValidation(String nis_siswa, String id) {
        connection = SqliteConnection.Connector();
        String validate = "select * from tb_user where nis = ? and id_user = ?";
        String search = "select * from tb_user where nis = ?";
        try {
            preparedStatement = connection.prepareStatement(validate);
            preparedStatement.setString(1, nis_siswa);
            preparedStatement.setString(2, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                return true;
            } else {
                resultSet.close();
                preparedStatement.close();
                var searching = connection.prepareStatement(search);
                ResultSet searched = null;
                searching.setString(1, nis_siswa);
                searched = searching.executeQuery();
                if (searched.next()) {
                    searching.close();
                    searched.close();
                    return false;
                } else {
                    searching.close();
                    searched.close();
                    return true;
                }
            }
            } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
