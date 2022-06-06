package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Drug implements Initializable {

    @FXML
    private TextField searchText;

    @FXML
    private TableView drugTableView;

    @FXML
    private TableColumn<DrugAdd, String> itemcodeTable;

    @FXML
    private TextField itemcodeText;

    @FXML
    private TableColumn<DrugAdd, String> nameTable;

    @FXML
    private TextField nameText;

    @FXML
    private TableColumn<DrugAdd,Integer> quantityTable;

    @FXML
    private TextField quantityText;

    @FXML
    private TextField descriptionText;

    @FXML
    private TableColumn<DrugAdd,String> descriptionTable;

    @FXML
    private TableColumn<DrugAdd,String> barcodeTable;

    @FXML
    private TableColumn<DrugAdd,String> typeTable;

    @FXML
    private TextField typeText;

    @FXML
    private Label successLabelC;



    ObservableList<DrugAdd> drugAddObservableList = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resource) {

        SearchTable();
        quantityText.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
    }


    private void clearCompanyFields() {
        itemcodeText.clear();
        nameText.clear();
        typeText.clear();
        quantityText.clear();
        descriptionText.clear();
    }

    public void SearchTable() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
//        SQL Query to view
        String InventoryViewQuery1 = "SELECT * FROM drug";

        try {
            Statement statement1 = connectDB.createStatement();
            ResultSet QueryOutPut1 = statement1.executeQuery(InventoryViewQuery1);

            while (QueryOutPut1.next()) {

                String queryItemcode = QueryOutPut1.getString("d_code");
                String queryName = QueryOutPut1.getString("d_name");
                String queryType = QueryOutPut1.getString("d_type");
                String queryBarcode = QueryOutPut1.getString("barcode");
                Integer queryQuantity = QueryOutPut1.getInt("qty");
                String queryDescription = QueryOutPut1.getString("description");


                drugAddObservableList.add(new DrugAdd(queryItemcode,queryName,queryType,queryBarcode,queryQuantity,queryDescription));

            }

            itemcodeTable.setCellValueFactory(new PropertyValueFactory<>("item_code"));
            nameTable.setCellValueFactory(new PropertyValueFactory<>("name"));
            typeTable.setCellValueFactory(new PropertyValueFactory<>("type"));
            barcodeTable.setCellValueFactory(new PropertyValueFactory<>("barcode"));
            quantityTable.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            descriptionTable.setCellValueFactory(new PropertyValueFactory<>("description"));



            drugTableView.setItems(drugAddObservableList);

//          Initial filtered list
            FilteredList<DrugAdd> filteredData = new FilteredList<>(drugAddObservableList, b -> true);
            searchText.textProperty().addListener((observable, newValue, oldValue) -> {
                filteredData.setPredicate(drugAdd -> {
//                    if no search value
                    if (newValue.isBlank() || newValue.isEmpty() || newValue == null) {
                        return true;
                    }

                    String searchKeyWord = newValue.toLowerCase();

                    if (drugAdd.getItem_code().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in itemcode
                    } else if (drugAdd.getName().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in name
                    } else if (drugAdd.getType().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in quantity
                    } else if (drugAdd.getQuantity().toString().indexOf(searchKeyWord) > -1) {
                        return true;
                    }  else {
                        return false; //no match found
                    }
                });
            });

            SortedList<DrugAdd> sortedData = new SortedList<>(filteredData);
//          bind sorted result with table view
            sortedData.comparatorProperty().bind(drugTableView.comparatorProperty());
//          Apply sorted and filtered data with table
            drugTableView.setItems(sortedData);


        } catch (SQLException e) {
            Logger.getLogger(Drug.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }
    @FXML
    void AddButtonONAction(ActionEvent event) throws SQLException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        String name = nameText.getText();
        String itemcode = itemcodeText.getText();
        String type = typeText.getText();
        Integer quantity = Integer.parseInt(quantityText.getText());
        String description = descriptionText.getText();




        PreparedStatement ps1 = null;
        String query = "INSERT INTO `pmsdb`.`drug` (`d_code`, `d_name`, `d_type`, `barcode`, `qty`, `description`) VALUES (?, ?, ?, ?,?,?);";

        ps1 = connectDB.prepareStatement(query);
        ps1.setString(1, itemcode);
        ps1.setString(2, name);
        ps1.setString(3, type);
        ps1.setString(4,itemcode);
        ps1.setInt(5, quantity);
        ps1.setString(6,description );


        try {
            ps1.executeUpdate();
            clearCompanyFields();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("SUCCESSFULLY ADDED");
            alert.show();


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("NOT DELETED.TRY AGAIN");
            alert.show();

        }

    }

    @FXML
    void EditButtonONAction(ActionEvent event) throws SQLException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        String name = nameText.getText();
        String itemcode = itemcodeText.getText();
        String type = typeText.getText();
        Integer quantity = Integer.parseInt(quantityText.getText());
        String description = descriptionText.getText();


        PreparedStatement ps = null;
        String query = "UPDATE `pmsdb`.`drug` SET `d_code` = ?, `d_name` = ?, `d_type` = ?, `barcode` = ?, `qty` = ?, `description` = ? WHERE (`d_code` = ?);";
        ps = connectDB.prepareStatement(query);
        ps.setString(1, itemcode);
        ps.setString(2, name);
        ps.setString(3, type);
        ps.setString(4, itemcode);
        ps.setInt(5, quantity);
        ps.setString(7, itemcode);
        ps.setString(6,description );


        try {
            ps.executeUpdate();
            clearCompanyFields();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("SUCCESSFULLY EDITED");
            alert.show();


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("NOT EDITED.TRY AGAIN");
            alert.show();

        }

    }
    @FXML
    void DeleteButtonOnAction(ActionEvent event) throws SQLException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        String itemcode = itemcodeText.getText();

        PreparedStatement ps = null;
        String query = "DELETE FROM `pmsdb`.`drug` WHERE (`d_code` = ?);";
        ps = connectDB.prepareStatement(query);
        ps.setString(1, itemcode);

        try {

            ps.executeUpdate();
            clearCompanyFields();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("SUCCESSFULLY DELETED");
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("NOT DELETED.TRY AGAIN");
            alert.show();
        }
    }



}
