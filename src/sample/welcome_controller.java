package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

public class welcome_controller{


    @FXML
    private MenuItem log_out;
    @FXML
    private void logout(ActionEvent event){
        System.out.println("You Clicked");
        //Parent fxml = FXMLLoader.load(getClass().getResource("sample.fxml"));

        DBUtils.changeScene(event,"sample.fxml","Log In",null,null);
    }


    @FXML
    private Label label_welcome;

    @FXML
    private Label label_fav_channel;

    public void setUserInformation(String username, String favChannel){
        label_welcome.setText("Welcome"+username);
        label_fav_channel.setText("Your fav yt channel is "+favChannel);
    }
}