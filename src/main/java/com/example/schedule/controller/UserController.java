package com.example.schedule.controller;

import com.example.schedule.Main;
import com.example.schedule.connection.SqliteConnection;
import com.example.schedule.handle.HandleUser;
import com.example.schedule.models.DataUser;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;

import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    DataUser dataUser = null;
    @FXML
    private VBox items = new VBox();
    @FXML
    private HBox header = new HBox();
    @FXML
    private HBox table = new HBox();
    @FXML
    private Button btn_search;
    @FXML
    private Label text_nis;
    @FXML
    private TextField filter_data;
    @FXML
    private Button btn_tambah;
    @FXML
    private Pane addScene = new Pane();
    @FXML
    private Pane parent = new Pane();

    @FXML
    private TableView<DataUser> userTable;

    @FXML
    private TableColumn<DataUser, String> id;

    @FXML
    private TableColumn<DataUser, String> namaColumn;

    @FXML
    private TableColumn<DataUser, String> nisColumn;

    @FXML
    private TableColumn<DataUser, String> kelasColumn;

    @FXML
    private TableColumn<DataUser, String> roleColumn;

    @FXML
    private TableColumn<DataUser, String> passwordColumn;

    @FXML
    private TableColumn<DataUser, String> actionColumn;

    ObservableList<DataUser> dataList = FXCollections.observableArrayList();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public void refresh() {
        Table();
        FilteredList<DataUser> filteredList = new FilteredList<>(dataList, e -> true);
        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productUser -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productUser.getNama_user().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productUser.getNis().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productUser.getKelas().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productUser.getPassword().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productUser.getRole().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<DataUser> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(userTable.comparatorProperty());
    }

    public void Table() {
        var connection = SqliteConnection.Connector();
        refreshTable();

        id.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama_user"));
        nisColumn.setCellValueFactory(new PropertyValueFactory<>("nis"));
        kelasColumn.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        FilteredList<DataUser> filteredList = new FilteredList<>(dataList, e -> true);

        filter_data.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(productUser -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (productUser.getNama_user().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productUser.getNis().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productUser.getKelas().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productUser.getPassword().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (productUser.getRole().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<DataUser> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(userTable.comparatorProperty());

        Callback<TableColumn<DataUser, String>, TableCell<DataUser, String>> cellFoctory = (TableColumn<DataUser, String> param) -> {
            // make cell containing buttons
            final TableCell<DataUser, String> cell = new TableCell<DataUser, String>() {
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
                                dataUser = userTable.getSelectionModel().getSelectedItem();
                                var query = "DELETE FROM tb_user WHERE id_user  = ?";
                                var connection = SqliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, dataUser.getid());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                refresh();
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });
                        updateIcon.setOnMouseClicked((MouseEvent event) -> {
                            dataUser = userTable.getSelectionModel().getSelectedItem();
                            try {
                                Main main = new Main();
                                HandleUser handle = new HandleUser();
                                DataUser data = new DataUser(dataUser.getid(),dataUser.getNama_user(), dataUser.getNis(), dataUser.getKelas(), dataUser.getPassword(), dataUser.getRole());
                                handle.setUpdateUser(data);
                                main.changeScene("update/update_user.fxml");
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
        userTable.setItems(dataList);
        userTable.setItems(sortedList);
    }

    public void refreshTable() {
        try {
            dataList.clear();

            var query = "SELECT * FROM tb_user";
            var connection = SqliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                dataList.add(new DataUser(
                        resultSet.getString("id_user"),
                        resultSet.getString("nama_user"),
                        resultSet.getString("nis"),
                        resultSet.getString("kelas"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                        ));
                userTable.setItems(dataList);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.Table();
    }

    public void addData(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("create/add_user.fxml");
    }

    public void guruScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_guru.fxml");
    }

    public void jadwalScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/schedule_management.fxml");
    }

    public void kelasScene(ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("data/data_kelas.fxml");
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
