package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class Settings implements Initializable {
    @FXML
    private Button btn_edit_name,btn_edit_address,btn_edit_mobile,btn_change_password,btn_update,btn_reset_password,btn_back;
    @FXML
    private Label lbl_code,lbl_one,lbl_two;
    @FXML
    private TextField tf_name,tf_address,tf_mobile,tf_old_p,tf_new_p,tf_com_p;
    @FXML
    private TabPane tab;
    @FXML
    private Tab tab_reset_password,tab_info;
    private String username;
    private String name;
    private String mobile;
    private String address;
    private String pw;

    boolean validity = true;

    DBUtils connect = new DBUtils();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_edit_name.setOnAction(actionEvent -> {
            tf_name.setEditable(true);
            tf_name.requestFocus();
            btn_update.setVisible(true);
        });
        btn_edit_address.setOnAction(actionEvent -> {
            tf_address.setEditable(true);
            tf_address.requestFocus();
            btn_update.setVisible(true);
        });
        btn_edit_mobile.setOnAction(actionEvent -> {
            tf_mobile.setEditable(true);
            tf_mobile.requestFocus();
            btn_update.setVisible(true);
        });
        btn_update.setOnAction(actionEvent -> {
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            Alert a = new Alert(Alert.AlertType.NONE,"Are you sure..?",yes,no);
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setResizable(false);
            a.showAndWait().ifPresent(response -> {
                if (response == yes) {
                    updateInfo(tf_name.getText(), tf_mobile.getText(), tf_address.getText(), username);
                }
            });
        });
        btn_change_password.setOnAction(actionEvent -> {
            tab.getSelectionModel().select(tab_reset_password);
        });
        btn_reset_password.setOnAction(actionEvent -> {
            resetPassword();
            clear();
        });
        btn_back.setOnAction(actionEvent -> {
            tab.getSelectionModel().select(tab_info);
            clear();
        });
    }

    void clear(){
        tf_new_p.clear();
        tf_old_p.clear();
        tf_com_p.clear();
    }

    public void resetPassword() {
        String newP=tf_new_p.getText();
        String oldP=tf_old_p.getText();
        String comP=tf_com_p.getText();
        if (lbl_one.isVisible()){
            lbl_one.setVisible(false);
        }
        if (lbl_two.isVisible()){
            lbl_two.setVisible(false);
        }
        if(!Objects.equals(oldP, pw)){
            lbl_one.setVisible(true);
        }
        else if(!Objects.equals(newP, comP)){
            lbl_two.setVisible(true);
        } else if (newP.trim().isEmpty()) {
            lbl_two.setVisible(true);
        } else {
            Connection connection = null;
            PreparedStatement preparedStatement=null;
            Alert alert = new Alert(Alert.AlertType.NONE);
            try {
                connection=connect.connection();
                preparedStatement=connection.prepareStatement("UPDATE em_user SET emp_password = ? WHERE (emp_no = ?);");
                preparedStatement.setString(1,newP);
                preparedStatement.setString(2,username);
                preparedStatement.execute();

            }catch (SQLException e){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Update Failed..!");
                e.printStackTrace();
                alert.show();
            }
            finally {
                if (connection!=null){
                    try {
                        connection.close();
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("Successfully Updated..!");
                        pw=tf_new_p.getText();
                        tab.getSelectionModel().select(tab_info);
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

    public void updateInfo(String name,String phone,String address,String username){

        Connection connection=null;
        PreparedStatement statement=null;
        try {
            connection=connect.connection();
            statement=connection.prepareStatement("UPDATE em_user SET u_name =?, phone = ?, address = ? WHERE (emp_no = ?);");
            statement.setString(1,name);
            statement.setString(2,phone);
            statement.setString(3,address);
            statement.setString(4,username);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection!=null){
                btn_update.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Successfully Updated..!");
                alert.show();
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (statement != null){
                try{
                    statement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    String RetrieveData(String d_code){
        return  "h";
    }
    public void setUserInformation(String username){
        this.username=username;
        lbl_code.setText(" "+username);
    }
    public void setName(String name) {
        this.name = name;
        tf_name.setText(name);
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
        tf_mobile.setText(mobile);
    }
    public void setAddress(String address) {
        this.address = address;
        tf_address.setText(address);
    }
    public void setPw(String pw) {
        this.pw=pw;
    }
}
