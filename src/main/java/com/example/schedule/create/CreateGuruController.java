package com.example.schedule.create;

import com.example.schedule.Location;
import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateGuruController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public TextField guru;

    @FXML
    public TextField kode = new TextField();

    @FXML
    public ComboBox<String> mapel = new ComboBox<>();

    ObservableList<String> mapelItem = FXCollections.observableArrayList();

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

    public void addMapel() {
        connection = SqliteConnection.Connector();
        String query = "select * from tb_mapel";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                mapelItem.add(resultSet.getString("nama_mapel"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        mapel.setItems(mapelItem);
    }

    public void submitData(ActionEvent event) throws IOException {
        Main init = new Main();
        connection = SqliteConnection.Connector();
        String query = "insert into tb_guru (nama_guru, kode_guru, mapel) values (?, ?, ?)";
        String validate = "select * from tb_guru where kode_guru = ? ";
        try {
            if (guru.getText().isEmpty() || mapel.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                var validation = connection.prepareStatement(validate);
                validation.setString(1, kode.getText());
                resultSet = validation.executeQuery();
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Kode guru yang anda masukkan telah terdaftar !!");
                    alert.showAndWait();
                } else {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, guru.getText());
                    preparedStatement.setString(2, kode.getText());
                    preparedStatement.setString(3, mapel.getValue());
                    preparedStatement.execute();
                    connection.close();
                    preparedStatement.close();
                    init.changeScene("data/data_guru.fxml");
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
        init.changeScene("data/data_guru.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.addMapel();
        this.numericText();
    }
}
