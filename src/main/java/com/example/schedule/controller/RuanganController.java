package com.example.schedule.controller;

import com.example.schedule.Location;
import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleRuangan;
import com.example.schedule.models.Ruangan;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RuanganController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Ruangan ruangan = null;

    @FXML
    public TableView<Ruangan> ruanganTable;

    @FXML
    public TableColumn<Ruangan, String> id;

    @FXML
    public TableColumn<Ruangan, String> ruanganColumn;

    @FXML
    public TableColumn<Ruangan, String> actionColumn;

    @FXML
    public TextField filter_data;

    ObservableList<Ruangan> dataList = FXCollections.observableArrayList();

    public void refresh() {
        Table();
        FilteredList<Ruangan> filteredList = new FilteredList<>(dataList, e -> true);

        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productRuangan -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productRuangan.getNama_ruangan().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Ruangan> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(ruanganTable.comparatorProperty());
    }

    public void Table() {
        connection = SqliteConnection.Connector();
        refreshTable();

        id.setCellValueFactory(new PropertyValueFactory<>("id_ruangan"));
        ruanganColumn.setCellValueFactory(new PropertyValueFactory<>("nama_ruangan"));

        FilteredList<Ruangan> filteredList = new FilteredList<>(dataList, e -> true);

        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productRuangan -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productRuangan.getNama_ruangan().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Ruangan> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(ruanganTable.comparatorProperty());

        Callback<TableColumn<Ruangan, String>, TableCell<Ruangan, String>> cellFoctory = (TableColumn<Ruangan, String> param) -> {
            // make cell containing buttons
            final TableCell<Ruangan, String> cell = new TableCell<Ruangan, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        HBox updateIcon = new HBox(GlyphsDude.createIcon(FontAwesomeIcon.PENCIL_SQUARE, "30px"));
                        HBox deleteIcon = new HBox(GlyphsDude.createIcon(FontAwesomeIcon.TRASH, "30px"));
//
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                        );
                        updateIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-fx-text-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                ruangan = ruanganTable.getSelectionModel().getSelectedItem();
                                var query = "DELETE FROM tb_ruangan WHERE id_ruangan  = ?";
                                var connection = SqliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, ruangan.getId_ruangan());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                refresh();
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });
                        updateIcon.setOnMouseClicked((MouseEvent event) -> {
                            ruangan = ruanganTable.getSelectionModel().getSelectedItem();
                            try {
                                Main main = new Main();
                                HandleRuangan handle = new HandleRuangan();
                                Ruangan data = new Ruangan(ruangan.getId_ruangan(), ruangan.getNama_ruangan());
                                handle.setUpdateRuangan(data);
                                main.changeScene("update/update_ruangan.fxml");
                            } catch (IOException ex) {
                                System.out.println(ex.getCause());
                            }
                        });

                        HBox hBox = new HBox(updateIcon, deleteIcon);
                        hBox.setStyle("-fx-alignment: center");
                        HBox.setMargin(updateIcon,new Insets(2, 2, 0 , 3));
                        HBox.setMargin(deleteIcon,new Insets(2, 3, 0 , 5));

                        setGraphic(hBox);

                        setText(null);
                    }
                }
            };
            return cell;
        };
        actionColumn.setCellFactory(cellFoctory);
        ruanganTable.setItems(dataList);
        ruanganTable.setItems(sortedList);
    }

    public void refreshTable() {
        try {
            dataList.clear();

            var query = "SELECT * FROM tb_ruangan";
            connection = SqliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                dataList.add(new Ruangan(
                        resultSet.getString("id_ruangan"),
                        resultSet.getString("nama_ruangan")
                ));
                ruanganTable.setItems(dataList);

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addData(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("create/add_ruangan.fxml");
    }

    public void jadwalScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/schedule_management.fxml");
    }

    public void pelajaranScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_pelajaran.fxml");
    }

    public void guruScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_guru.fxml");
    }

    public void userScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/user_data.fxml");
    }

    public void kelasScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_kelas.fxml");
    }

    public void userLogout(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("login.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.Table();
    }
}
