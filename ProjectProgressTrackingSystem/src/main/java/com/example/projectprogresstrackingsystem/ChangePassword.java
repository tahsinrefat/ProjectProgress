package com.example.projectprogresstrackingsystem;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

public class ChangePassword extends SceneController{
    public void switchToChangePass(Stage stage, String mail, String table){
        Pane root = new Pane();

        Font titleFont = new Font("Ramaraja",40);
        Font anTitleFont = new Font("Ramaraja", 35);
        Font normalFont = new Font(30);
        Font backBtnFont = new Font(20);

        Button backBtn = new Button("<< Back");
        backBtn.setFont(backBtnFont);
        backBtn.setLayoutX(20);
        backBtn.setLayoutY(20);
        backBtn.setCursor(Cursor.HAND);
        backBtn.setOnAction(backEvent -> {
            LoginSignScene backToLogin = new LoginSignScene();
            backToLogin.switchToLogSignScene(null,stage);
        });

        Image welcome_logo = new Image("logo.png");
        ImageView welcome_logo_view = new ImageView(welcome_logo);
        welcome_logo_view.setFitHeight(220);
        welcome_logo_view.setFitWidth(550);
        welcome_logo_view.setLayoutX(540);
        welcome_logo_view.setLayoutY(15);

        Text console = new Text("Changing password is a hectic process we know. Don't worry! This is the last step.");
        console.setFont(titleFont);
        console.setLayoutX(210);
        console.setLayoutY(260);

        Text newPass = new Text("New Password");
        newPass.setFont(anTitleFont);
        newPass.setLayoutX(605);
        newPass.setLayoutY(300);

        PasswordField inNewPass = new PasswordField();
        inNewPass.setFont(normalFont);
        inNewPass.setLayoutX(605);
        inNewPass.setLayoutY(320);

        Text conPass = new Text("Confirm Password");
        conPass.setFont(anTitleFont);
        conPass.setLayoutX(605);
        conPass.setLayoutY(412);

        PasswordField inConPass = new PasswordField();
        inConPass.setFont(normalFont);
        inConPass.setLayoutX(605);
        inConPass.setLayoutY(425);

        Button confirmPassBtn = new Button("Confirm Password");
        confirmPassBtn.setFont(normalFont);
        confirmPassBtn.setLayoutX(647.5);
        confirmPassBtn.setLayoutY(500);
        confirmPassBtn.setCursor(Cursor.HAND);
        confirmPassBtn.setOnAction(confirmPassEvent -> {
            String newInPass = inNewPass.getText();
            String conInPass = inConPass.getText();
            if ((newInPass!=null && conInPass!=null) && newInPass.equals(conInPass)) {
                ConnectDB cdb = new ConnectDB();
                try (Connection connected = cdb.connect()) {
                    Statement statement = connected.createStatement();
                    String connectQuery = "UPDATE "+table+"_TABLE SET password='"+newInPass+"' WHERE email='"+mail+"'";
//                    System.out.println(connectQuery);
                    int rows = 0;
                    rows = statement.executeUpdate(connectQuery);
                    if (rows!=0){
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success!");
                        success.setHeaderText("Password changed successfully!");
                        success.show();
                    }
                    else{
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error!");
                        error.setHeaderText("Couldn't change passwords!");
                        error.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (!newInPass.equals(conInPass)) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Passwords doesn't match!");
                error.show();
            } else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Fields cannot be empty!");
                error.show();
            }
        });

        Image logo = new Image("logo.png");

        root.getChildren().addAll(welcome_logo_view,console,newPass,inNewPass,conPass,inConPass,confirmPassBtn,backBtn);

        Scene scene = new Scene(root);

        stage.getIcons().add(logo);
        stage.setTitle("Project Progress Tracker");
        stage.setScene(scene);
        stage.setHeight(900);
        stage.setWidth(1600);
        stage.setResizable(false);
        stage.show();
    }
}
