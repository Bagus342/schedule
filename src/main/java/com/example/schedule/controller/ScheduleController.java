package com.example.schedule.controller;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.models.Jadwal;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Jadwal jadwal = null;

    @FXML
    private TextField filter_data;

    @FXML
    private TableView<Jadwal> jadwalTable;

    @FXML
    private TableColumn<Jadwal, String> id;

    @FXML
    private TableColumn<Jadwal, String> hariColumn;

    @FXML
    private TableColumn<Jadwal, String> mapelColumn;

    @FXML
    private TableColumn<Jadwal, String> guruColumn;

    @FXML
    private TableColumn<Jadwal, String> kelasColumn;

    @FXML
    private TableColumn<Jadwal, String> mulaiColumn;

    @FXML
    private TableColumn<Jadwal, String> selesaiColumn;

    @FXML
    private TableColumn<Jadwal, String> ruanganColumn;

    @FXML
    private TableColumn<Jadwal, String> actionColumn;

    ObservableList<Jadwal> dataList = FXCollections.observableArrayList();

    public void refresh() {
        Table();
        FilteredList<Jadwal> filteredList = new FilteredList<>(dataList, e -> true);
        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productJadwal -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productJadwal.getHari().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getMata_pelajaran().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getNama_guru().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getNama_kelas().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getMulai().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getSelesai().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getNama_ruangan().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Jadwal> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(jadwalTable.comparatorProperty());
    }

    public void Table() {
        var connection = SqliteConnection.Connector();
        refreshTable();

        id.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        hariColumn.setCellValueFactory(new PropertyValueFactory<>("hari"));
        mapelColumn.setCellValueFactory(new PropertyValueFactory<>("mata_pelajaran"));
        guruColumn.setCellValueFactory(new PropertyValueFactory<>("nama_guru"));
        kelasColumn.setCellValueFactory(new PropertyValueFactory<>("nama_kelas"));
        mulaiColumn.setCellValueFactory(new PropertyValueFactory<>("mulai"));
        selesaiColumn.setCellValueFactory(new PropertyValueFactory<>("selesai"));
        ruanganColumn.setCellValueFactory(new PropertyValueFactory<>("nama_ruangan"));

        FilteredList<Jadwal> filteredList = new FilteredList<>(dataList, e -> true);

        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productJadwal -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productJadwal.getHari().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getMata_pelajaran().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getNama_guru().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getNama_kelas().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getMulai().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getSelesai().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productJadwal.getNama_ruangan().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Jadwal> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(jadwalTable.comparatorProperty());

        Callback<TableColumn<Jadwal, String>, TableCell<Jadwal, String>> cellFoctory = (TableColumn<Jadwal, String> param) -> {
            // make cell containing buttons
            final TableCell<Jadwal, String> cell = new TableCell<Jadwal, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        HBox deleteIcon = new HBox(GlyphsDude.createIcon(FontAwesomeIcon.TRASH, "30px"));
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                jadwal = jadwalTable.getSelectionModel().getSelectedItem();
                                var query = "DELETE FROM tb_jadwal WHERE id_jadwal  = ?";
                                var connection = SqliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, jadwal.getId_jadwal());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                refresh();
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });

                        HBox hBox = new HBox(deleteIcon);
                        hBox.setStyle("-fx-alignment: center");
                        HBox.setMargin(deleteIcon,new Insets(2, 3, 0 , 5));

                        setGraphic(hBox);

                        setText(null);
                    }
                }
            };
            return cell;
        };
        actionColumn.setCellFactory(cellFoctory);
        jadwalTable.setItems(dataList);
        jadwalTable.setItems(sortedList);
    }

    public void refreshTable() {
        try {
            dataList.clear();

            var query = "SELECT * FROM tb_jadwal";
            var connection = SqliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                dataList.add(new Jadwal(
                        resultSet.getString("id_jadwal"),
                        resultSet.getString("hari"),
                        resultSet.getString("mapel"),
                        resultSet.getString("guru"),
                        resultSet.getString("kelas"),
                        resultSet.getString("jam_mulai"),
                        resultSet.getString("jam_selesai"),
                        resultSet.getString("ruangan")
                ));
                jadwalTable.setItems(dataList);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.Table();
    }


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

    public void jurusanScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_jurusan.fxml");
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

}
