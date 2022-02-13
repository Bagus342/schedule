package com.example.schedule.handle;

import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Ruangan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class HandleRuangan {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet =null;

    public void setUpdateRuangan(Ruangan ruangan) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", ruangan.id_ruangan);
        preferences.put("ruangan", ruangan.nama_ruangan);
    }

    public static Ruangan getUpdateRuangan() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var ruangan = preferences.get("ruangan", "String");
        return new Ruangan(id, ruangan);
    }

    public boolean checkValidation(String ruangan, String id) {
        connection = SqliteConnection.Connector();
        String validate = "select * from tb_ruangan where nama_ruangan = ? and id_ruangan = ?";
        String search = "select nama_ruangan from tb_ruangan where nama_ruangan = ?";
        try {
            preparedStatement = connection.prepareStatement(validate);
            preparedStatement.setString(1, ruangan);
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
                searching.setString(1, ruangan.toUpperCase());
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
