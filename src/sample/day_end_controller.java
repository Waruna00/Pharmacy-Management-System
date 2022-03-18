package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class day_end_controller implements Initializable {
    @FXML
    private Button button_add,button_clear,button_view,button_pro;

    @FXML
    private TextField tf_to_be_bank,tf_prv_cih,tf_tot_sale,tf_tot_rem,tf_cih,tf_def;

    @FXML
    private DatePicker date;

    @Override
    public void initialize(URL location, ResourceBundle resources){

        button_view.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //DBUtils.logInUser(event,tf_username.getText(),tf_password.getText());
            }
        });

        button_pro.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"sign-up.fxml","Sign-up",null,null);
            }
        });
    }
}
