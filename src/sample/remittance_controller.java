package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.tools.Platform;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class remittance_controller implements Initializable {
    @FXML
    TextField tf_acc,tf_des,tf_amount;
    @FXML
    Button btn_clear,btn_pro;
    String acc=null;
    String des=null;
    Double amount=0.0;

    public void setDate(LocalDate date) {
        this.date = date;
    }

    LocalDate date=null;
    Alert a = new Alert(Alert.AlertType.NONE);
    @Override
    public void initialize(URL location, ResourceBundle resources){
        tf_acc.requestFocus();
        btn_pro.setOnAction(actionEvent -> {


            acc=tf_acc.getText();
            des=tf_des.getText();
            amount=Double.parseDouble(tf_amount.getText());
            if (acc.trim().isEmpty() || des.trim().isEmpty()){
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Account number and Description can not be empty");
                a.show();
            }
            else if (amount==0){
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Amount can not be zero");
                a.show();
            }
            else {
                sentData();
                final Node source = (Node) actionEvent.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });

        btn_clear.setOnAction(actionEvent  -> {
            clear();
        });
    }

    void clear(){
        tf_acc.clear();
        tf_amount.clear();
        tf_des.clear();
        acc=null;
        amount=0.0;
        des=null;
    }

    void getNo(){

    }
    void sentData(){
        DBUtils con = new DBUtils();
        Connection connection=null;
        PreparedStatement statement=null;
        PreparedStatement statement2=null;
        ResultSet resultSet=null;
        int no=0;
        try {
            connection= con.connection();
            statement = connection.prepareStatement("INSERT INTO remittance (`date`, `remittance_no`, `description`, `amount`, `account_no`) VALUES (?,?,?,?,?);");
            statement2 = connection.prepareStatement("SELECT max(remittance_no) AS oldNo FROM remittance WHERE date=?;");
            statement2.setString(1,String.valueOf(date));
            resultSet = statement2.executeQuery();
            while (resultSet.next()){
                no = resultSet.getInt("oldNo")+1;
            }
            statement.setString(1,String.valueOf(date));
            statement.setString(2,String.valueOf(no));
            statement.setString(3,des);
            statement.setDouble(4,amount);
            statement.setString(5,acc);
            System.out.println(no);
            statement.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("Successfully Updated..!");
                    a.show();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (statement!=null){
                try {
                    statement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (statement2!=null){
                try {
                    statement2.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (resultSet!=null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

}
