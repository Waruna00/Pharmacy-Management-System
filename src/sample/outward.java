package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


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
    private TextField salePriceText;

    @FXML
    private TableColumn<outward, String> sale_Table;

    @FXML
    private Label successLabel;

    @FXML
    private TextField timeText;

    ObservableList<outwardList> outwardListObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        fillButton.setOnAction(actionEvent -> {
            getValues();
        });

    }



        public void getValues(){
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
        try{
            PreparedStatement ps = null;
            String query = "SELECT p_no,batch_no,barcode,c_no,qty,p_time,p_date,exp,mpd FROM purchase WHERE p_no=?";
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
                mpd = resultSet1.getDate("mpd");

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

}
