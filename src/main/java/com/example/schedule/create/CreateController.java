package com.example.schedule.create;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CreateController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement;
    ResultSet resultSet = null;

    @FXML
    private ComboBox<String> kode = new ComboBox<>();

    @FXML
    private TextField guru = new TextField();

    @FXML
    private ComboBox<String> mapel = new ComboBox<>();

    @FXML
    private ComboBox<String> kelas = new ComboBox<>();

    @FXML
    private ComboBox<String> ruangan = new ComboBox<>();

    @FXML
    private ComboBox<String> hari = new ComboBox<>();

    @FXML
    private ComboBox<String> mulai = new ComboBox<>();

    @FXML
    private ComboBox<String> selesai = new ComboBox<>();

    ObservableList<String> hariItems = FXCollections.observableArrayList("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu");
    String[] array = {"1", "2", "3", "4", "5", "6", "7", "8"};
    ObservableList<String> mulaiItems = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8");
    ObservableList<String> selesaiItems = FXCollections.observableArrayList();
    ObservableList<String> kodeItems = FXCollections.observableArrayList();
    ObservableList<String> mapelItems = FXCollections.observableArrayList();
    ObservableList<String> kelasItems = FXCollections.observableArrayList();
    ObservableList<String> ruanganItems = FXCollections.observableArrayList();

    public void setHariItems() {
        hari.setItems(hariItems);
    }

    public void setMulaiItems() {
        mulai.setItems(mulaiItems);
    }

    public void setSelesai(ActionEvent event) {
        selesaiItems.clear();
        int getIndex = Arrays.asList(array).indexOf(mulai.getValue());
        for (int i = getIndex + 1; i < array.length; i++) {
            selesaiItems.add(array[i]);
        }
        selesai.setItems(selesaiItems);
    }

    public void setKode(ActionEvent event) {
        String query = "select * from tb_guru where kode_guru = ?";
        connection = SqliteConnection.Connector();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kode.getValue());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                guru.setText(resultSet.getString("nama_guru"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setKodeItems() {
        String query = "select * from tb_guru";
        connection = SqliteConnection.Connector();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                kodeItems.add(resultSet.getString("kode_guru"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kode.setItems(kodeItems);
    }

    public void setMapelItems() {
        String query = "select * from tb_mapel";
        connection = SqliteConnection.Connector();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                mapelItems.add(resultSet.getString("nama_mapel"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        mapel.setItems(mapelItems);
    }

    public void setKelasItems() {
        String query = "select * from tb_kelas";
        connection = SqliteConnection.Connector();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                kelasItems.add(resultSet.getString("nama_kelas"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kelas.setItems(kelasItems);
    }

    public void setRuanganItems() {
        String query = "select * from tb_ruangan";
        connection = SqliteConnection.Connector();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                ruanganItems.add(resultSet.getString("nama_ruangan"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ruangan.setItems(ruanganItems);
    }

    public void backScene(ActionEvent event) throws IOException {
        Main init = new Main();
        init.changeScene("data/schedule_management.fxml");
    }

    public void submitData(ActionEvent event) throws IOException {
        Main init = new Main();
        connection = SqliteConnection.Connector();
        String query = "insert into tb_jadwal (kode, guru, mapel, kelas, ruangan, hari, jam_mulai, jam_selesai) values (?, ?, ?, ?, ?, ?, ?, ?)";
        String validate = "select * from tb_jadwal where hari = ? and kode = ? and jam_mulai = ? and jam_selesai = ?";
        String validate2 = "select * from tb_jadwal where hari = ? and ruangan = ? and jam_mulai = ? and jam_selesai = ?";
        try {
            if (kode.getValue() == null || mapel.getValue() == null || kelas.getValue() == null || ruangan.getValue() == null || hari.getValue() == null || mulai.getValue() == null || selesai.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                var validation = connection.prepareStatement(validate);
                validation.setString(1, hari.getValue());
                validation.setString(2, kode.getValue());
                validation.setString(3, mulai.getValue());
                validation.setString(4, selesai.getValue());
                resultSet = validation.executeQuery();
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Guru yang anda masukkan telah terjadwal !!");
                    alert.showAndWait();
                } else {
                    resultSet = null;
                    var validation2 = connection.prepareStatement(validate2);
                    validation2.setString(1, hari.getValue());
                    validation2.setString(2, ruangan.getValue());
                    validation2.setString(3, mulai.getValue());
                    validation2.setString(4, selesai.getValue());
                    resultSet = validation2.executeQuery();
                    if (resultSet.next()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Ruangan yang anda masukkan telah terjadwal !!");
                        alert.showAndWait();
                    } else {
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, kode.getValue());
                        preparedStatement.setString(2, guru.getText());
                        preparedStatement.setString(3, mapel.getValue());
                        preparedStatement.setString(4, kelas.getValue());
                        preparedStatement.setString(5, ruangan.getValue());
                        preparedStatement.setString(6, hari.getValue());
                        preparedStatement.setString(7, mulai.getValue());
                        preparedStatement.setString(8, selesai.getValue());
                        preparedStatement.execute();
                        preparedStatement.close();
                        connection.close();
                        init.changeScene("data/schedule_management.fxml");
                    }
                }
                resultSet.close();
                validation.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setHariItems();
        this.setKelasItems();
        this.setKodeItems();
        this.setMapelItems();
        this.setRuanganItems();
        this.setMulaiItems();
    }
}
