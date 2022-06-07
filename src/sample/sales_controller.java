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
    private double price,amount;
    private int bill_item_no=0;
    private int bill_no = 0;
    private String qty = null;
    private double Total;
    private String cust_no=null;
    private ArrayList<String> av_qtyList=new ArrayList<>();

    @FXML
    public Label lbl_bill_no;
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
    public TextField tf_tot;
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
    private final Alert alert = new Alert(Alert.AlertType.NONE);
    private final DBUtils connect = new DBUtils();
    private Connection connection=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connection = connect.connection();
            PreparedStatement statement5 = connection.prepareStatement("SELECT MAX(bill_no) AS bill_no FROM bill;");
            ResultSet R_bill_no=statement5.executeQuery();

            while (R_bill_no.next()){
                bill_no=R_bill_no.getInt("bill_no");
            }
            bill_no=bill_no+1;
        }catch (SQLException e){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText(String.valueOf(e));
        }

        lbl_bill_no.setText(String.valueOf(bill_no));
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
            String new_av_qty="0";
            String d_code = tf_code.getText();
            try{
                if (!(av_qtyList.contains(d_code))){
                    PreparedStatement statement =connection.prepareStatement ("SELECT qty FROM drug WHERE d_code=?");
                    statement.setString(1,tf_code.getText());
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        av_qtyList.add(tf_code.getText());
                        av_qtyList.add(String.valueOf(resultSet.getInt("qty")));
                        new_av_qty = String.valueOf(resultSet.getInt("qty"));
                    }
                }
                else {
                    int i =av_qtyList.indexOf(d_code)+1;
                    new_av_qty = String.valueOf(Integer.parseInt(av_qtyList.get(i)));
                }

            }catch (SQLException e){
                e.printStackTrace();
            }

            if (Integer.parseInt(new_av_qty)>=Integer.parseInt(qty)){
                av_qtyList.set(av_qtyList.indexOf(d_code)+1,String.valueOf(Integer.parseInt(new_av_qty)-Integer.parseInt(qty)));
                amount=Integer.parseInt(getQty())*price;
                AddItem();
                clear_tf();
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Insufficient Quantity on hand ("+new_av_qty+" remaining)");
                alert.show();
            }

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
            Total=0;
        });

        btn_pro.setOnAction(event -> {
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            Alert a = new Alert(Alert.AlertType.NONE,"Are you sure..?",yes,no);
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setResizable(false);
            a.showAndWait().ifPresent(response -> {
                if (response == yes) {
                    SentData();
                    clear_tf();
                    tbl.getItems().clear();
                    checkoutsListObservableList.clear();
                    tbl.getItems().clear();
                    Total=0;

                }
            });

        });
    }

    public void SentData(){

        try{
            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO sale(qty,b_date,amount,b_time,barcode,cust_no,bill_no) VALUES(?,?,?,?,?,?,?);");
            PreparedStatement statement2 = connection.prepareStatement("SELECT barcode,qty FROM drug WHERE d_code=?;");
            PreparedStatement statement3 = connection.prepareStatement("UPDATE drug SET qty = ? WHERE (d_code = ?);");
            PreparedStatement statement4 = connection.prepareStatement("INSERT INTO bill(tot_ammount,cust_no,date) VALUES(?,?,?);");
            PreparedStatement statement6 = connection.prepareStatement("SELECT COUNT(*) AS count FROM dayend WHERE (date=?);");
            PreparedStatement statement7 = connection.prepareStatement("INSERT INTO dayend(date) VALUES(?)");

            statement6.setString(1,String.valueOf(java.time.LocalDate.now()));
            int c=0;
            ResultSet R_dayend = statement6.executeQuery();
            while (R_dayend.next()){
                c=R_dayend.getInt("count");
            }
            if (c!=1){
                statement7.setString(1,String.valueOf(java.time.LocalDate.now()));
                statement7.execute();
            }

            statement4.setDouble(1,Total);
            statement4.setString(2, Objects.requireNonNullElse(cust_no, "001"));
            statement4.setString(3,String.valueOf(java.time.LocalDate.now()));
            statement4.execute();

            for (Checkout i : checkoutsListObservableList){
                String barcode=null;
                double available_qty = 0;
                double qty = i.getQty();
                double amount =i.getAmount();
                statement2.setString(1,i.getD_code());
                ResultSet R_Barcode=statement2.executeQuery();
                while (R_Barcode.next()){
                    barcode=R_Barcode.getNString("barcode");
                    available_qty=R_Barcode.getDouble("qty");

                }
                statement3.setString(1,String.valueOf(available_qty-(i.getQty())));
                statement3.setString(2,i.getD_code());
                statement3.executeUpdate();


                statement1.setString(1,String.valueOf(qty));
                statement1.setString(2,String.valueOf(java.time.LocalDate.now()));
                statement1.setString(3,String.valueOf(amount));
                statement1.setString(4,String.valueOf(java.time.LocalTime.now()));
                statement1.setString(5,barcode);
                statement1.setString(6, Objects.requireNonNullElse(cust_no, "001")); //cust_no
                statement1.setInt(7,bill_no); //bill_no
                statement1.executeUpdate();



            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void clear_tf(){
        tf_code.clear();
        tf_des.clear();
        tf_name.clear();
        tf_price.clear();
        tf_qty.clear();
        bill_item_no=0;
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
            int av_qty1 = 0;
            while (resultSet1.next()){
                barcode = (resultSet1.getNString("barcode"));
                av_qty1 = resultSet1.getInt("qty");
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
            int ii=list1.size()-1;
            price = Double.parseDouble(list1.get(ii-1));
            while(av_qty1>0 && ii>=3){
                if(av_qty1 > Integer.parseInt(list1.get(ii))){
                    av_qty1= av_qty1 - Integer.parseInt(list1.get(ii-2));
                }
                else {
                    price = Double.parseDouble(list1.get(ii));
                }
                ii=ii-3;

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
