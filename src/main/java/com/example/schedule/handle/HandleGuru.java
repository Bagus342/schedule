package com.example.schedule.handle;

import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Guru;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class HandleGuru {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet =null;

    public void setUpdateGuru(Guru guru) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", guru.id_guru);
        preferences.put("guru", guru.nama_guru);
        preferences.put("kode", guru.kode_guru);
        preferences.put("mapel", guru.mapel);
    }

    public static Guru getUpdateGuru() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var guru = preferences.get("guru", "String");
        var kode = preferences.get("kode", "String");
        var mapel = preferences.get("mapel", "String");
        return new Guru(id, guru, kode, mapel);
    }

    public boolean checkValidation(String kode, String id) {
        connection = SqliteConnection.Connector();
        String validate = "select * from tb_guru where kode_guru = ? and id_guru = ?";
        String search = "select * from tb_guru where kode_guru = ?";
        try {
            preparedStatement = connection.prepareStatement(validate);
            preparedStatement.setString(1, kode);
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
                searching.setString(1, kode);
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
