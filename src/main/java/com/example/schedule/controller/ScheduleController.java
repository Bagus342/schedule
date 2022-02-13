package com.example.schedule.controller;

import com.example.schedule.Location;
import com.example.schedule.Main;
//import com.jfoenix.controls.JFXDialog;
//import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    @FXML
    private Button btn_tambah;

    @FXML
    public void addData(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("create/tambah_data.fxml");
    }

    public void guruScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_guru.fxml");
    }

    public void kelasScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_kelas.fxml");
    }

    public void userScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/user_data.fxml");
    }

    public void ruanganScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_ruangan.fxml");
    }

    public void pelajaranScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_pelajaran.fxml");
    }

    public void userLogout(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("login.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
