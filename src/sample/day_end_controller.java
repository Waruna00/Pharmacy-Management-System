package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class day_end_controller implements Initializable {

    String status=null;
    Double prev_cih,tot_sale,cih,dif=0.0;
    Double tot_rem =0.0;
    LocalDate key;
    @FXML
    private Button btn_add,button_clear,button_view,button_pro;
    @FXML
    private TextField tf_cih;
    @FXML
    private DatePicker date;
    @FXML
    private Label lbl_prev_cih,lbl_tot_sale,lbl_dif,lbl_rem;
    @FXML
    private TableView<RemittanceList> tbl;
    @FXML
    TableColumn<RemittanceList,Integer> tbl_no;
    @FXML
    TableColumn<RemittanceList,String> tbl_acc;
    @FXML
    TableColumn<RemittanceList,String> tbl_des;
    @FXML
    TableColumn<RemittanceList,Double> tbl_amount;

    ObservableList<RemittanceList> RemittanceListObservableList = FXCollections.observableArrayList();
    FXMLLoader loader =null;
    public void cihToZero(){
        tf_cih.setText("00.0");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

        btn_add.setOnAction(actionEvent -> {
            if (!(date.getValue()==null)){
                welcome_controller window = new welcome_controller();
                Stage newWindow = new Stage();
                newWindow.setTitle("Remittance");
                newWindow.setResizable(false);

                //Create view from FXML
                loader = new FXMLLoader(getClass().getResource("remittance.fxml"));


                try{
                    //Parent root = loader.load();

                }catch (Exception e){
                    System.out.println(e);
                }
                //Set view in window
                try {
                    newWindow.setScene(new Scene(loader.load()));


                    //

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Launch
                newWindow.show();
                remittance_controller r = loader.getController();
                r.setDate(date.getValue());
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("View first");
                alert.show();
            }

        });

        button_clear.setOnAction(event -> {
            lbl_prev_cih.setText("00.0");
            lbl_tot_sale.setText("00.0");
            lbl_dif.setText("00.0");
            tf_cih.setText("00.0");
            prev_cih=null;
            tot_sale=null;
            cih=null;
            dif=null;
            tot_rem =00.0;
            key=null;
        });

        button_pro.setOnAction(event -> {

            Connection connection;
            PreparedStatement preparedStatement1 = null,preparedStatement2,preparedStatement3,preparedStatement4;
            ResultSet resultSet1,resultSet3;
            String status=null,day = null;
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb","root","Whoiam@123");
                preparedStatement1 = connection.prepareStatement("SELECT status FROM dayend WHERE date=?");
                preparedStatement2 = connection.prepareStatement("INSERT INTO dayend(date,cih,tot_sale,remittance,status,difference)values(?,?,?,?,'yes',?)");
                preparedStatement3 = connection.prepareStatement("SELECT date FROM dayend WHERE date=?;");
                preparedStatement4 = connection.prepareStatement("UPDATE dayend SET cih = ?,tot_sale = ?, remittance = ?, status = 'yes', difference=? WHERE date = ?;");

                String strDate = String.valueOf(date.getValue());
                preparedStatement1.setString(1,strDate);
                resultSet1 = preparedStatement1.executeQuery();

                while (resultSet1.next()){
                    status=resultSet1.getNString("status");
                }

                if (date.getValue()!=key){
                    System.out.println("Not Viewed");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("View once for that day");
                    alert.show();
                }
                else {
                    if (status==null){
                        preparedStatement3.setString(1,strDate);
                        resultSet3 = preparedStatement3.executeQuery();
                        while (resultSet3.next()){
                            day= String.valueOf(resultSet3.getDate("date"));
                        }
                        if (strDate.equals(day)){
                            preparedStatement4.setString(1,String.valueOf(cih));
                            preparedStatement4.setString(2,String.valueOf(tot_sale));
                            preparedStatement4.setString(3,String.valueOf(tot_rem));
                            preparedStatement4.setString(4,String.valueOf(dif));
                            preparedStatement4.setString(5,strDate);

                            preparedStatement4.executeUpdate();
                        }
                        else {
                            preparedStatement2.setString(1,strDate);
                            preparedStatement2.setString(2,String.valueOf(cih));
                            preparedStatement2.setString(3,String.valueOf(tot_sale));
                            preparedStatement2.setString(4,String.valueOf(tot_rem));
                            preparedStatement2.setString(5,String.valueOf(dif));
                            preparedStatement2.executeUpdate();
                        }
                        System.out.println("Successfully Updated");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Successfully Updated");
                        alert.show();
                    }
                    else {
                        System.out.println("Dayend Already Completed");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Already Completed");
                        alert.show();
                    }
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            finally {
                if (preparedStatement1 != null){
                    try{
                        preparedStatement1.close();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                if (preparedStatement1 != null){
                    try{
                        preparedStatement1.close();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }

        });

        button_view.setOnAction(event -> {
            tbl.getItems().clear();
            RemittanceListObservableList.clear();
            tot_rem=0.0;
            if (!(date.getValue()==null)){
                Connection connection;
                PreparedStatement fromdayend = null;
                PreparedStatement frombill = null;
                PreparedStatement fromdayend2;
                PreparedStatement fromrem=null;
                ResultSet resultSet = null,resultSet1,resultSet2,resultSet3;


                try{
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb","root","Whoiam@123");
                    fromdayend = connection.prepareStatement("SELECT*FROM dayend WHERE date=?");
                    fromdayend2= connection.prepareStatement("SELECT cih,status FROM dayend WHERE date=?");
                    frombill = connection.prepareStatement("SELECT SUM(tot_ammount) AS sum FROM bill WHERE date=?;");
                    fromrem = connection.prepareStatement("SELECT * FROM remittance WHERE date=?;");
                    fromrem.setString(1,String.valueOf(date.getValue()));
                    resultSet3=fromrem.executeQuery();

                    while (resultSet3.next()){
                        int no = Integer.parseInt(resultSet3.getNString("remittance_no"));
                        String acc = resultSet3.getNString("account_no");
                        String des = resultSet3.getNString("description");
                        double amount = resultSet3.getDouble("amount");

                        RemittanceListObservableList.add(new RemittanceList(no,acc,des,amount));

                        tbl_no.setCellValueFactory(new PropertyValueFactory<>("no"));
                        tbl_acc.setCellValueFactory(new PropertyValueFactory<>("acc"));
                        tbl_des.setCellValueFactory(new PropertyValueFactory<>("des"));
                        tbl_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

                        tot_rem=tot_rem+amount;

                        tbl.setItems(RemittanceListObservableList);
                    }


                    LocalDate Dat = date.getValue();
                    key=Dat;
                    String strDate = Dat.toString();
                    frombill.setString(1,strDate);
                    fromdayend2.setString(1,strDate);
                    String[] cal = strDate.split("-");
                    int day = Integer.parseInt(cal[2]);
                    day = day-1;
                    cal[2] = String.valueOf(day);
                    strDate = cal[0]+"-"+cal[1]+"-"+cal[2];

                    fromdayend.setString(1,strDate);

                    resultSet = fromdayend.executeQuery();
                    resultSet1 = frombill.executeQuery();
                    resultSet2 = fromdayend2.executeQuery();
                    if (resultSet1.isBeforeFirst()){
                        while (resultSet1.next()){
                            tot_sale = resultSet1.getDouble("sum");
                        }
                    }else {
                        tot_sale=0.0;
                    }
                    if(resultSet.isBeforeFirst()){
                        while (resultSet.next()){
                            prev_cih = resultSet.getDouble("cih");
                        }
                    }else {
                        prev_cih = 0.0;
                    }
                    if(resultSet2.isBeforeFirst()){
                        while (resultSet2.next()){
                            cih = resultSet2.getDouble("cih");
                            status = resultSet2.getNString("status");
                        }
                        if(status!=null){
                            System.out.println("Dayend Already Completed");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Dayend Already Completed");
                            btn_add.setVisible(false);
                            alert.show();
                            tf_cih.setText(String.valueOf(cih));
                        }else {
                            btn_add.setVisible(true);
                        }
                    }

                    lbl_prev_cih.setText(String.valueOf(prev_cih));
                    lbl_tot_sale.setText(String.valueOf(tot_sale));
                    cih = Double.parseDouble(tf_cih.getText());
                    dif = prev_cih+tot_sale-tot_rem-cih;

                    lbl_rem.setText(String.valueOf(tot_rem));
                    lbl_dif.setText(String.valueOf(dif));

                }
                catch (SQLException e){
                    e.printStackTrace();
                }
                finally {
                    if (resultSet != null){
                        try{
                            resultSet.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                    if (fromdayend != null){
                        try{
                            fromdayend.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                    if (frombill != null){
                        try{
                            frombill.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Date can not be empty..!");
                alert.show();
            }

        });

        tbl.setRowFactory(remittanceListTableView -> {
            TableRow<RemittanceList> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && status==null){
                    int index = remittanceListTableView.getSelectionModel().getFocusedIndex();
                    ButtonType yes = new ButtonType("Yes");
                    ButtonType no = new ButtonType("No");
                    Alert a = new Alert(Alert.AlertType.NONE,"Do you want to remove this..?",yes,no);
                    System.out.println(tbl.getItems().get(index).getNo());
                    a.setAlertType(Alert.AlertType.CONFIRMATION);
                    a.setResizable(false);
                    a.showAndWait().ifPresent(response -> {
                        if (response == yes) {
                            RemittanceList selectedItem = tbl.getSelectionModel().getSelectedItem();
                            RemittanceListObservableList.remove(selectedItem);
                            removeItem(String.valueOf(selectedItem.getNo()),date.getValue().toString());
                            tot_rem = tot_rem-selectedItem.getAmount();
                            lbl_rem.setText(String.valueOf(tot_rem));
                            tbl.getItems().remove(selectedItem);

                        }
                    });
                }
            });
            return row;
        });
    }
    void removeItem(String remNo,String date){
        DBUtils con = new DBUtils();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection=con.connection();
            preparedStatement = connection.prepareStatement("DELETE FROM remittance WHERE (date = ?) and (remittance_no = ?);");
            preparedStatement.setString(1,date);
            preparedStatement.setString(2,remNo);
            preparedStatement.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    connection.close();
                    alert.setContentText("Successfully Removed..!");
                    alert.show();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
