package com.example.schedule.controller;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleJurusan;
import com.example.schedule.models.Jurusan;
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
import java.util.ResourceBundle;

public class JurusanController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Jurusan jurusan = null;

    @FXML
    public TableView<Jurusan> jurusanTable;

    @FXML
    public TableColumn<Jurusan, String> id;

    @FXML
    public TableColumn<Jurusan, String> jurusanColumn;

    @FXML
    public TableColumn<Jurusan, String> actionColumn;

    @FXML
    public TextField filter_data;

    ObservableList<Jurusan> dataList = FXCollections.observableArrayList();

    public void refresh() {
        Table();
        FilteredList<Jurusan> filteredList = new FilteredList<>(dataList, e -> true);

        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productJurusan -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productJurusan.getNama_jurusan().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Jurusan> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(jurusanTable.comparatorProperty());
    }

    public void Table() {
        connection = SqliteConnection.Connector();
        refreshTable();

        id.setCellValueFactory(new PropertyValueFactory<>("id_jurusan"));
        jurusanColumn.setCellValueFactory(new PropertyValueFactory<>("nama_jurusan"));

        FilteredList<Jurusan> filteredList = new FilteredList<>(dataList, e -> true);

        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productJurusan -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productJurusan.getNama_jurusan().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Jurusan> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(jurusanTable.comparatorProperty());

        Callback<TableColumn<Jurusan, String>, TableCell<Jurusan, String>> cellFoctory = (TableColumn<Jurusan, String> param) -> {
            // make cell containing buttons
            final TableCell<Jurusan, String> cell = new TableCell<Jurusan, String>() {
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
                                jurusan = jurusanTable.getSelectionModel().getSelectedItem();
                                var query = "DELETE FROM tb_jurusan WHERE id_jurusan  = ?";
                                var connection = SqliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, jurusan.getId_jurusan());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                refresh();
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });
                        updateIcon.setOnMouseClicked((MouseEvent event) -> {
                            jurusan = jurusanTable.getSelectionModel().getSelectedItem();
                            try {
                                Main main = new Main();
                                HandleJurusan handle = new HandleJurusan();
                                Jurusan data = new Jurusan(jurusan.getId_jurusan(), jurusan.getNama_jurusan());
                                handle.setUpdateJurusan(data);
                                main.changeScene("update/update_jurusan.fxml");
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
        jurusanTable.setItems(dataList);
        jurusanTable.setItems(sortedList);
    }

    public void refreshTable() {
        try {
            dataList.clear();

            var query = "SELECT * FROM tb_jurusan";
            connection = SqliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                dataList.add(new Jurusan(
                        resultSet.getString("id_jurusan"),
                        resultSet.getString("nama_jurusan")
                ));
                jurusanTable.setItems(dataList);

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addData(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("create/add_jurusan.fxml");
    }

    public void jadwalScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/schedule_management.fxml");
    }

    public void kelasScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_kelas.fxml");
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

    public void ruanganScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_ruangan.fxml");
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
