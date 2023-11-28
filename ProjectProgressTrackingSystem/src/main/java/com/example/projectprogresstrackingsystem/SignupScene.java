package com.example.projectprogresstrackingsystem;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

public class SignupScene extends SceneController{
    public void switchToSignupScene(Stage stage, String rank, String mail){
        Pane root = new Pane();

        Font titleFont = new Font("Ramaraja",40);
        Font normalFont = new Font(30);
        Font backBtnFont = new Font(20);

        Button backBtn = new Button("<< Back");
        backBtn.setFont(backBtnFont);
        backBtn.setLayoutX(20);
        backBtn.setLayoutY(20);
        backBtn.setOnAction(backEvent -> {
            LoginSignScene backToLogin = new LoginSignScene();
            try {
                backToLogin.switchToLogSignScene(null,stage);
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
        });

        Image welcome_logo = new Image("logo.png");
        ImageView welcome_logo_view = new ImageView(welcome_logo);
        welcome_logo_view.setFitHeight(220);
        welcome_logo_view.setFitWidth(550);
        welcome_logo_view.setLayoutX(540);
        welcome_logo_view.setLayoutY(15);

        Text nameText = new Text("Name");
        nameText.setFont(titleFont);
        nameText.setLayoutX(350);
        nameText.setLayoutY(290);

        TextField inNameTextF = new TextField();
        inNameTextF.setFont(normalFont);
        inNameTextF.setLayoutX(350);
        inNameTextF.setLayoutY(310);

        Text phoneText = new Text("Phone Number");
        phoneText.setFont(titleFont);
        phoneText.setLayoutX(850);
        phoneText.setLayoutY(290);

        TextField inPhoneTextF = new TextField();
        inPhoneTextF.setFont(normalFont);
        inPhoneTextF.setLayoutX(850);
        inPhoneTextF.setLayoutY(310);

        Text newPassText = new Text("New Password");
        newPassText.setFont(titleFont);
        newPassText.setLayoutX(350);
        newPassText.setLayoutY(420);

        PasswordField newPassTextF = new PasswordField();
        newPassTextF.setFont(normalFont);
        newPassTextF.setLayoutX(350);
        newPassTextF.setLayoutY(430);

        Text conPassText = new Text("Confirm Password");
        conPassText.setFont(titleFont);
        conPassText.setLayoutX(850);
        conPassText.setLayoutY(420);

        PasswordField conPassTextF = new PasswordField();
        conPassTextF.setFont(normalFont);
        conPassTextF.setLayoutX(850);
        conPassTextF.setLayoutY(430);

        Button signUpBtn = new Button("Sign Me Up");
        signUpBtn.setFont(normalFont);
        signUpBtn.setLayoutX(690);
        signUpBtn.setLayoutY(500);
        signUpBtn.setCursor(Cursor.HAND);
        signUpBtn.setOnAction(signUpEvent ->{
            String name = inNameTextF.getText();
            String phone = inPhoneTextF.getText();
            String newPass = newPassTextF.getText();
            String conPass = conPassTextF.getText();
            if ((name!=null && phone!=null && newPass!=null && conPass!=null) || (!name.isEmpty() && !phone.isEmpty() && !newPass.isEmpty() && !conPass.isEmpty())){
                System.out.println("Printing: "+name+" "+phone+" "+newPass+" "+conPass);
                if (newPass.equals(conPass)){
                    ConnectDB fetch = new ConnectDB();
                    try (Connection con = fetch.connect()) {
                        Statement statement = con.createStatement();
                        String connectQuery = "UPDATE "+rank+"_TABLE SET password='"+newPass+"', name='"+name+"', phone='"+phone+"' WHERE email='"+mail+"'";
                        System.out.println(connectQuery);
                        int rows = 0;
                        rows = statement.executeUpdate(connectQuery);
                        if (rows!=0){
                            Alert success = new Alert(Alert.AlertType.INFORMATION);
                            success.setTitle("Success!");
                            success.setHeaderText("Sign up successful!");
                            success.show();
                            LoginSignScene loginSignScene = new LoginSignScene();
                            loginSignScene.switchToLogSignScene(null,stage);
                        }
                        else{
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error!");
                            error.setHeaderText("Couldn't sign up! Please try Again later");
                            error.show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error!");
                    error.setHeaderText("Passwords Don't Match!");
                    error.show();
                }
            }
            else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("No Fields Can be Empty!");
                error.show();
            }

        });

        Image logo = new Image("logo.png");

        root.getChildren().addAll(welcome_logo_view,backBtn,nameText,inNameTextF,phoneText,inPhoneTextF,newPassText,newPassTextF,conPassText,conPassTextF,signUpBtn);

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
