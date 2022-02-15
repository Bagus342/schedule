package com.example.schedule.update;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleGuru;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateGuruController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public TextField guru;

    @FXML
    public TextField kode = new TextField();

    public void numericText() {
        kode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    kode.setText(t1.replaceAll("[^\\d]",""));
                }
            }
        });
    }

    public void updateQuery(){
        connection = SqliteConnection.Connector();
        String query = "update tb_guru set nama_guru = ?, kode_guru = ? where id_guru = ?";
        var id = HandleGuru.getUpdateGuru();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, guru.getText());
            preparedStatement.setString(2, kode.getText());
            preparedStatement.setString(3, id.id_guru);
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
        var id = HandleGuru.getUpdateGuru();
        HandleGuru validation = new HandleGuru();
        try {
            if (guru.getText().isEmpty() || kode.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                Optional<ButtonType> result = alert.showAndWait();
                result.ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        var data = HandleGuru.getUpdateGuru();
                        guru.setText(data.nama_guru);
                        kode.setText(data.kode_guru);
                    }
                });
            } else {
                if (validation.checkValidation(kode.getText(), id.id_guru)) {
                    updateQuery();
                    main.changeScene("data/data_guru.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Nama guru yang anda masukkan telah terdaftar !!");
                    Optional<ButtonType> result = alert.showAndWait();
                    result.ifPresent(buttonType -> {
                        if (buttonType == ButtonType.OK) {
                            var data = HandleGuru.getUpdateGuru();
                            guru.setText(data.nama_guru);
                            kode.setText(data.kode_guru);
                        }
                    });
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void backScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_guru.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.numericText();
        var data = HandleGuru.getUpdateGuru();
        guru.setText(data.nama_guru);
        kode.setText(data.kode_guru);
    }
}
