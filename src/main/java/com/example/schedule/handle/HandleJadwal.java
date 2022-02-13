package com.example.schedule.handle;

import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Jadwal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class HandleJadwal {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet =null;

    public void setUpdateJadwal(Jadwal jadwal) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id", jadwal.id_jadwal);
        preferences.put("hari", jadwal.hari);
        preferences.put("mapel", jadwal.mata_pelajaran);
        preferences.put("guru", jadwal.nama_guru);
        preferences.put("kelas", jadwal.nama_kelas);
        preferences.put("mulai", jadwal.mulai);
        preferences.put("selesai", jadwal.selesai);
        preferences.put("ruangan", jadwal.nama_ruangan);
    }

    public static Jadwal getUpdateJadwal() {
        Preferences preferences = Preferences.userRoot();
        var id = preferences.get("id", "String");
        var hari = preferences.get("hari", "String");
        var mapel = preferences.get("mapel", "String");
        var guru = preferences.get("guru", "String");
        var kelas = preferences.get("kelas", "String");
        var mulai = preferences.get("mulai", "String");
        var selesai = preferences.get("selesai", "String");
        var ruangan = preferences.get("ruangan", "String");
        return new Jadwal(id, hari, mapel, guru, kelas, mulai, selesai, ruangan);
    }

    public boolean checkValidation(String kode, String id) {
        connection = SqliteConnection.Connector();
        String validate = "select * from tb_jadwal where hari = ? and id_guru = ?";
        String search = "select * from tb_jadwal where kode_guru = ?";
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
