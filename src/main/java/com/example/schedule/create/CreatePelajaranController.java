package com.example.schedule.create;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreatePelajaranController {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public TextField pelajaran = new TextField();

    public void backScene(ActionEvent event) throws IOException {
        Main init = new Main();
        init.changeScene("data/data_pelajaran.fxml");
    }

    public void submitData(ActionEvent event) throws IOException {
        Main init = new Main();
        connection = SqliteConnection.Connector();
        String query = "insert into tb_mapel (nama_mapel) values (?)";
        String validate = "select nama_mapel from tb_mapel where nama_mapel = ? ";
        try {
            if (pelajaran.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                var validation = connection.prepareStatement(validate);
                validation.setString(1, pelajaran.getText());
                resultSet = validation.executeQuery();
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Mata Pelajaran yang anda masukkan telah terdaftar !!");
                    alert.showAndWait();
                } else {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, pelajaran.getText());
                    preparedStatement.execute();
                    connection.close();
                    preparedStatement.close();
                    init.changeScene("data/data_pelajaran.fxml");
                }
                resultSet.close();
                validation.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
