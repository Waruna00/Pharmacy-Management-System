package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class DBUtils {
    public Connection connection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb", "root", "Whoiam@123");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(String.valueOf(e));
            alert.show();
        }
        return connection;
    }
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, int v, int v1){
        Parent root = null;
        if (username!= null){
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                welcome_controller welcome_controller = loader.getController();
                welcome_controller.setUserInformation(username);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            try{
                root=FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,v,v1));
        if (!(title.equals("Login"))){
            stage.setMaximized(true);
        }
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username,String password){
        Connection connection;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb", "root", "Whoiam@123");
            psCheckUserExists = connection.prepareStatement("SELECT*FROM em_user WHERE = ? ?");
            psCheckUserExists.setString(1,username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()){
                System.out.println("User already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can not use this user name");
                alert.show();
            }
            else {
                psInsert = connection.prepareStatement("INSERT INTO emp_user(emp_no,emp_password) VALUES(?, ?)");
                psInsert.setString(1,username);
                psInsert.setString(2,password);
                psInsert.executeUpdate();

                changeScene(event, "logged-in.fxml","Welcome",username,1280,800);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null){
                try{
                    resultSet.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null){
                try{
                    psCheckUserExists.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null){
                try{
                    psInsert.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }
    }

    public static void logInUser(ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmsdb", "root", "Whoiam@123");
            preparedStatement = connection.prepareStatement("SELECT emp_password FROM em_user WHERE emp_no = ?");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            }
            else {
                while (resultSet.next()){
                    String retrievedPassword = resultSet.getNString("emp_password");
                    if(retrievedPassword.equals(password)){
                        User details = new User();
                        details.setUserName(username);
                        details.setPassword(password);
                        changeScene(event,"welcome.fxml","Welcome",username,1280,800);
                    }
                    else {
                        System.out.println("Password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect");
                        alert.show();
                    }
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null){
                try{
                    resultSet.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try{
                    preparedStatement.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try{
                    connection.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
