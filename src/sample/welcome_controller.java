package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class welcome_controller implements Initializable {


    @FXML
    private MenuItem log_out;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        log_out.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"sample.fxml","Log In",null,null);
                System.out.println("You Clicked");
            }
        });
    }
    /*@FXML
    private void logout(ActionEvent event) throws IOException {

        DBUtils.changeScene(event,"sample.fxml","Log In",null,null);
    }*/

    @FXML
    private Label label_welcome;

    @FXML
    private Label label_fav_channel;

    public void setUserInformation(String username, String favChannel){
        label_welcome.setText("Welcome"+username);
        label_fav_channel.setText("Your fav yt channel is "+favChannel);
    }
}