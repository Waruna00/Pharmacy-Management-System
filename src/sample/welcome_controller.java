package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class welcome_controller implements Initializable {
    String username=null;

    @FXML
    private MenuItem MenuRSale;
    @FXML
    private MenuItem MenuWSale;
    @FXML
    private MenuItem MenuIn;
    @FXML
    private MenuItem MenuOut;
    @FXML
    private MenuItem MenuTracker;
    @FXML
    private MenuItem MenuDayend;
    @FXML
    private MenuItem MenuRepSale;
    @FXML
    private MenuItem MenuRepInventory;
    @FXML
    private MenuItem MenuRepFinance;
    @FXML
    private MenuItem MenuSettings;
    @FXML
    private MenuItem MenuQA;
    @FXML
    private MenuItem MenuContact;
    @FXML
    private MenuItem MenuAbout;
    @FXML
    private MenuItem MenuReg;

    FXMLLoader loader=null;

    public void NewWindow(String fxmlFile,String title){
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        newWindow.setResizable(false);

        //Create view from FXML
        loader = new FXMLLoader(getClass().getResource(fxmlFile));

        try{
            //Parent root = loader.load();

        }catch (Exception e){
            System.out.println(e);
        }
        //Set view in window
        try {
            newWindow.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Launch
        newWindow.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        MenuRSale.setOnAction(event -> {
            NewWindow("sales.fxml","Retail Sales");
        });

        MenuWSale.setOnAction(event -> {
            NewWindow("sales.fxml","Retail Sales");
        });

        MenuIn.setOnAction(event -> {
            NewWindow("inward.fxml","Inward");
        });

        MenuOut.setOnAction(event -> {
            NewWindow("outward.fxml","Outward");
        });


        MenuTracker.setOnAction(event -> {
            NewWindow("search.fxml","Tracker");
        });

        MenuDayend.setOnAction(event -> {
            NewWindow("day-end.fxml","Retail Sales");
        });

        MenuRepSale.setOnAction(event -> {
            //NewWindow("");
        });

        MenuRepInventory.setOnAction(event -> {
            //NewWindow("");
        });

        MenuRepFinance.setOnAction(event -> {
            //NewWindow("");
        });
        MenuReg.setOnAction(event -> {
            NewWindow("sign-up.fxml","Registration");
        });

        MenuSettings.setOnAction(event -> {

            DBUtils connect = new DBUtils();
            NewWindow("settings.fxml","Settings");
            Connection connection=null;
            String name=null;
            String mobile=null;
            String address=null;
            String pw=null;
            try {
                connection = connect.connection();
                PreparedStatement statement=connection.prepareStatement("SELECT u_name,phone,address,emp_password FROM em_user WHERE emp_no=?;");
                statement.setString(1,username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    name=resultSet.getNString("u_name");
                    mobile=resultSet.getNString("phone");
                    address=resultSet.getNString("address");
                    pw=resultSet.getNString("emp_password");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                if (connection!=null){
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            Settings s = loader.getController();
            s.setUserInformation(username);
            s.setName(name);
            s.setAddress(address);
            s.setMobile(mobile);
            s.setPw(pw);

        });

        MenuQA.setOnAction(event -> {
            //NewWindow("");
        });

        MenuContact.setOnAction(event -> {
            //NewWindow("");
        });

        MenuAbout.setOnAction(event -> {
            //NewWindow("");
        });
    }

    public void logout(ActionEvent event){
        //DBUtils.changeScene(event,"sample.fxml","Login",null,600,400);
        System.exit(1);
    }

    @FXML
    private Label label_welcome,lbl_time;

    public void setUserInformation(String username){
        this.username=username;
        label_welcome.setText(username);
        LocalTime time = java.time.LocalTime.now();
        String FormattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lbl_time.setText("Login : "+FormattedTime);
    }

}