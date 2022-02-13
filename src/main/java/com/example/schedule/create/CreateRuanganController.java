package com.example.schedule.create;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class CreateRuanganController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public TextField ruangan = new TextField() {
        @Override
        public void replaceText(int start, int end, String text) {
            super.replaceText(start, end, text.toUpperCase());
        }
    };

    @FXML
    public TextField nomor = new TextField();

    public void numericText() {
        nomor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    nomor.setText(t1.replaceAll("[^\\d]",""));
                }
            }
        });
    }

    public void setUpper() {
        ruangan.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    public void submitData(ActionEvent event) throws IOException {
        Main init = new Main();
        connection = SqliteConnection.Connector();
        String query = "insert into tb_ruangan (nama_ruangan) values (?)";
        String validate = "select nama_ruangan from tb_ruangan where nama_ruangan = ? ";
        try {
            if (ruangan.getText().isEmpty() || nomor.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                alert.showAndWait();
            } else {
                var validation = connection.prepareStatement(validate);
                validation.setString(1, ruangan.getText().toUpperCase() + " " + nomor.getText());
                resultSet = validation.executeQuery();
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Nama ruangan yang anda masukkan telah terdaftar !!");
                    alert.showAndWait();
                } else {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, ruangan.getText().toUpperCase() + " " + nomor.getText());
                    preparedStatement.execute();
                    connection.close();
                    preparedStatement.close();
                    init.changeScene("data/data_ruangan.fxml");
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
        init.changeScene("data/data_ruangan.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setUpper();
        this.numericText();
    }
}
