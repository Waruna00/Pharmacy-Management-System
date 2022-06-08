package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.event.TableModelEvent.INSERT;

public class inward implements Initializable {

    //    Labels
    @FXML
    private Label successLabel;

    //    buttons
    @FXML
    private Button addButton;
    @FXML
    private Button btnProcess, btn_clear;
    @FXML
    private Button fillButton;


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
    private TableColumn<ProductAdd, String> itemname_Table;
    @FXML
    private TableColumn<ProductAdd, String> description_Table;


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
    private TextField itemNameText;
    @FXML
    private TextField descriptionText;
    @FXML
    private DatePicker mdpPicker;
    @FXML
    private DatePicker expPicker;
    @FXML
    private Button btn_add, btn_com;

    ObservableList<ProductAdd> productAddObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        quantityText.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
        salePriceText.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));
        costPriceText.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));

        addButton.setOnAction(actionEvent -> {
            AddItems();
            clearFields();
        });
        fillButton.setOnAction(actionEvent -> {
            getValues();
        });

        btn_add.setOnAction(actionEvent -> {
            welcome_controller window = new welcome_controller();
            window.NewWindow("drug.fxml", "DURGS");
        });

        btn_com.setOnAction(actionEvent -> {
            welcome_controller window = new welcome_controller();
            window.NewWindow("company.fxml", "COMPANY");
        });

        InventoryTableView.setRowFactory(InventoryTableView -> {
            TableRow<ProductAdd> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    int index = InventoryTableView.getSelectionModel().getFocusedIndex();
                    ButtonType yes = new ButtonType("Yes");
                    ButtonType no = new ButtonType("No");
                    Alert a = new Alert(Alert.AlertType.NONE, "Do you want to remove this..?", yes, no);
                    System.out.println(InventoryTableView.getItems().get(index).getBatch_no());
                    a.setAlertType(Alert.AlertType.CONFIRMATION);
                    a.setResizable(false);
                    a.showAndWait().ifPresent(response -> {
                        if (response == yes) {
                            ProductAdd selectedItem = InventoryTableView.getSelectionModel().getSelectedItem();
                            productAddObservableList.remove(selectedItem);
                            InventoryTableView.getItems().remove(selectedItem);
                        }
                    });
                }
            });
            return row;
        });

        btnProcess.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            Alert a = new Alert(Alert.AlertType.NONE, "Are sure..?", yes, no);
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setResizable(false);
            a.showAndWait().ifPresent(response -> {
                if (response == yes) {
                    UpdateBD();
                    clearFields();
                    InventoryTableView.getItems().clear();
                    productAddObservableList.clear();
                    InventoryTableView.getItems().clear();

                }
            });

        });

        btn_clear.setOnAction(event -> {
            clearFields();
            InventoryTableView.getItems().clear();
            productAddObservableList.clear();
        });

        TextFields.bindAutoCompletion(itemcodeText, suggesting("d_code"));

    }

    private void clearFields() {
        batchNoText.clear();
        itemcodeText.clear();
        itemNameText.clear();
        companyNoText.clear();
        salePriceText.clear();
        costPriceText.clear();
        quantityText.clear();
        descriptionText.clear();

    }

    public void AddItems() {
        try {
            String queryBatch_no = batchNoText.getText();
            LocalDate queryEXPDate = expPicker.getValue();
            LocalDate queryMDPDate = mdpPicker.getValue();
            String queryCostPrice = costPriceText.getText();
            String querySalePrice = salePriceText.getText();
            String queryQuantity = quantityText.getText();
            String queryCompanyNo = companyNoText.getText();
            String queryItemcode = itemcodeText.getText();
            String queryItemname = itemNameText.getText();
            String queryDescription = descriptionText.getText();

            productAddObservableList.add(new ProductAdd(queryItemcode, queryItemname, queryDescription, queryEXPDate, queryMDPDate, Integer.parseInt(queryCostPrice), Integer.parseInt(querySalePrice), Integer.parseInt(queryQuantity), queryBatch_no, queryCompanyNo));

            itemcode_Table.setCellValueFactory(new PropertyValueFactory<>("itemcode"));
            itemname_Table.setCellValueFactory(new PropertyValueFactory<>("itemname"));
            description_Table.setCellValueFactory(new PropertyValueFactory<>("description"));
            expDate_Table.setCellValueFactory(new PropertyValueFactory<>("EXP"));
            mpdDate_Table.setCellValueFactory(new PropertyValueFactory<>("MPD"));
            cost_Table.setCellValueFactory(new PropertyValueFactory<>("cost_per_unit"));
            sale_Table.setCellValueFactory(new PropertyValueFactory<>("sale_per_unit"));
            quantity_Table.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            batch_Table.setCellValueFactory(new PropertyValueFactory<>("batch_no"));
            companyNO_Table.setCellValueFactory(new PropertyValueFactory<>("Com_No"));

            InventoryTableView.setItems(productAddObservableList);
        } catch (Exception e) {
            Logger.getLogger(inward.class.getName()).log(Level.SEVERE, "enter correct data types", e);
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("enter correct data type");
            alert.show();

        }

    }

    public ArrayList<String> suggesting(String x) {
        ArrayList<String> d_codeArray = new ArrayList<>();
        Connection connection;
        ResultSet resultSet;
        try {
            DBUtils con = new DBUtils();
            connection = con.connection();
            PreparedStatement statement = connection.prepareStatement("SELECT d_code,d_name,d_type,description FROM drug");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String d_code = resultSet.getNString("d_code");
                d_codeArray.add(d_code);

            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return d_codeArray;
    }

    public void getValues() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
        try {
            PreparedStatement ps = null;
            String query = "SELECT d_name,description FROM drug WHERE d_code=?";
            ps = connectDB.prepareStatement(query);
            String Itemcode = itemcodeText.getText();
            ps.setString(1, Itemcode);
            ResultSet resultSet1 = ps.executeQuery();

            String d_name = null;
            String d_des = null;
            while (resultSet1.next()) {
                d_des = resultSet1.getNString("description");
                d_name = resultSet1.getNString("d_name");
            }
            itemNameText.setText(d_name);
            descriptionText.setText(d_des);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid Itemcode");
            alert.show();
        }

    }

    public void UpdateBD() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
        try {
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            String query = "INSERT INTO `pmsdb`.`purchase` (`batch_no`, `barcode`, `c_no`, `emp_no`, `qty`, `p_time`, `p_date`, `exp`, `mfd`, `sellig_price`, `buying_price`, `availability`) VALUES (?,?, ?,?, ?,?,?, ?,?,?,?, 'y')";
            String query1 = "UPDATE `pmsdb`.`drug` SET `qty` = ? WHERE (`d_code` = ?)";
            String query2 = "SELECT qty FROM drug WHERE d_code=?";
            ps = connectDB.prepareStatement(query);
            ps2 = connectDB.prepareStatement(query1);
            ps1 = connectDB.prepareStatement(query2);

            for (ProductAdd i : productAddObservableList) {
                String batchNO = i.getBatch_no();
                String barcode = i.getItemcode();
                String Cno = i.getCom_No();
                String empno = "MGR01";
                Integer qty = i.getQuantity();
                LocalTime time = LocalTime.now();
                LocalDate date = LocalDate.now();
                LocalDate EXP = i.getEXP();
                LocalDate MPD = i.getMPD();
                Integer Sprice = i.getSale_per_unit();
                Integer Bprice = i.getCost_per_unit();

                String Itemcode = i.getItemcode();
                ps1.setString(1, Itemcode);
                ResultSet resultSet1 = ps1.executeQuery();
                Integer quantity = null;
                while (resultSet1.next()) {
                    quantity = resultSet1.getInt("qty");
                }
                Integer totalQuantity = (qty + quantity);
                ps2.setInt(1, totalQuantity);
                ps2.setString(2, barcode);
                ps2.executeUpdate();
                ps.setString(1, String.valueOf(batchNO));
                ps.setString(2, String.valueOf(barcode));
                ps.setString(3, String.valueOf(Cno));
                ps.setString(4, empno);
                ps.setInt(5, qty);
                ps.setTime(6, Time.valueOf(time));
                ps.setDate(7, java.sql.Date.valueOf(date));
                ps.setDate(8, java.sql.Date.valueOf(EXP));
                ps.setDate(9, java.sql.Date.valueOf(MPD));
                ps.setInt(10, Sprice);
                ps.setInt(11, Bprice);


                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

