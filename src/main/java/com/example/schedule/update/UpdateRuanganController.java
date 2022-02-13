package com.example.schedule.update;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleRuangan;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateRuanganController implements Initializable {

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

    public void updateQuery(){
        connection = SqliteConnection.Connector();
        String query = "update tb_ruangan set nama_ruangan = ? where id_ruangan = ?";
        var id = HandleRuangan.getUpdateRuangan();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ruangan.getText().toUpperCase() + " " + nomor.getText());
            preparedStatement.setString(2, id.id_ruangan);
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
        var id = HandleRuangan.getUpdateRuangan();
        HandleRuangan validation = new HandleRuangan();
        try {
            if (ruangan.getText().isEmpty() || nomor.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Kolom tidak boleh kosong !!");
                Optional<ButtonType> result = alert.showAndWait();
                result.ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        var data = HandleRuangan.getUpdateRuangan();
                        String str = data.nama_ruangan;
                        String number = data.nama_ruangan;
                        str = str.replaceAll("[\\d ]","");
                        number = number.replaceAll("[^\\d]","");
                        ruangan.setText(str);
                        nomor.setText(number);
                    }
                });
            } else {
                if (validation.checkValidation(ruangan.getText() + " " + nomor.getText(), id.id_ruangan)) {
                    updateQuery();
                    main.changeScene("data/data_ruangan.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Nama ruangan telah terdaftar !!");
                    Optional<ButtonType> result = alert.showAndWait();
                    result.ifPresent(buttonType -> {
                        if (buttonType == ButtonType.OK) {
                            var data = HandleRuangan.getUpdateRuangan();
                            String str = data.nama_ruangan;
                            String number = data.nama_ruangan;
                            str = str.replaceAll("[\\d ]","");
                            number = number.replaceAll("[^\\d]","");
                            ruangan.setText(str);
                            nomor.setText(number);
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
        this.setUpper();
        this.numericText();
        var data = HandleRuangan.getUpdateRuangan();
        String str = data.nama_ruangan;
        String number = data.nama_ruangan;
        str = str.replaceAll("[\\d ]","");
        number = number.replaceAll("[^\\d]","");
        ruangan.setText(str);
        nomor.setText(number);
    }
}
