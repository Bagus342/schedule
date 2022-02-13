package com.example.schedule.handle;

import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Kelas;
import com.example.schedule.models.Pelajaran;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class HandlePelajaran {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet =null;

    public void setUpdatePelajaran(Pelajaran pelajaran) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", pelajaran.id);
        preferences.put("nama_pelajaran", pelajaran.mapel);
    }

    public static Pelajaran getUpdatePelajaran() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var pelajaran = preferences.get("nama_pelajaran", "String");
        return new Pelajaran(id, pelajaran);
    }

    public boolean checkValidation(String nama_mapel, String id) {
        connection = SqliteConnection.Connector();
        String validate = "select * from tb_mapel where nama_mapel = ? and id_mapel = ?";
        String search = "select * from tb_mapel where nama_mapel = ?";
        try {
            preparedStatement = connection.prepareStatement(validate);
            preparedStatement.setString(1, nama_mapel);
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
                searching.setString(1, nama_mapel);
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
