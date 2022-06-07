package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button button_sign_up;
    @FXML
    private Button button_clear;
    @FXML
    private TextField tf_emp_no,tf_name,tf_mobile,tf_add;
    @FXML
    private Label lbl_emp_no_invalid,lbl_mobile_no_invalid,lbl_mismatch,lbl_empty;
    @FXML
    private TextField tf_password,tf_password_com;

    @Override
    public void initialize(URL location, ResourceBundle resources){

        tf_mobile.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([0-9]*)?")) ? change : null));
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Connection connection;
                PreparedStatement psInsert = null;
                PreparedStatement psCheckUserExists = null;
                ResultSet resultSet = null;
                lbl_mismatch.setVisible(false);
                lbl_emp_no_invalid.setVisible(false);
                lbl_mobile_no_invalid.setVisible(false);
                lbl_empty.setVisible(false);
                String username=tf_emp_no.getText();
                String name=tf_name.getText();
                String mobile = tf_mobile.getText();
                String add = tf_add.getText();
                String password = tf_password.getText();
                String password_com = tf_password_com.getText();

                try{
                    DBUtils con = new DBUtils();
                    connection = con.connection();
                    psCheckUserExists = connection.prepareStatement("SELECT*FROM em_user WHERE emp_no= ?");
                    psCheckUserExists.setString(1,username);
                    resultSet = psCheckUserExists.executeQuery();

                    if (resultSet.isBeforeFirst()){
                        lbl_emp_no_invalid.setVisible(true);
                    } else if ((mobile.trim()).length()!=9) {
                        lbl_mobile_no_invalid.setVisible(true);
                    } else if (password.trim().isEmpty()) {
                        lbl_empty.setVisible(true);
                    } else if (!(password.equals(password_com))) {
                        lbl_mismatch.setVisible(true);
                    } else {
                        psInsert = connection.prepareStatement("INSERT INTO em_user(emp_no,u_name,phone,address,emp_password) VALUES(?,?,?,?,?)");
                        psInsert.setString(1,username);
                        psInsert.setString(2,name);
                        psInsert.setString(3,"+94"+mobile);
                        psInsert.setString(4,add);
                        psInsert.setString(5,password);

                        psInsert.executeUpdate();
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
                finally {
                    clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Registration Successful..!");
                    alert.show();
                    if (resultSet != null){
                        try{
                            resultSet.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                    if (psCheckUserExists != null){
                        try{
                            psCheckUserExists.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                    if (psInsert != null){
                        try{
                            psInsert.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        button_clear.setOnAction(actionEvent -> {
            clear();
        });
    }

    void clear(){
        tf_mobile.clear();
        tf_add.clear();
        tf_emp_no.clear();
        tf_password_com.clear();
        tf_password.clear();
        tf_name.clear();
    }

}
