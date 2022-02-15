package com.example.schedule.update;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleJurusan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateJurusanController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;

    @FXML
    public TextField jurusan = new TextField() {
        @Override
        public void replaceText(int start, int end, String text) {
            super.replaceText(start, end, text.toUpperCase());
        }
    };

    public void setUpper() {
        jurusan.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    public void updateQuery(){
        connection = SqliteConnection.Connector();
        String query = "update tb_jurusan set nama_jurusan = ? where id_jurusan = ?";
        var id = HandleJurusan.getUpdateJurusan();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, jurusan.getText().toUpperCase());
            preparedStatement.setString(2, id.id_jurusan);
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
        var id = HandleJurusan.getUpdateJurusan();
        HandleJurusan validation = new HandleJurusan();
        try {
            if (jurusan.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                Optional<ButtonType> result = alert.showAndWait();
                result.ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        var data = HandleJurusan.getUpdateJurusan();
                        String str = data.nama_jurusan;
                        str = str.replaceAll("[\\d]","");
                        jurusan.setText(str);
                    }
                });
            } else {
                if (validation.checkValidation(jurusan.getText(), id.id_jurusan)) {
                    updateQuery();
                    main.changeScene("data/data_jurusan.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Nama jurusan telah terdaftar !!");
                    Optional<ButtonType> result = alert.showAndWait();
                    result.ifPresent(buttonType -> {
                        if (buttonType == ButtonType.OK) {
                            var data = HandleJurusan.getUpdateJurusan();
                            String str = data.nama_jurusan;
                            str = str.replaceAll("[\\d]","");
                            jurusan.setText(str);
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
        main.changeScene("data/data_jurusan.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setUpper();
        var data = HandleJurusan.getUpdateJurusan();
        String str = data.nama_jurusan;
        str = str.replaceAll("[\\d]","");
        jurusan.setText(str);
    }
}
