package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class day_end_controller implements Initializable {

    Double prev_cih,tot_sale,cih,dif;
    Double tot_rem =0.0;
    //Date Dat;

    @FXML
    private Button button_add,button_clear,button_view,button_pro;

    @FXML
    private TextField tf_cih;

    @FXML
    private DatePicker date;

    @FXML
    private Label lbl_prev_cih,lbl_tot_sale,lbl_dif;


    @Override
    public void initialize(URL location, ResourceBundle resources){

        button_pro.setOnAction(event -> {

        });

        button_view.setOnAction(event -> {
            Connection connection;
            PreparedStatement fromdayend = null;
            PreparedStatement frombill = null;
            ResultSet resultSet = null;
            ResultSet resultSet1;

            try{
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb","root","Whoiam@123");
                fromdayend = connection.prepareStatement("SELECT*FROM dayend WHERE date=?");
                frombill = connection.prepareStatement("SELECT SUM(tot_ammount) AS sum FROM bill WHERE date=?;");

                LocalDate Dat = date.getValue();
                String strDate = Dat.toString();
                frombill.setString(1,strDate);
                String[] cal = strDate.split("-");
                int day = Integer.parseInt(cal[2]);
                day = day-1;
                cal[2] = String.valueOf(day);
                strDate = cal[0]+"-"+cal[1]+"-"+cal[2];

                fromdayend.setString(1,strDate);

                resultSet = fromdayend.executeQuery();
                resultSet1 = frombill.executeQuery();
                while (resultSet1.next()){
                    tot_sale = resultSet1.getDouble("sum");
                }

                if(!resultSet.isBeforeFirst()){
                    System.out.println("Date not found in the database");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Provided Date is not correct");
                    alert.show();
                }
                else {
                    while (resultSet.next()){
                        prev_cih = resultSet.getDouble("cih");
                    }
                    lbl_prev_cih.setText(String.valueOf(prev_cih));
                    lbl_tot_sale.setText(String.valueOf(tot_sale));
                    cih = Double.parseDouble(tf_cih.getText());
                    dif = prev_cih+tot_sale-tot_rem-cih;
                    lbl_dif.setText(String.valueOf(dif));

                }
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
