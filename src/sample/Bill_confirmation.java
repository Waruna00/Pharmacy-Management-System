package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Bill_confirmation implements Initializable {
    @FXML
    private Label label_tot;
    @FXML
    private TextField tf_cash;
    @FXML
    private Button btn_con;

    public void setTotal(double total) {
        Total = total;
        tf_cash.setText("Hello");
        label_tot.setText("Hello0 " + total);
        System.out.println("Done 2 "+total +label_tot.getText());
    }

    private double Total=00.0;
    private double Change=00.0;
    private double Cash=00.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_con.setOnAction(event ->{

            Cash=Double.parseDouble(tf_cash.getText());
            Change = Total-Cash;
            tf_cash.setText("hi");

        });
    }

}
