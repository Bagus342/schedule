package com.example.schedule.update;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleKelas;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateKelasController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public TextField abjad_kelas;

    @FXML
    public ComboBox<String> jurusan = new ComboBox<>();

    @FXML
    public ComboBox<String> no_kelas = new ComboBox<>();

    ObservableList<String> kelasItems = FXCollections.observableArrayList("X", "XI", "XII", "XIII");
    ObservableList<String> jurusanItems = FXCollections.observableArrayList();

    public void setAbjad_kelas() {
        abjad_kelas.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (abjad_kelas.getText().length() > 1) {
                    String s = abjad_kelas.getText().substring(0, 1);
                    abjad_kelas.setText(s);
                }
            }
        });
    }

    public void setUpper() {
        abjad_kelas.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    public void setJurusan() {
        connection = SqliteConnection.Connector();
        String query = "select * from tb_jurusan";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                jurusanItems.add(resultSet.getString("nama_jurusan"));
            }
            jurusan.setItems(jurusanItems);
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setNo_kelas() {
        no_kelas.setItems(kelasItems);
    }

    public void updateQuery(){
        String query = "update tb_kelas set nama_kelas = ? where id_kelas = ?";
        var id = HandleKelas.getUpdateKelas();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, no_kelas.getValue() + "-" + jurusan.getValue() + "-" + abjad_kelas.getText());
            preparedStatement.setString(2, id.id);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void submitData(ActionEvent event) {
        Main main = new Main();
        connection = SqliteConnection.Connector();
        var id = HandleKelas.getUpdateKelas();
        HandleKelas validation = new HandleKelas();
        try {
            if (abjad_kelas.getText().isEmpty() || jurusan.getValue() == null || no_kelas.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                if (validation.checkValidation(no_kelas.getValue() + "-" + jurusan.getValue() + "-" + abjad_kelas.getText(), id.id)) {
                    updateQuery();
                    main.changeScene("data/data_kelas.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Nama kelas yang anda masukkan telah terdaftar !!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void backScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_kelas.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setUpper();
        this.setAbjad_kelas();
        this.setNo_kelas();
        this.setJurusan();
        var data = HandleKelas.getUpdateKelas();
        var no = data.nama_kelas.split("-")[0];
        var jurusan_kelas = data.nama_kelas.split("-")[1];
        var abjad = data.nama_kelas.split("-")[2];
        no_kelas.setValue(no);
        jurusan.setValue(jurusan_kelas);
        abjad_kelas.setText(abjad);
    }
}
