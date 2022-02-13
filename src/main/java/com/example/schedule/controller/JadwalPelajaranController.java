package com.example.schedule.controller;

import com.example.schedule.Location;
import com.example.schedule.Main;
import com.example.schedule.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class JadwalPelajaranController implements Initializable {
    @FXML
    public VBox items = new VBox();

    @FXML
    private Button btn_kelas;

    @FXML
    private Button btn_keluar;

    @FXML
    private Label nama_user;

    @FXML
    private Label kelas;

    public void UserLogout(ActionEvent event) throws IOException {
        Main logout = new Main();
        logout.changeScene("login.fxml");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var session = UserSession.getUserSession();
        nama_user.setText(session.nama_user);
        kelas.setText(session.kelas);
    }
}