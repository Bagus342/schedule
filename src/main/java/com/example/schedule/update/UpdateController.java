package com.example.schedule.update;

import com.example.schedule.Main;
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
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateController{

    public void userLogout(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("login.fxml");
    }

    public void submitData(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("schedule_management.fxml");
    }

    public void backScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("schedule_management.fxml");
    }

    public void userScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("user_data.fxml");
    }

    public void scheduleScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("schedule_management.fxml");
    }

}
