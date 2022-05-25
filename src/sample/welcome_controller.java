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
    private MenuItem MenuLogout;
    @FXML
    private MenuItem MenuQA;
    @FXML
    private MenuItem MenuContact;
    @FXML
    private MenuItem MenuAbout;

    public void NewWindow(String fxmlFile,String title){
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        newWindow.setResizable(false);

        //Create view from FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));

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
            //System.out.println("Menu Item 1 Selected");
            NewWindow("sales.fxml","Retail Sales");
        });

        MenuWSale.setOnAction(event -> {
            //System.out.println("Menu Item 1 Selected");
            NewWindow("sales.fxml","Retail Sales");
        });

        MenuIn.setOnAction(event -> {
            NewWindow("inward.fxml","Inward");
        });

        MenuOut.setOnAction(event -> {
            NewWindow("tracker.fxml","Outward");
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

        MenuSettings.setOnAction(event -> {
            NewWindow("settings.fxml","Settings");
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
        DBUtils.changeScene(event,"sample.fxml","Login",null,600,400);
    }

    @FXML
    private Label label_welcome;

    public void setUserInformation(String username){
        this.username=username;
        label_welcome.setText("Welcome "+username);
    }

}