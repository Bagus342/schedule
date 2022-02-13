package com.example.schedule.controller;

import com.example.schedule.Main;
import com.example.schedule.models.LoginModels;
import com.example.schedule.session.User;
import com.example.schedule.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    public TextField nis;

    @FXML
    public PasswordField password;

    @FXML
    private Label status;

    @FXML
    private HBox asd = new HBox();

    LoginModels loginModels = new LoginModels();

    public void UserLogin(ActionEvent event) throws IOException {
        Main login = new Main();
        try {
            if (loginModels.isLogin(nis.getText(), password.getText())) {
                if (loginModels.role(nis.getText(), "Siswa")) {
                    var session = loginModels.session(nis.getText());
                    User data = new User(session.nama_user, session.kelas);
                    UserSession.setUserSession(data);
                    login.changeScene("data/jadwal_pelajaran.fxml");
                } else {
                    login.changeScene("data/schedule_management.fxml");
                }
            } else {
                status.setText("Nis atau Password salah !");
            }
        } catch (SQLException e) {
            status.setText("Error");
            System.out.println(e.getCause());
        }
    }

}