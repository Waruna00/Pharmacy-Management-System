package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Drug{

    @FXML
    private TextField SearchText;

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
    private TableColumn<DrugAdd,String> typeTable;

    @FXML
    private TextField typeText;

    @FXML

    ObservableList<DrugAdd> drugAddObservableList = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resource) {
        SearchTable();
    }


    private void clearCompanyFields() {
        itemcodeText.clear();
        nameText.clear();
        typeText.clear();
        quantityText.clear();
    }

    public void SearchTable() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
//        SQL Query to view
        String InventoryViewQuery = "SELECT * FROM drug";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet QueryOutPut = statement.executeQuery(InventoryViewQuery);

            while (QueryOutPut.next()) {

                String queryItemcode = QueryOutPut.getString("itemcode");
                String queryName = QueryOutPut.getString("name");
                String queryType = QueryOutPut.getString("type");
                Integer queryQuantity = QueryOutPut.getInt("quantity");


                drugAddObservableList.add(new DrugAdd(queryItemcode, queryName,queryType,queryQuantity));

            }

            itemcodeTable.setCellValueFactory(new PropertyValueFactory<>("Com_No"));
            nameTable.setCellValueFactory(new PropertyValueFactory<>("name"));
            typeTable.setCellValueFactory(new PropertyValueFactory<>("address"));
            quantityTable.setCellValueFactory(new PropertyValueFactory<>("phone"));



            drugTableView.setItems(drugAddObservableList);

//          Initial filtered list
            FilteredList<DrugAdd> filteredData = new FilteredList<>(drugAddObservableList, b -> true);
            SearchText.textProperty().addListener((observable, newValue, oldValue) -> {
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
                        return true; // that means we found a match in price
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




        PreparedStatement ps = null;
        String query = "INSERT INTO `new_project`.`drug` (`itemcode`, `name`, `type`, `quantity`) VALUES (?,?,?,?);";
        ps = connectDB.prepareStatement(query);
        ps.setString(1, itemcode);
        ps.setString(2, name);
        ps.setString(3, type);
        ps.setInt(4, quantity);


        try {
            ps.executeUpdate();
//            successLabelC.setText("Successfully added");
            clearCompanyFields();


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

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




        PreparedStatement ps = null;
        String query = "UPDATE `new_project`.`drug` SET `itemcode` = ?, `name` = ?, `type` = ?, `quantity` = ? WHERE (`itemcode` = ?);";
        ps = connectDB.prepareStatement(query);
        ps.setString(1, itemcode);
        ps.setString(2, name);
        ps.setString(3, type);
        ps.setInt(4, quantity);
        ps.setString(1, itemcode);


        try {
            ps.executeUpdate();
//            successLabelC.setText("Successfully edited");
            clearCompanyFields();


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }

    }
    @FXML
    void DeleteButtonOnAction(ActionEvent event) throws SQLException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        String itemcode = itemcodeText.getText();

        PreparedStatement ps = null;
        String query = "DELETE FROM `new_project`.`drug` WHERE (`itemcode` = ?);";
        ps = connectDB.prepareStatement(query);
        ps.setString(1, itemcode);

        try {

            ps.executeUpdate();
//            successLabelC.setText("Successfully Deleted");
            clearCompanyFields();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }



}
