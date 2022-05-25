package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {
    @FXML
    private Button btn_edit;

    @FXML
    private TextField tf_name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_edit.setOnAction(actionEvent -> {
            tf_name.setEditable(true);
            tf_name.setCursor(Cursor.cursor(do));
        });
    }
}
