package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Company implements Initializable {

    @FXML
    private TableView<CompanyAdd> companyTableView;

    @FXML
    private TextField successLabelC;

    @FXML
    private TextField addressText;

    @FXML
    private TableColumn<CompanyAdd,String> address_Table;

    @FXML
    private TextField companyNoText;

    @FXML
    private TableColumn<CompanyAdd, Integer> company_Table;

    @FXML
    private TextField nameText;

    @FXML
    private TableColumn<CompanyAdd, String> name_Table;

    @FXML
    private TextField phoneNoText;

    @FXML
    private TextField comSearchText;

    @FXML
    private TableColumn<CompanyAdd, Integer> phoneNo_Table;


    ObservableList<CompanyAdd> companyAddObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        SearchTable();
    }


    private void clearCompanyFields() {
        companyNoText.clear();
        nameText.clear();
        phoneNoText.clear();
        addressText.clear();
    }

    public void SearchTable() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
//        SQL Query to view
        String InventoryViewQuery = "SELECT * FROM company";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet QueryOutPut = statement.executeQuery(InventoryViewQuery);

            while (QueryOutPut.next()) {

                Integer queryCompanyNo = QueryOutPut.getInt("Com_No");
                String queryName = QueryOutPut.getString("name");
                String queryAddress = QueryOutPut.getString("address");
                Integer queryPhone = QueryOutPut.getInt("phone");


                companyAddObservableList.add(new CompanyAdd(queryCompanyNo, queryName,queryAddress, queryPhone));

            }

            company_Table.setCellValueFactory(new PropertyValueFactory<>("Com_No"));
            name_Table.setCellValueFactory(new PropertyValueFactory<>("name"));
            address_Table.setCellValueFactory(new PropertyValueFactory<>("address"));
            phoneNo_Table.setCellValueFactory(new PropertyValueFactory<>("phone"));



            companyTableView.setItems(companyAddObservableList);

//          Initial filtered list
            FilteredList<CompanyAdd> filteredData = new FilteredList<>(companyAddObservableList, b -> true);
            comSearchText.textProperty().addListener((observable, newValue, oldValue) -> {
                filteredData.setPredicate(CompanyAdd -> {
//                    if no search value
                    if (newValue.isBlank() || newValue.isEmpty() || newValue == null) {
                        return true;
                    }

                    String searchKeyWord = newValue.toLowerCase();

                    if (CompanyAdd.getCom_no().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in itemcode
                    } else if (CompanyAdd.getName().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in name
                    } else if (CompanyAdd.getAddress().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in price
                    } else if (CompanyAdd.getPhone().toString().indexOf(searchKeyWord) > -1) {
                        return true;
                    }  else {
                        return false; //no match found
                    }
                });
            });

            SortedList<CompanyAdd> sortedData = new SortedList<>(filteredData);
//          bind sorted result with table view
            sortedData.comparatorProperty().bind(companyTableView.comparatorProperty());
//          Apply sorted and filtered data with table
            companyTableView.setItems(sortedData);


        } catch (SQLException e) {
            Logger.getLogger(inward.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }
    @FXML
    void AddButtonONAction(ActionEvent event) throws SQLException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        String name = nameText.getText();
        String address = addressText.getText();
        Integer phone = Integer.parseInt(phoneNoText.getText());
        Integer companyNo = Integer.parseInt(companyNoText.getText());




        PreparedStatement ps = null;
        String query = "INSERT INTO `new_project`.`company` (`Com_No`, `name`, `address`, `phone`) VALUES (?,?,?,?);";
        ps = connectDB.prepareStatement(query);
        ps.setInt(1, companyNo);
        ps.setString(2, name);
        ps.setString(3, address);
        ps.setInt(4, phone);


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
        String address = addressText.getText();
        Integer phone = Integer.parseInt(phoneNoText.getText());
        Integer companyNo = Integer.parseInt(companyNoText.getText());




        PreparedStatement ps = null;
        String query = "UPDATE `new_project`.`company` SET `Com_No` = ?, `name` = ?, `address` = ?, `phone` = ? WHERE (`Com_No` = ?);";
        ps = connectDB.prepareStatement(query);
        ps.setInt(1, companyNo);
        ps.setString(2, name);
        ps.setString(3, address);
        ps.setInt(4, phone);
        ps.setInt(5, companyNo);


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

        Integer companyNo = Integer.parseInt(companyNoText.getText());


        PreparedStatement ps = null;
        String query = "DELETE FROM `new_project`.`company` WHERE (`Com_No` = ?);";
        ps = connectDB.prepareStatement(query);
        ps.setInt(1, companyNo);

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
