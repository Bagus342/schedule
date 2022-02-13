module com.example.schedule {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires org.xerial.sqlitejdbc;
    requires fontawesomefx;

    opens com.example.schedule to javafx.fxml;
    exports com.example.schedule;
    exports com.example.schedule.connection;
    opens com.example.schedule.connection to javafx.fxml;
    exports com.example.schedule.controller;
    opens com.example.schedule.models to javafx.fxml;
    exports com.example.schedule.models;
    opens com.example.schedule.controller to javafx.fxml;
    exports com.example.schedule.create;
    opens com.example.schedule.create to javafx.fxml;
    exports com.example.schedule.update;
    opens com.example.schedule.update to javafx.fxml;
}