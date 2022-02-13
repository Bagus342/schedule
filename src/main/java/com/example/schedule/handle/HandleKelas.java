package com.example.schedule.handle;

import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Kelas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class HandleKelas {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet =null;

    public void setUpdateKelas(Kelas kelas) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", kelas.id);
        preferences.put("kelas", kelas.nama_kelas);
    }

    public static Kelas getUpdateKelas() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var kelas = preferences.get("kelas", "String");
        return new Kelas(id, kelas);
    }

    public boolean checkValidation(String kelas, String id) {
        connection = SqliteConnection.Connector();
        String validate = "select * from tb_kelas where nama_kelas = ? and id_kelas = ?";
        String search = "select * from tb_kelas where nama_kelas = ?";
        try {
            preparedStatement = connection.prepareStatement(validate);
            preparedStatement.setString(1, kelas);
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
                searching.setString(1, kelas);
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
