package com.example.schedule.update;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleUser;
import com.example.schedule.models.DataUser;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateUserController implements Initializable {

    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Connection connection = null;

    @FXML
    public TextField user = new TextField();

    @FXML
    private TextField nis_siswa = new TextField();

    @FXML
    public ComboBox<String> kelas = new ComboBox<>();

    @FXML
    private PasswordField password_siswa = new PasswordField();

    @FXML
    private ComboBox<String> role_siswa = new ComboBox<>();

    ObservableList<String> roleItems = FXCollections.observableArrayList("Siswa", "Admin");
    ObservableList<String> kelasItems = FXCollections.observableArrayList();

    public void addRole() {
        role_siswa.setItems(roleItems);
    }

    public void kelasItems() throws SQLException {
        connection = SqliteConnection.Connector();
        String query = "select * from tb_kelas";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                kelasItems.add(resultSet.getString("nama_kelas"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        kelas.setItems(kelasItems);
    }

    public void updateQuery(){
        connection = SqliteConnection.Connector();
        String query = "update tb_user set nama_user = ?, nis = ?, kelas = ?, password = ?, role = ? where id_user = ?";
        var id = HandleUser.getUpdateUser();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getText());
            preparedStatement.setString(2, nis_siswa.getText());
            preparedStatement.setString(3, kelas.getValue());
            preparedStatement.setString(4, password_siswa.getText());
            preparedStatement.setString(5, role_siswa.getValue());
            preparedStatement.setString(6, id.id);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void submitData(ActionEvent event) throws SQLException {
        Main main = new Main();
        var id = HandleUser.getUpdateUser();
        HandleUser validation = new HandleUser();
        try {
            if (user.getText().isEmpty() || nis_siswa.getText().isEmpty() || kelas.getValue() == null || password_siswa.getText().isEmpty() || role_siswa.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                if (validation.checkValidation(nis_siswa.getText(), id.id)) {
                    updateQuery();
                    main.changeScene("data/user_data.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Nis yang anda masukkan telah terdaftar !!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void backScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/user_data.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.addRole();
        try {
            this.kelasItems();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        var data = HandleUser.getUpdateUser();
        user.setText(data.nama_user);
        nis_siswa.setText(data.nis);
        kelas.setValue(data.kelas);
        password_siswa.setText(data.password);
        role_siswa.setValue(data.role);
    }
}
