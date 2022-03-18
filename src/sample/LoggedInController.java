package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_logout;

    @FXML
    private Label label_welcome;

    @FXML
    private Label label_fav_channel;

    @Override
    public void initialize (URL location, ResourceBundle resources){

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"sample.fxml","Log In",null,null);
            }
        });
    }

    public void setUserInformation(String username, String favChannel){
        label_welcome.setText("Welcome"+username);
        label_fav_channel.setText("Your fav yt channel is "+favChannel);
    }
}