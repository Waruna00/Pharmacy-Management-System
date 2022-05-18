package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Search implements Initializable {

    @FXML
    private TableView<searchList> tbl;

    @FXML
    private TableColumn<searchList, String> tbl_code;
    @FXML
    private TableColumn<searchList, String> tbl_name;
    @FXML
    private TableColumn<searchList, String> tbl_type;
    @FXML
    private TableColumn<searchList, String> tbl_des;

    @FXML
    private TextField search_txt;

    ObservableList<searchList> searchListObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        SearchTable();
    }

    public void SearchTable() {
        Connection connection;
        ResultSet resultSet;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb","root","Whoiam@123");
            PreparedStatement statement = connection.prepareStatement("SELECT d_code,d_name,d_type,description FROM drug");
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                String d_code = resultSet.getNString("d_code");
                String d_name=resultSet.getNString("d_name");
                String d_type=resultSet.getNString("d_type");
                String des = resultSet.getNString("description");

                searchListObservableList.add(new searchList(d_code, d_name, d_type, des));

                tbl_code.setCellValueFactory(new PropertyValueFactory<>("d_code"));
                tbl_name.setCellValueFactory(new PropertyValueFactory<>("d_name"));
                tbl_type.setCellValueFactory(new PropertyValueFactory<>("d_type"));
                tbl_des.setCellValueFactory(new PropertyValueFactory<>("des"));

                tbl.setItems(searchListObservableList);

                FilteredList<searchList> filteredData = new FilteredList<>(searchListObservableList, b -> true);
                search_txt.textProperty().addListener((observable, newValue, oldValue) -> filteredData.setPredicate(searchList -> {
                    if (newValue.isBlank() || newValue.isEmpty()) {
                        return true;
                    }
                    String searchKeyWord = oldValue.toLowerCase();

                    if (searchList.getD_code().toLowerCase().contains(searchKeyWord)) {
                        return true; // that means we found a match in itemCode
                    } else if (searchList.getD_name().toLowerCase().contains(searchKeyWord)) {
                        return true; // that means we found a match in name
                    } else if (searchList.getD_type().toLowerCase().contains(searchKeyWord)) {
                        return true; // that means we found a match in type
                    } else if (searchList.getDes().toLowerCase().contains(searchKeyWord)) {
                        return true; // that means we found a match in description
                    }else {
                        return false;
                    }
                }));

                SortedList<searchList> sortedList = new SortedList<>(filteredData);
                sortedList.comparatorProperty().bind(tbl.comparatorProperty());
                tbl.setItems(sortedList);


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        tbl.setRowFactory( searchListTableView -> {
            TableRow<searchList> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2){
                    int index = searchListTableView.getSelectionModel().getFocusedIndex();
                    System.out.println(tbl.getItems().get(index).getD_code());
                    sales_controller s = new sales_controller();
                    //search_txt.setText(tbl.getItems().get(index).getD_code());
                    //s.SettingValues(tbl.getItems().get(index).getD_code(),tbl.getItems().get(index).getD_name(),tbl.getItems().get(index).getDes());

                    Stage stage = (Stage) tbl.getScene().getWindow();
                    //stage.close();
                }
            });
            return row;
        });
    }
}
