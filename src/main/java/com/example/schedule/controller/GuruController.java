package com.example.schedule.controller;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleGuru;
import com.example.schedule.models.Guru;
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

public class GuruController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Guru guru = null;

    @FXML
    public TableView<Guru> guruTable;

    @FXML
    public TableColumn<Guru, String> id;

    @FXML
    public TableColumn<Guru, String> guruColumn;

    @FXML
    public TableColumn<Guru, String> kodeColumn;

    @FXML
    public TableColumn<Guru, String> actionColumn;

    @FXML
    public TextField filter_data;

    ObservableList<Guru> dataList = FXCollections.observableArrayList();

    public void refresh() {
        Table();
        FilteredList<Guru> filteredList = new FilteredList<>(dataList, e -> true);

        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productGuru -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productGuru.getNama_guru().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productGuru.getKode_guru().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Guru> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(guruTable.comparatorProperty());
    }

    public void Table() {
        connection = SqliteConnection.Connector();
        refreshTable();

        id.setCellValueFactory(new PropertyValueFactory<>("id_guru"));
        guruColumn.setCellValueFactory(new PropertyValueFactory<>("nama_guru"));
        kodeColumn.setCellValueFactory(new PropertyValueFactory<>("kode_guru"));

        FilteredList<Guru> filteredList = new FilteredList<>(dataList, e -> true);

        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productGuru -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productGuru.getNama_guru().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productGuru.getKode_guru().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Guru> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(guruTable.comparatorProperty());

        Callback<TableColumn<Guru, String>, TableCell<Guru, String>> cellFoctory = (TableColumn<Guru, String> param) -> {
            // make cell containing buttons
            final TableCell<Guru, String> cell = new TableCell<Guru, String>() {
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
                                guru = guruTable.getSelectionModel().getSelectedItem();
                                var query = "DELETE FROM tb_guru WHERE id_guru  = ?";
                                var connection = SqliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, guru.getId_guru());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                refresh();
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });
                        updateIcon.setOnMouseClicked((MouseEvent event) -> {
                            guru = guruTable.getSelectionModel().getSelectedItem();
                            try {
                                Main main = new Main();
                                HandleGuru handle = new HandleGuru();
                                Guru data = new Guru(guru.getId_guru(), guru.getNama_guru(), guru.getKode_guru());
                                handle.setUpdateGuru(data);
                                main.changeScene("update/update_guru.fxml");
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
        guruTable.setItems(dataList);
        guruTable.setItems(sortedList);
    }

    public void refreshTable() {
        try {
            dataList.clear();

            var query = "SELECT * FROM tb_guru";
            connection = SqliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                dataList.add(new Guru(
                        resultSet.getString("id_guru"),
                        resultSet.getString("nama_guru"),
                        resultSet.getString("kode_guru")
                ));
                guruTable.setItems(dataList);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addData(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("create/add_guru.fxml");
    }

    public void jadwalScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/schedule_management.fxml");
    }

    public void pelajaranScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_pelajaran.fxml");
    }

    public void jurusanScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_jurusan.fxml");
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

    public void userLogout(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("login.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.Table();
    }

}
