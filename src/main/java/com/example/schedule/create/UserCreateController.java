package com.example.schedule.create;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
//import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserCreateController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    @FXML
    public TextField nama_user = new TextField();

    @FXML
    public TextField nis = new TextField();

    @FXML
    public ComboBox<String> nama_kelas = new ComboBox<>();

    @FXML
    public PasswordField password = new PasswordField();

    @FXML
    public ComboBox<String> role = new ComboBox<>();

    ObservableList<String> items = FXCollections.observableArrayList("Siswa", "Admin");
    ObservableList<String> kelasItems = FXCollections.observableArrayList();
    public void addItems() {
        role.setItems(items);
    }

    public void kelasItems() {
        connection = SqliteConnection.Connector();
        String query = "select * from tb_kelas";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                kelasItems.add(resultSet.getString("nama_kelas"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        nama_kelas.setItems(kelasItems);
    }

    public void submitData(ActionEvent event) throws IOException {
        Main main = new Main();
        connection = SqliteConnection.Connector();
        String query = "insert into tb_user (nama_user, nis, kelas, password, role) values (?, ?, ?, ?, ?)";
        String validate = "select nis from tb_user where nis = ? ";
        try {
            if (nama_user.getText().isEmpty() || nis.getText().isEmpty() || nama_kelas.getValue() == null || password.getText().isEmpty() || role.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                var validation = connection.prepareStatement(validate);
                validation.setString(1, nis.getText());
                resultSet = validation.executeQuery();
                    if (resultSet.next()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Nis yang anda masukkan telah terdaftar !!");
                        alert.showAndWait();
                    } else {
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, nama_user.getText());
                        preparedStatement.setString(2, nis.getText());
                        preparedStatement.setString(3, nama_kelas.getValue());
                        preparedStatement.setString(4, password.getText());
                        preparedStatement.setString(5, role.getValue());
                        preparedStatement.execute();
                        preparedStatement.close();
                        connection.close();
                        main.changeScene("data/user_data.fxml");
                    }
                    resultSet.close();
                    validation.close();
                }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void backScene(ActionEvent event) throws IOException {
        Main main = new Main();
        nama_user.setText(null);
        nis.setText(null);
        nama_kelas.setValue(null);
        password.setText(null);
        role.setValue(null);
        main.changeScene("data/user_data.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addItems();
        this.kelasItems();
    }
}
