package com.example.schedule;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class Location {

    public Parent getLocation(String fxml) throws IOException {
        return FXMLLoader.load(getClass().getResource(fxml));
    }
}
