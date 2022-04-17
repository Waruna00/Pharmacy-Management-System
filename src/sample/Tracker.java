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
import javafx.scene.paint.Color;




import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Tracker implements Initializable {

    //    Labels
    @FXML
    private Label successLabel;
    //    columns
    @FXML
    private TableView<ProductAdd> InventoryTableView;
    @FXML
    private TableColumn<ProductAdd, Integer> batch_Table;
    @FXML
    private TableColumn<ProductAdd, String> itemcode_Table;
    @FXML
    private TableColumn<ProductAdd, String> companyNO_Table;
    @FXML
    private TableColumn<ProductAdd, String> mpdDate_Table;
    @FXML
    private TableColumn<ProductAdd, Integer> cost_Table;
    @FXML
    private TableColumn<ProductAdd, Integer> sale_Table;
    @FXML
    private TableColumn<ProductAdd, Integer> quantity_Table;
    @FXML
    private TableColumn<ProductAdd, Date> expDate_Table;
    @FXML
    private TableColumn<ProductAdd, Date> date_Table;
    @FXML
    private TableColumn<ProductAdd, Date> time_Table;

//    text inputs

    @FXML
    private TextField batchNoText;
    @FXML
    private TextField itemcodeText;
    @FXML
    private TextField companyNoText;
    @FXML
    private TextField costPriceText;
    @FXML
    private TextField salePriceText;
    @FXML
    private TextField quantityText;
    @FXML
    private TextField expDateText;
    @FXML
    private TextField dateText;
    @FXML
    private TextField searchText;
    @FXML
    private TextField mpdDateText;
    @FXML
    private TextField timeText;

    //    button
    @FXML
    private Button addButton;

    ObservableList<ProductAdd> productAddObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        SearchTable();
    }

    private void clearFields() {
        batchNoText.clear();
        itemcodeText.clear();
        companyNoText.clear();
        salePriceText.clear();
        costPriceText.clear();
        mpdDateText.clear();
        searchText.clear();
        quantityText.clear();
        expDateText.clear();
        timeText.clear();
        dateText.clear();
    }

    public void SearchTable() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
//        SQL Query to view
        String InventoryViewQuery = "SELECT * FROM purchase";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet QueryOutPut = statement.executeQuery(InventoryViewQuery);

            while (QueryOutPut.next()) {

                Integer queryBatch_no = QueryOutPut.getInt("batch_no");
                Date queryDate = QueryOutPut.getDate("date");
                Time queryTime = QueryOutPut.getTime("time");
                Date queryEXPDate = QueryOutPut.getDate("EXP");
                Date queryMDPDate = QueryOutPut.getDate("MPD");
                Integer queryCostPrice = QueryOutPut.getInt("cost_per_unit");
                Integer querySalePrice = QueryOutPut.getInt("sale_per_unit");
                Integer queryQuantity = QueryOutPut.getInt("quantity");
                Integer queryCompanyNo = QueryOutPut.getInt("Com_No");
                String queryItemcode = QueryOutPut.getString("itemcode");


                productAddObservableList.add(new ProductAdd(queryBatch_no, queryDate, queryTime, queryEXPDate, queryMDPDate, queryCostPrice, querySalePrice, queryQuantity, queryCompanyNo, queryItemcode));

            }

            batch_Table.setCellValueFactory(new PropertyValueFactory<>("batch_no"));
            date_Table.setCellValueFactory(new PropertyValueFactory<>("date"));
            time_Table.setCellValueFactory(new PropertyValueFactory<>("time"));
            expDate_Table.setCellValueFactory(new PropertyValueFactory<>("EXP"));
            mpdDate_Table.setCellValueFactory(new PropertyValueFactory<>("MPD"));
            cost_Table.setCellValueFactory(new PropertyValueFactory<>("cost_per_unit"));
            sale_Table.setCellValueFactory(new PropertyValueFactory<>("sale_per_unit"));
            quantity_Table.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            itemcode_Table.setCellValueFactory(new PropertyValueFactory<>("itemcode"));
            companyNO_Table.setCellValueFactory(new PropertyValueFactory<>("Com_No"));


            InventoryTableView.setItems(productAddObservableList);

//          Initial filtered list
            FilteredList<ProductAdd> filteredData = new FilteredList<>(productAddObservableList, b -> true);
            searchText.textProperty().addListener((observable, newValue, oldValue) -> {
                filteredData.setPredicate(ProductAdd -> {
//                    if no search value
                    if (newValue.isBlank() || newValue.isEmpty()) {
                        return true;
                    }

                    String searchKeyWord = newValue.toLowerCase();

                    if (ProductAdd.getItemcode().toLowerCase().contains(searchKeyWord)) {
                        return true; // that means we found a match in itemcode
                    } else if (ProductAdd.getCom_No().toString().contains(searchKeyWord)) {
                        return true; // that means we found a match in name
                    } else if (ProductAdd.getCost_per_unit().toString().contains(searchKeyWord)) {
                        return true; // that means we found a match in price
                    } else if (ProductAdd.getQuantity().toString().contains(searchKeyWord)) {
                        return true;
                    } else if (ProductAdd.getEXP().toString().contains(searchKeyWord)) {
                        return true;
                    } else if (ProductAdd.getDate().toString().contains(searchKeyWord)) {
                        return true; // that means we found a match in name
                    } else if (ProductAdd.getBatch_no().toString().contains(searchKeyWord)) {
                        return true;
                    } else if (ProductAdd.getSale_per_unit().toString().contains(searchKeyWord)) {
                        return true;
                    } else if (ProductAdd.getTime().toString().contains(searchKeyWord)) {
                        return true;
                    } else if (ProductAdd.getMPD().toString().contains(searchKeyWord)) {
                        return true;
                    } else {
                        return false; //no match found
                    }
                });
            });

            SortedList<ProductAdd> sortedData = new SortedList<>(filteredData);
//          bind sorted result with table view
            sortedData.comparatorProperty().bind(InventoryTableView.comparatorProperty());
//          Apply sorted and filtered data with table
            InventoryTableView.setItems(sortedData);


        } catch (SQLException e) {
            Logger.getLogger(Tracker.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }


    @FXML
    void AddButtonONAction(ActionEvent event) throws SQLException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        String text = mpdDateText.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate mdpDateTextAsDate = LocalDate.parse(text, formatter);
        java.sql.Date sqlMpdDate = java.sql.Date.valueOf(mdpDateTextAsDate);

        String text2 = expDateText.getText();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate expDateTextAsDate = LocalDate.parse(text2, formatter2);
        java.sql.Date sqlExpDate = java.sql.Date.valueOf(expDateTextAsDate);

        String text1 = dateText.getText();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate DateTextAsDate = LocalDate.parse(text1, formatter1);
        java.sql.Date sqlDate = java.sql.Date.valueOf(DateTextAsDate);

        String text3 = timeText.getText();
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime TimeTextAsTime = LocalTime.parse(text3, formatter3);
        java.sql.Time sqlTime = java.sql.Time.valueOf(TimeTextAsTime);

        String itemcode = itemcodeText.getText();
        int batch_no = Integer.parseInt(batchNoText.getText());
        int companyNo = Integer.parseInt(companyNoText.getText());
        int costprice = Integer.parseInt(costPriceText.getText());
        int saleprice = Integer.parseInt(salePriceText.getText());
        int quantity = Integer.parseInt(quantityText.getText());

        PreparedStatement ps = null;
        String query = "INSERT INTO `new_project`.`purchase` (`batch_no`, `date`, `time`, `EXP`, `MPD`, `cost_per_unit`, `sale_per_unit`, `quantity`, `Com_No`, `itemcode`) VALUES (?, ?, ?,?, ?, ?, ?, ?,?,?);";
        ps = connectDB.prepareStatement(query);
        ps.setInt(1, batch_no);
        ps.setDate(2, sqlDate);
        ps.setTime(3, sqlTime);
        ps.setDate(4, sqlExpDate);
        ps.setDate(5, sqlMpdDate);
        ps.setInt(6, costprice);
        ps.setInt(7, saleprice);
        ps.setInt(8, quantity);
        ps.setInt(9, companyNo);
        ps.setString(10, itemcode);

        try {
            ps.executeUpdate();
            successLabel.setTextFill(Color.GREEN);
            successLabel.setText("Successfully added");
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void EditButtonOnAction(ActionEvent event) throws SQLException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        String text = mpdDateText.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate mdpDateTextAsDate = LocalDate.parse(text, formatter);
        java.sql.Date sqlMpdDate = java.sql.Date.valueOf(mdpDateTextAsDate);

        String text2 = expDateText.getText();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate expDateTextAsDate = LocalDate.parse(text2, formatter2);
        java.sql.Date sqlExpDate = java.sql.Date.valueOf(expDateTextAsDate);

        String text1 = dateText.getText();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate DateTextAsDate = LocalDate.parse(text1, formatter1);
        java.sql.Date sqlDate = java.sql.Date.valueOf(DateTextAsDate);

        String text3 = timeText.getText();
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime TimeTextAsTime = LocalTime.parse(text3, formatter3);
        java.sql.Time sqlTime = java.sql.Time.valueOf(TimeTextAsTime);


        String itemcode = itemcodeText.getText();
        int batch_no = Integer.parseInt(batchNoText.getText());
        int companyNo = Integer.parseInt(companyNoText.getText());
        int costprice = Integer.parseInt(costPriceText.getText());
        int saleprice = Integer.parseInt(salePriceText.getText());
        int quantity = Integer.parseInt(quantityText.getText());

        PreparedStatement ps = null;
        String query = "UPDATE `new_project`.`purchase` SET `batch_no` = ?, `date` = ?, `time` = ?, `EXP` = ?, `MPD` = ?, `cost_per_unit` = ?, `sale_per_unit` = ?, `quantity` = ?, `Com_No` = ?, `itemcode` = ? WHERE (`batch_no` = ?);";
        ps = connectDB.prepareStatement(query);
        ps.setInt(1, batch_no);
        ps.setDate(2, sqlDate);
        ps.setTime(3, sqlTime);
        ps.setDate(4, sqlExpDate);
        ps.setDate(5, sqlMpdDate);
        ps.setInt(6, costprice);
        ps.setInt(7, saleprice);
        ps.setInt(8, quantity);
        ps.setInt(9, companyNo);
        ps.setString(10, itemcode);
        ps.setInt(11, batch_no);

        try {
            ps.executeUpdate();
            successLabel.setTextFill(Color.GREEN);
            successLabel.setText("Successfully Edited");
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();

        }
    }

    @FXML
    void DeleteButtonOnAction(ActionEvent event) throws SQLException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        int batch_no = Integer.parseInt(batchNoText.getText());


        PreparedStatement ps = null;
        String query = "DELETE FROM `new_project`.`purchase` WHERE (`batch_no` = ?);";
        ps = connectDB.prepareStatement(query);
        ps.setInt(1, batch_no);

        try {

            ps.executeUpdate();
            successLabel.setTextFill(Color.GREEN);
            successLabel.setText("Successfully Deleted");
            clearFields();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

   public void ToAddCom(ActionEvent event){
       DBUtils.changeScene(event,"company.fxml","Add Company",null,809,480);
   }
    public void ToAddDrug(ActionEvent event){
        DBUtils.changeScene(event,"Drug.fxml","Add Drug",null,853,510);
    }


}

