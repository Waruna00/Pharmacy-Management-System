package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class day_end_controller implements Initializable {

    Double prev_cih,tot_sale,cih,dif=0.0;
    Double tot_rem =0.0;
    LocalDate key;

    //Date Dat;

    @FXML
    private Button button_add,button_clear,button_view,button_pro;

    @FXML
    private TextField tf_cih;

    @FXML
    private DatePicker date;

    @FXML
    private Label lbl_prev_cih,lbl_tot_sale,lbl_dif;

    public void cihToZero(){
        tf_cih.setText("00.0");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

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
                            preparedStatement4.setString(4,strDate);
                            preparedStatement4.setString(5,String.valueOf(dif));
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
            //x=1;
            Connection connection;
            PreparedStatement fromdayend = null;
            PreparedStatement frombill = null;
            PreparedStatement fromdayend2;
            ResultSet resultSet = null,resultSet1,resultSet2;
            String status=null;

            try{
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb","root","Whoiam@123");
                fromdayend = connection.prepareStatement("SELECT*FROM dayend WHERE date=?");
                fromdayend2= connection.prepareStatement("SELECT cih,status FROM dayend WHERE date=?");
                frombill = connection.prepareStatement("SELECT SUM(tot_ammount) AS sum FROM bill WHERE date=?;");

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
                        alert.show();
                        tf_cih.setText(String.valueOf(cih));
                    }
                }

                lbl_prev_cih.setText(String.valueOf(prev_cih));
                lbl_tot_sale.setText(String.valueOf(tot_sale));
                cih = Double.parseDouble(tf_cih.getText());
                dif = prev_cih+tot_sale-tot_rem-cih;

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
        });
    }
}
