package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;



public class welcome_controller {
    String username=null;

    public void logout(ActionEvent event){
        DBUtils.changeScene(event,"sample.fxml","Login",null,600,400);
    }
    public void ToDay(ActionEvent event){
        DBUtils.changeScene(event,"day-end.fxml","Day End",null,1280,800);
    }
    public void ToSales(ActionEvent event){
        DBUtils.changeScene(event,"sales.fxml","Sales",null,1280,800);
    }
    public void ToSettings(ActionEvent event){
        DBUtils.changeScene(event,"settings.fxml","Settings",null,1280,800);
    }
    public void ToTracker(ActionEvent event){
        DBUtils.changeScene(event,"tracker.fxml","Inventory Tracker",null,1280,800);
    }

    @FXML
    private Label label_welcome;

    public void setUserInformation(String username){
        this.username=username;
        label_welcome.setText("Welcome"+username);
    }

}