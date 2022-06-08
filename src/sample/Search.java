package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

public class Search implements Initializable {

    @FXML
    private TableView<searchList> SearchTableView;

    @FXML
    private TableColumn<searchList, String> batch_Table;

    @FXML
    private TableColumn<searchList, String> companyNO_Table;

    @FXML
    private TableColumn<searchList, String> cost_Table;

    @FXML
    private TableColumn<searchList, String> expDate_Table;

    @FXML
    private TextField searchText;

    @FXML
    private TableColumn<searchList, String> itemcode_Table;

    @FXML
    private TableColumn<searchList, String> mpdDate_Table;

    @FXML
    private TableColumn<searchList, String> quantity_Table;

    @FXML
    private TableColumn<searchList, String> sale_Table;

    @FXML
    private TableColumn<searchList, String> NOTable;

    ObservableList<searchList> searchListObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        SearchTable();
    }

    public void SearchTable() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
//        SQL Query to view
        String InventoryViewQuery = "SELECT p_no,batch_no,exp,mfd,buying_price,sellig_price,qty,c_no,barcode FROM purchase";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet QueryOutPut = statement.executeQuery(InventoryViewQuery);

            while (QueryOutPut.next()) {
                Integer queryPno = QueryOutPut.getInt("p_no");
                String queryBatch_no = QueryOutPut.getString("batch_no");
                java.sql.Date queryEXPDate = QueryOutPut.getDate("exp");
                Date queryMDPDate = QueryOutPut.getDate("mfd");
                Integer queryCostPrice = QueryOutPut.getInt("buying_price");
                Integer querySalePrice = QueryOutPut.getInt("sellig_price");
                Integer queryQuantity = QueryOutPut.getInt("qty");
                String queryCompanyNo = QueryOutPut.getNString("c_no");
                String queryItemcode = QueryOutPut.getString("barcode");


                searchListObservableList.add(new searchList(queryPno,queryItemcode, queryEXPDate, queryMDPDate, queryCostPrice, querySalePrice,queryQuantity, queryBatch_no, queryCompanyNo));
            }

                NOTable.setCellValueFactory(new PropertyValueFactory<>("pno"));
                itemcode_Table.setCellValueFactory(new PropertyValueFactory<>("itemcode"));
                expDate_Table.setCellValueFactory(new PropertyValueFactory<>("EXP"));
                mpdDate_Table.setCellValueFactory(new PropertyValueFactory<>("MPD"));
                cost_Table.setCellValueFactory(new PropertyValueFactory<>("cost_per_unit"));
                sale_Table.setCellValueFactory(new PropertyValueFactory<>("sale_per_unit"));
                quantity_Table.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                batch_Table.setCellValueFactory(new PropertyValueFactory<>("batch_no"));
                companyNO_Table.setCellValueFactory(new PropertyValueFactory<>("Com_No"));



            SearchTableView.setItems(searchListObservableList);

//          Initial filtered list
            FilteredList<searchList> filteredData = new FilteredList<>(searchListObservableList, b -> true);
            searchText.textProperty().addListener((observable, newValue, oldValue) -> {
                filteredData.setPredicate(searchList -> {
//                    if no search value
                    if (newValue.isBlank() || newValue.isEmpty() || newValue == null) {
                        return true;
                    }

                    String searchKeyWord = newValue.toLowerCase();

                    if (searchList.getItemcode().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in itemcode
                    } else if (searchList.getBatch_no().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in name
                    } else if (searchList.getCom_No().toString().indexOf(searchKeyWord) > -1) {
                        return true; // that means we found a match in price
                    } else if (searchList.getPno().toString().indexOf(searchKeyWord) > -1) {
                        return true;
                    }  else {
                        return false; //no match found
                    }
                });
            });

            SortedList<searchList> sortedData = new SortedList<>(filteredData);
//          bind sorted result with table view
            sortedData.comparatorProperty().bind(SearchTableView.comparatorProperty());
//          Apply sorted and filtered data with table
            SearchTableView.setItems(sortedData);


        } catch (SQLException e) {
            Logger.getLogger(inward.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } }

    }

