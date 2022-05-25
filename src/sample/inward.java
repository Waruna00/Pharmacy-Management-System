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
import org.controlsfx.control.textfield.TextFields;


import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class inward implements Initializable {

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
    private Button btn_add,btn_com;

    ObservableList<ProductAdd> productAddObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        SearchTable();
        TextFields.bindAutoCompletion(itemcodeText,suggesting("d_code"));
        TextFields.bindAutoCompletion(itemcodeText,suggesting("d_code"));

        btn_add.setOnAction(actionEvent -> {

        });

        btn_com.setOnAction(actionEvent -> {

        });

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



        } catch (SQLException e) {
            Logger.getLogger(inward.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }





    public ArrayList<String> suggesting(String x){
        ArrayList<String> d_codeArray = new ArrayList<>();
        ArrayList<String> d_nameArray = new ArrayList<>();
        Connection connection;
        ResultSet resultSet;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb","root","Whoiam@123");
            PreparedStatement statement = connection.prepareStatement("SELECT d_code,d_name,d_type,description FROM drug");
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                String d_code = resultSet.getNString("d_code");
                String d_name=resultSet.getNString("d_name");

                d_codeArray.add(d_code);
                d_nameArray.add(d_name);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        if (x.equals("d_code")){
            return d_codeArray;
        }
        else {
            return d_nameArray;
        }





//   public void ToAddCom(ActionEvent event){
//       DBUtils.changeScene(event,"company.fxml","Add Company",null,809,480);
//   }
//    public void ToAddDrug(ActionEvent event){
//        DBUtils.changeScene(event,"Drug.fxml","Add Drug",null,853,510);
//    }


}}

