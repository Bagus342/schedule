package com.example.schedule.create;

import com.example.schedule.Main;
import javafx.event.ActionEvent;

import java.io.IOException;

public class CreateController {

    public void backScene(ActionEvent event) throws IOException {
        Main init = new Main();
        init.changeScene("data/schedule_management.fxml");
    }

    public void submitData(ActionEvent event) throws IOException {
        Main init = new Main();
        init.changeScene("data/schedule_management.fxml");
    }
}
