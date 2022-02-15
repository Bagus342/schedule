package com.example.schedule.handle;

import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Jurusan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class HandleJurusan {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet =null;

    public void setUpdateJurusan(Jurusan jurusan) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", jurusan.id_jurusan);
        preferences.put("jurusan", jurusan.nama_jurusan);
    }

    public static Jurusan getUpdateJurusan() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var jurusan = preferences.get("jurusan", "String");
        return new Jurusan(id, jurusan);
    }

    public boolean checkValidation(String jurusan, String id) {
        connection = SqliteConnection.Connector();
        String validate = "select * from tb_jurusan where nama_jurusan = ? and id_jurusan = ?";
        String search = "select * from tb_jurusan where nama_jurusan = ?";
        try {
            preparedStatement = connection.prepareStatement(validate);
            preparedStatement.setString(1, jurusan);
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
                searching.setString(1, jurusan);
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
