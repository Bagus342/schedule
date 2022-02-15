package com.example.schedule.controller;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Jadwal;
import com.example.schedule.session.UserSession;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class JadwalPelajaranController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Jadwal jadwal = null;

    @FXML
    private ComboBox<String> hari;

    @FXML
    private ComboBox<String> filter_kelas;

    @FXML
    private Label nama_user;

    @FXML
    private Label kelas;

    @FXML
    private TableView<Jadwal> jadwalTable;

    @FXML
    private TableColumn<Jadwal, String> id;

    @FXML
    private TableColumn<Jadwal, String> hariColumn;

    @FXML
    private TableColumn<Jadwal, String> mapelColumn;

    @FXML
    private TableColumn<Jadwal, String> guruColumn;

    @FXML
    private TableColumn<Jadwal, String> kelasColumn;

    @FXML
    private TableColumn<Jadwal, String> mulaiColumn;

    @FXML
    private TableColumn<Jadwal, String> selesaiColumn;

    @FXML
    private TableColumn<Jadwal, String> ruanganColumn;


    ObservableList<Jadwal> dataList = FXCollections.observableArrayList();
    ObservableList<String> hariItems = FXCollections.observableArrayList("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu");
    ObservableList<String> kelasItems = FXCollections.observableArrayList();

    public void setHariItems() {
        hari.setItems(hariItems);
    }

    public void setKelasItems() {
        connection = SqliteConnection.Connector();
        String query = "select * from tb_kelas";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                kelasItems.add(resultSet.getString("nama_kelas"));
            }
            filter_kelas.setItems(kelasItems);
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setKelas() {
        try {
            hari.setValue(null);
            hari.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Hari");
                    } else {
                        setText(item);
                    }
                }
            });
            Table(filter_kelas.getValue());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void Table(String filter) {
        var connection = SqliteConnection.Connector();
        refreshTable(filter);

        id.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        hariColumn.setCellValueFactory(new PropertyValueFactory<>("hari"));
        mapelColumn.setCellValueFactory(new PropertyValueFactory<>("mata_pelajaran"));
        guruColumn.setCellValueFactory(new PropertyValueFactory<>("nama_guru"));
        kelasColumn.setCellValueFactory(new PropertyValueFactory<>("nama_kelas"));
        mulaiColumn.setCellValueFactory(new PropertyValueFactory<>("mulai"));
        selesaiColumn.setCellValueFactory(new PropertyValueFactory<>("selesai"));
        ruanganColumn.setCellValueFactory(new PropertyValueFactory<>("nama_ruangan"));

        FilteredList<Jadwal> filteredList = new FilteredList<>(dataList, e -> true);

        hari.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productJadwal -> {
                if (newValue == null) {
                    return true;
                }

                if (productJadwal.getHari().indexOf(newValue.toString()) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Jadwal> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(jadwalTable.comparatorProperty());

        jadwalTable.setItems(dataList);
        jadwalTable.setItems(sortedList);
    }

    public void refreshTable(String kelas) {
        try {
            dataList.clear();
            var query = "SELECT * FROM tb_jadwal where kelas = ?";
            var connection = SqliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kelas);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                dataList.add(new Jadwal(
                        resultSet.getString("id_jadwal"),
                        resultSet.getString("hari"),
                        resultSet.getString("mapel"),
                        resultSet.getString("guru"),
                        resultSet.getString("kelas"),
                        resultSet.getString("jam_mulai"),
                        resultSet.getString("jam_selesai"),
                        resultSet.getString("ruangan")
                ));
                jadwalTable.setItems(dataList);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void UserLogout(ActionEvent event) throws IOException {
        Main logout = new Main();
        logout.changeScene("login.fxml");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setHariItems();
        this.setKelasItems();
        var session = UserSession.getUserSession();
        nama_user.setText(session.nama_user);
        kelas.setText(session.kelas);
        this.Table(session.kelas);
    }
}