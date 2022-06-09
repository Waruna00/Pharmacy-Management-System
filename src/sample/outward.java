package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class outward implements Initializable {
    @FXML
    private Button ClearButton;
    @FXML
    private Button ProcessButton;
    @FXML
    private Button btn_fill;
    @FXML
    private Button btn_add;
    @FXML
    private TextField tf_code;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_des;
    @FXML
    private TextField tf_qty;
    @FXML
    private TableView<outwardList> InventoryTableView;
    @FXML
    private TableColumn<outwardList, Integer> tbl_no;
    @FXML
    private TableColumn<outwardList, String> tbl_code;
    @FXML
    private TableColumn<outwardList, String> tbl_name;
    @FXML
    private TableColumn<outwardList, String> tbl_des;
    @FXML
    private TableColumn<outwardList, Integer> tbl_qty;
    @FXML
    private Label successLabel;
    int qty = 0;
    String code=null;
    String name = null;
    String des = null;
    int number=1;
    ObservableList<outwardList> outwardListObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        TextFields.bindAutoCompletion(tf_code,suggesting("d_code"));
        tf_qty.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null));

        btn_add.setOnAction(actionEvent -> {
            AddItems();
            tf_qty.clear();
        });
        btn_fill.setOnAction(actionEvent -> {
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
        tf_code.clear();
        tf_name.clear();
        tf_des.clear();
        tf_qty.clear();
    }
    public void getValues(){
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
        ResultSet resultSet1=null;
        try{
            PreparedStatement ps = null;
            String query = "SELECT d_name,description,qty FROM drug WHERE d_code=?;";
            ps = connectDB.prepareStatement(query);
            code = tf_code.getText();
            ps.setString(1,code);
            resultSet1 = ps.executeQuery();


            while (resultSet1.next()){
                name = resultSet1.getNString("d_name");
                des = resultSet1.getNString("description");
                qty = resultSet1.getInt("qty");
            }
            tf_name.setText(name);
            tf_des.setText(des);
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connectDB!=null){
                try {
                    connectDB.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (resultSet1!=null){
                try {
                    resultSet1.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void AddItems() {
        Alert a =new Alert(Alert.AlertType.NONE);
        if (tf_qty.getText().trim().isEmpty()){
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Quantity can not be null");
            a.show();
        }else{
            int q = Integer.parseInt(tf_qty.getText());
            System.out.println(q);
            System.out.println(qty);
            if (q<=qty && q>0){
                try {
                    outwardListObservableList.add(new outwardList(number,code,name,des,q));

                    tbl_no.setCellValueFactory(new PropertyValueFactory<>("no"));
                    tbl_code.setCellValueFactory(new PropertyValueFactory<>("code"));
                    tbl_name.setCellValueFactory(new PropertyValueFactory<>("name"));
                    tbl_des.setCellValueFactory(new PropertyValueFactory<>("des"));
                    tbl_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));

                    InventoryTableView.setItems(outwardListObservableList);
                    clearFields();
                    number++;

                } catch (Exception e) {
                    Logger.getLogger(inward.class.getName()).log(Level.SEVERE, null, e);
                    e.printStackTrace();
                }
            }else {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("Insufficient quantity on hand");
                a.show();
            }
        }
    }
    public void UpdateBD(){

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.Connect();
        try {
            PreparedStatement ps = null;
            String query = "UPDATE drug SET qty=qty-? WHERE d_code=?;";
            ps = connectDB.prepareStatement(query);

            for (outwardList i : outwardListObservableList) {

                String dcode = i.getCode();
                int qt = i.getQty();
                ps.setInt(1,qt);
                ps.setString(2, dcode);
                ps.executeUpdate();

            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Successfully Updated..!");
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connectDB!=null){
                try {
                    connectDB.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public ArrayList<String> suggesting(String x){
        ArrayList<String> d_codeArray = new ArrayList<>();
        ArrayList<String> d_nameArray = new ArrayList<>();
        Connection connection;
        ResultSet resultSet;
        try {
            DBUtils con = new DBUtils();
            connection = con.connection();
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

    }
}
