package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class outward implements Initializable {
    @FXML
    private Button ClearButton;
    @FXML
    private TableView<outwardList> InventoryTableView;
    @FXML
    private TextField PNoText;
    @FXML
    private Button ProcessButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField batchNoText;
    @FXML
    private TableColumn<outward, String> batch_Table;
    @FXML
    private TableColumn<outward, String> NoTable;
    @FXML
    private TableColumn<outward, String> companyNO_Table;
    @FXML
    private TextField companyNoText;
    @FXML
    private TextField costPriceText;
    @FXML
    private TableColumn<outward, String> cost_Table;
    @FXML
    private TextField dateText;
    @FXML
    private TextField expDateText;
    @FXML
    private TableColumn<outward, String> expDate_Table;
    @FXML
    private Button fillButton;
    @FXML
    private TextField itemcodeText;

    @FXML
    private TableColumn<outward, String> itemcode_Table;

    @FXML
    private TextField mpdDateText;

    @FXML
    private TableColumn<outward, String> mpdDate_Table;

    @FXML
    private TextField quantityText;

    @FXML
    private TableColumn<outward, String> quantity_Table;

    @FXML
    private Label successLabel;

    @FXML
    private TextField timeText;

    ObservableList<outwardList> outwardListObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        PNoText.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));

        addButton.setOnAction(actionEvent -> {
            AddItems();
            clearFields();
        });

        fillButton.setOnAction(actionEvent -> {
            getValues();
        });

        InventoryTableView.setRowFactory(InventoryTableView -> {
            TableRow<outwardList> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2){
                    int index = InventoryTableView.getSelectionModel().getFocusedIndex();
                    ButtonType yes = new ButtonType("Yes");
                    ButtonType no = new ButtonType("No");
                    Alert a = new Alert(Alert.AlertType.NONE,"Do you want to remove this..?",yes,no);
                    System.out.println(InventoryTableView.getItems().get(index).getBatch_no());
                    a.setAlertType(Alert.AlertType.CONFIRMATION);
                    a.setResizable(false);
                    a.showAndWait().ifPresent(response -> {
                        if (response == yes) {
                            outwardList selectedItem = InventoryTableView.getSelectionModel().getSelectedItem();
                            outwardListObservableList.remove(selectedItem);
                            InventoryTableView.getItems().remove(selectedItem);
                            clearFields();
                        }
                    });
                }
            });
            return row;
        });

        ClearButton.setOnAction(event -> {
            clearFields();
            InventoryTableView.getItems().clear();
            outwardListObservableList.clear();
        });

        ProcessButton.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            Alert a = new Alert(Alert.AlertType.NONE,"Are sure..?",yes,no);
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setResizable(false);
            a.showAndWait().ifPresent(response -> {
                if (response == yes) {
                    UpdateBD();
                    clearFields();
                    InventoryTableView.getItems().clear();
                    outwardListObservableList.clear();
                    InventoryTableView.getItems().clear();

                }
            });

        });


    }


    private void clearFields() {
        PNoText.clear();
        batchNoText.clear();
        itemcodeText.clear();
        companyNoText.clear();
        dateText.clear();
        expDateText.clear();
        mpdDateText.clear();
        quantityText.clear();
        timeText.clear();

    }


        public void getValues(){
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();

        try{
            PreparedStatement ps = null;
            String query = "SELECT p_no,batch_no,barcode,c_no,qty,p_time,p_date,exp,mfd FROM purchase WHERE p_no=?";
            ps = connectDB.prepareStatement(query);
            String P_No = PNoText.getText();
            ps.setString(1,P_No);
            ResultSet resultSet1 = ps.executeQuery();

            String batchNo = null;
            String barcode = null;
            Integer pno = null;
            String comNo = null;
            Integer quantity = null;
            Time time = null;
            Date date = null;
            Date exp  = null;
            Date mpd  = null;


            while (resultSet1.next()){
                pno = resultSet1.getInt("p_no");
                batchNo = resultSet1.getNString("batch_no");
                barcode = resultSet1.getNString("barcode");
                comNo = resultSet1.getNString("c_no");
                quantity = resultSet1.getInt("qty");
                time = resultSet1.getTime("p_time");
                date = resultSet1.getDate("p_date");
                exp = resultSet1.getDate("exp");
                mpd = resultSet1.getDate("mfd");

            }
            itemcodeText.setText(barcode);
            batchNoText.setText(batchNo);
            companyNoText.setText(comNo);
            quantityText.setText(String.valueOf(quantity));
            batchNoText.setText(batchNo);
            timeText.setText(String.valueOf(time));
            dateText.setText(String.valueOf(date));
            expDateText.setText(String.valueOf(exp));
            mpdDateText.setText(String.valueOf(mpd));

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void AddItems() {

        try {
            Integer querypno = Integer.valueOf(PNoText.getText());
            String queryBatch_no = batchNoText.getText();
            LocalDate queryexp = LocalDate.parse(expDateText.getText());
            LocalDate querymdp = LocalDate.parse(mpdDateText.getText());
            Integer queryQuantity = Integer.valueOf(quantityText.getText());
            String queryCompanyNo = companyNoText.getText();
            String queryItemcode = itemcodeText.getText();


            outwardListObservableList.add(new outwardList(querypno,queryItemcode,queryexp,querymdp,queryQuantity,queryBatch_no,queryCompanyNo));


            itemcode_Table.setCellValueFactory(new PropertyValueFactory<>("itemcode"));
            expDate_Table.setCellValueFactory(new PropertyValueFactory<>("EXP"));
            mpdDate_Table.setCellValueFactory(new PropertyValueFactory<>("MPD"));
            NoTable.setCellValueFactory(new PropertyValueFactory<>("p_no"));
            quantity_Table.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            batch_Table.setCellValueFactory(new PropertyValueFactory<>("batch_no"));
            companyNO_Table.setCellValueFactory(new PropertyValueFactory<>("Com_No"));

            InventoryTableView.setItems(outwardListObservableList);



        } catch (Exception e) {
            Logger.getLogger(inward.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }

    public void UpdateBD(){

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
        try {
            PreparedStatement ps = null;
            String query = "DELETE FROM `pmsdb`.`purchase` WHERE (`p_no` = ?)";
            ps = connectDB.prepareStatement(query);

            for (outwardList i : outwardListObservableList) {

                Integer PNO = i.getP_no();

                ps.setInt(1, PNO);

                ps.executeUpdate();

            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("SUCCESSFULLY UPDATED");
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }










}
