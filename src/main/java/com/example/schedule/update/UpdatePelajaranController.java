package com.example.schedule.update;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandlePelajaran;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdatePelajaranController implements Initializable {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    @FXML
    public TextField pelajaran;

    public void updateQuery(){
        String query = "update tb_mapel set nama_mapel = ? where id_mapel = ?";
        var id = HandlePelajaran.getUpdatePelajaran();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pelajaran.getText());
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
        var id = HandlePelajaran.getUpdatePelajaran();
        HandlePelajaran validation = new HandlePelajaran();
        try {
            if (pelajaran.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                if (validation.checkValidation(pelajaran.getText(), id.id)) {
                    updateQuery();
                    main.changeScene("data/data_pelajaran.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Mata Pelajaran yang anda masukkan telah terdaftar !!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void backScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_pelajaran.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var data = HandlePelajaran.getUpdatePelajaran();
        pelajaran.setText(data.mapel);
    }
}
