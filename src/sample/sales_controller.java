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
import java.util.*;

public class sales_controller implements Initializable {

    private Integer av_qty=0;
    private double price,amount;
    private int bill_item_no=0;
    private String qty = null;
    double Total;


    @FXML
    public TextField tf_code;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_price;
    @FXML
    private TextField tf_des;
    @FXML
    private TextField tf_qty;
    @FXML
    private TextField tf_tot;

    @FXML
    private Button btn_done,btn_add,btn_pro,btn_clear;

    @FXML
    private TableView<Checkout>tbl;
    @FXML
    TableColumn<Checkout, Integer> tbl_no,tbl_qty;
    @FXML
    TableColumn<Checkout,String> tbl_code,tbl_name,tbl_price,tbl_des;
    @FXML
    TableColumn<Checkout,Double> tbl_amount;

    ObservableList<Checkout> checkoutsListObservableList = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFields.bindAutoCompletion(tf_code,suggesting("d_code"));
        TextFields.bindAutoCompletion(tf_name,suggesting("d_name"));

        btn_done.setOnAction(event -> {
            tf_des.clear();
            tf_name.clear();
            tf_price.clear();
            tf_qty.clear();
            SettingValues(tf_code.getText());
        });

        btn_add.setOnAction(event -> {
            setQty();
            System.out.println(tf_des.getText());
            amount=Integer.parseInt(getQty())*price;
            AddItem();
            clear_tf();
        });

        tbl.setRowFactory(checkoutTableView -> {
            TableRow<Checkout> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2){
                    int index = checkoutTableView.getSelectionModel().getFocusedIndex();
                    ButtonType yes = new ButtonType("Yes");
                    ButtonType no = new ButtonType("No");
                    Alert a = new Alert(Alert.AlertType.NONE,"Do you want to remove this..?",yes,no);
                    System.out.println(tbl.getItems().get(index).getD_code());
                    a.setAlertType(Alert.AlertType.CONFIRMATION);
                    a.setResizable(false);
                    a.showAndWait().ifPresent(response -> {
                        if (response == yes) {
                            Checkout selectedItem = tbl.getSelectionModel().getSelectedItem();
                            checkoutsListObservableList.remove(selectedItem);
                            Total = Total-selectedItem.getAmount();
                            tf_tot.setText(String.valueOf(Total));
                            tbl.getItems().remove(selectedItem);

                            System.out.println(checkoutsListObservableList);
                        }
                    });
                }
            });
            return row;
        });

        btn_clear.setOnAction(event -> {
            clear_tf();
            tf_tot.clear();
            tbl.getItems().clear();
            checkoutsListObservableList.clear();

        });
    }

    public void clear_tf(){
        tf_code.clear();
        tf_des.clear();
        tf_name.clear();
        tf_price.clear();
        tf_qty.clear();
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

    }

    public void SettingValues(String d_code){
        Connection connection;
        ResultSet resultSet1,resultSet2;
        String barcode =null;
        String d_name = null;
        String d_des = null;
        ArrayList<String> list1 = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb", "root", "Whoiam@123");
            PreparedStatement statement1 = connection.prepareStatement("SELECT barcode,qty,d_name,description FROM drug WHERE d_code=?");
            PreparedStatement statement2 = connection.prepareStatement("SELECT p_no,sellig_price,qty FROM purchase WHERE (availability='y' and barcode=?)");
            statement1.setString(1,d_code);
            resultSet1 = statement1.executeQuery();
            while (resultSet1.next()){
                barcode = (resultSet1.getNString("barcode"));
                av_qty = resultSet1.getInt("qty");
                d_des = resultSet1.getNString("description");
                d_name = resultSet1.getNString("d_name");
            }

            statement2.setString(1,barcode);
            resultSet2 = statement2.executeQuery();
            while (resultSet2.next()){
                list1.add(String.valueOf(resultSet2.getInt("p_no")));
                list1.add(String.valueOf(resultSet2.getDouble("sellig_price")));
                list1.add(String.valueOf(resultSet2.getInt("qty")));
            }

            System.out.println(list1);
            System.out.println(list1.size());
            int ii=list1.size()-1;
            price = Double.parseDouble(list1.get(ii-1));
            while(av_qty>0 && ii>=3){
                System.out.println(ii);
                if(av_qty > Integer.parseInt(list1.get(ii))){
                    av_qty= av_qty - Integer.parseInt(list1.get(ii-2));
                    ii=ii-3;
                }
                else {
                    price = Double.parseDouble(list1.get(ii));
                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tf_price.setText(String.valueOf(price));


        tf_code.setText(d_code);
        tf_name.setText(d_name);
        tf_des.setText(d_des);
    }

    public void AddItem(){
        String d_code = tf_code.getText();
        String d_name = tf_name.getText();
        String d_price = tf_price.getText();
        String d_des = tf_des.getText();
        bill_item_no=bill_item_no+1;
        System.out.println(bill_item_no);

        checkoutsListObservableList.add(new Checkout(bill_item_no, d_code, d_name, d_price, d_des,Integer.parseInt(qty),amount));

        tbl_no.setCellValueFactory(new PropertyValueFactory<>("bill_item_no"));
        tbl_code.setCellValueFactory(new PropertyValueFactory<>("d_code"));
        tbl_name.setCellValueFactory(new PropertyValueFactory<>("d_name"));
        tbl_price.setCellValueFactory(new PropertyValueFactory<>("d_price"));
        tbl_des.setCellValueFactory(new PropertyValueFactory<>("d_des"));
        tbl_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        tbl_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tbl.setItems(checkoutsListObservableList);

        Total=Total+amount;
        tf_tot.setText(String.valueOf(Total));
    }

    public void setQty(){
          this.qty=tf_qty.getText();
    }
    public String getQty(){
        return qty;
    }
}
