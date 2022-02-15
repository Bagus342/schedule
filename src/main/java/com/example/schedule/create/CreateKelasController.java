package com.example.schedule.create;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Kelas;
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
import java.util.ResourceBundle;

public class CreateKelasController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public TextField abjad_kelas = new TextField();

    @FXML
    public ComboBox<String> no_kelas = new ComboBox<>();

    @FXML
    public ComboBox<String> jurusan = new ComboBox<>();

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

    public void setNo_kelas() {
        no_kelas.setItems(kelasItems);
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

    public void submitData(ActionEvent event) throws IOException {
        Main init = new Main();
        connection = SqliteConnection.Connector();
        String query = "insert into tb_kelas (nama_kelas) values (?)";
        String validate = "select nama_kelas from tb_kelas where nama_kelas = ? ";
        try {
            if (abjad_kelas.getText().isEmpty() || jurusan.getValue() == null || no_kelas.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                var validation = connection.prepareStatement(validate);
                validation.setString(1, no_kelas.getValue() + "-" + jurusan.getValue() + "-" + abjad_kelas.getText());
                resultSet = validation.executeQuery();
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Nama kelas yang anda masukkan telah terdaftar !!");
                    alert.showAndWait();
                } else {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, no_kelas.getValue() + "-" + jurusan.getValue() + "-" + abjad_kelas.getText());
                    preparedStatement.execute();
                    connection.close();
                    preparedStatement.close();
                    init.changeScene("data/data_kelas.fxml");
                }
                resultSet.close();
                validation.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void backScene(ActionEvent event) throws IOException {
        Main init = new Main();
        init.changeScene("data/data_kelas.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setAbjad_kelas();
        this.setJurusan();
        this.setNo_kelas();
        this.setUpper();
    }
}
