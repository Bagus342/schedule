package com.example.schedule.update;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleKelas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

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

    @FXML
    public TextField kelas;

    public void updateQuery(){
        String query = "update tb_kelas set nama_kelas = ? where id_kelas = ?";
        var id = HandleKelas.getUpdateKelas();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kelas.getText());
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
            if (kelas.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                if (validation.checkValidation(kelas.getText(), id.id)) {
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
        var data = HandleKelas.getUpdateKelas();
        kelas.setText(data.nama_kelas);
    }
}
