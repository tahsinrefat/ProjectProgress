package com.example.projectprogresstrackingsystem;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Objects;

public class LoginSignScene extends SceneController{
    @Override
    public void switchToLogSignScene(ActionEvent event, Stage stage) {
        Pane root = new Pane();

        Image welcome_logo = new Image("logo.png");
        ImageView welcome_logo_view = new ImageView(welcome_logo);
        welcome_logo_view.setFitHeight(220);
        welcome_logo_view.setFitWidth(550);
        welcome_logo_view.setLayoutX(540);
        welcome_logo_view.setLayoutY(15);

        Text loginText = new Text("Login");
        Font font = Font.font("Ramaraja",50);
        loginText.setFont(font);
        loginText.setLayoutX(365);
        loginText.setLayoutY(300);

        Font titleFont = new Font("Ramaraja",30);
        Font normalFont = new Font(25);

        Text email = new Text("Email");
        email.setLayoutX(250);
        email.setLayoutY(350);
        email.setFont(titleFont);

        TextField emailField = new TextField();
        emailField.setLayoutX(250);
        emailField.setLayoutY(370);
        emailField.setFont(normalFont);

        Text passwordText = new Text("Paswword");
        passwordText.setLayoutX(250);
        passwordText.setLayoutY(450);
        passwordText.setFont(titleFont);

        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(250);
        passwordField.setLayoutY(470);
        passwordField.setPrefHeight(45);
        passwordField.setPrefWidth(328);

        Text rankText = new Text("Login As: ");
        rankText.setLayoutX(250);
        rankText.setLayoutY(550);
        rankText.setFont(titleFont);

        ComboBox<String> setRank = new ComboBox<>();
        setRank.getItems().addAll("--Select a Rank--","HR", "Teamlead", "Developer", "SQA");
        setRank.setValue("--Select a Rank--");
        setRank.setLayoutX(370);
        setRank.setLayoutY(530);
        setRank.setStyle("-fx-font-size: 15 px;-fx-font-weight: bold;");

        Button login = new Button("Login");
        login.setLayoutX(250);
        login.setLayoutY(570);
        login.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");
        login.setOnAction(loginEvent -> {
            String inEmail = emailField.getText();
            String inPassword = passwordField.getText();
            String rankSet = setRank.getValue();
            if (inEmail==null || inPassword==null || Objects.equals(rankSet, "--Select a Rank--")){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("At least One of the Inputs Are Invalid!");
                error.show();
            }
            else{
//                System.out.println(inEmail+" "+ inPassword+ " "+ rankSet);
                String connectQuery = "SELECT * FROM "+rankSet+"_TABLE";
//                System.out.println(connectQuery);
                ConnectDB fetch = new ConnectDB();
                try (Connection con = fetch.connect()) {
                    Statement statement = con.createStatement();
                    ResultSet data = statement.executeQuery(connectQuery);
                    while (data.next()){
//                        System.out.println(data.getString("email")+" "+data.getString("password"));
                        if (!data.getString("password").equals("N/A")){
                            if (inEmail.equals(data.getString("email")) && inPassword.equals(data.getString("password"))){
                                System.out.println("Welcome "+data.getString("email")+"!");
                            }
                            else{
                                Alert error = new Alert(Alert.AlertType.ERROR);
                                error.setTitle("Error!");
                                error.setHeaderText("Wrong Credentials Provided!");
                                error.show();
                            }
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button forgotPass = new Button("Forgot Password?");
        forgotPass.setLayoutX(350);
        forgotPass.setLayoutY(570);
        forgotPass.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");
        forgotPass.setOnAction(forgotPassEvent -> {
            Dialog<String>forForgotPass = new Dialog<>();
            forForgotPass.setTitle("Forgot Password");
            forForgotPass.setHeaderText("Please Enter Your Email");
            ButtonType okButton = new ButtonType("OK");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            forForgotPass.getDialogPane().getButtonTypes().addAll();
            TextField forEmail = new TextField();
            forForgotPass.getDialogPane().setContent(new Pane());
            forForgotPass.getDialogPane().setExpandableContent(forEmail);
            String result = forForgotPass.showAndWait().orElse("Nothing Entered");
        });

        Line upperLine = new Line(820,240,820,420);

        Text orText = new Text("OR");
        orText.setFont(font);
        orText.setLayoutX(790);
        orText.setLayoutY(460);

        Line lowerLine = new Line(820,470,820,650);

        Text signUpText = new Text("Sign Up");
        signUpText.setFont(font);
        signUpText.setLayoutX(1125);
        signUpText.setLayoutY(350);

        Text appEmailText = new Text("Email You Applied With");
        appEmailText.setLayoutX(1040);
        appEmailText.setLayoutY(400);
        appEmailText.setFont(titleFont);

        TextField appEmailTextF = new TextField();
        appEmailTextF.setLayoutX(1040);
        appEmailTextF.setLayoutY(420);
        appEmailTextF.setFont(normalFont);

        Text rankTextSign = new Text("Sign Up As: ");
        rankTextSign.setFont(titleFont);
        rankTextSign.setLayoutX(1040);
        rankTextSign.setLayoutY(500);

        ComboBox<String> setRankSign = new ComboBox<>();
        setRankSign.getItems().addAll("--Select a Rank--","HR", "Developer", "SQA");
        setRankSign.setValue("--Select a Rank--");
        setRankSign.setLayoutX(1175);
        setRankSign.setLayoutY(480);
        setRankSign.setStyle("-fx-font-size: 15 px;-fx-font-weight: bold;");

        Button signUp = new Button("Sign Up");
        signUp.setLayoutX(1125);
        signUp.setLayoutY(520);
        signUp.setStyle("-fx-font-size: 25;-fx-font-weight: bold;");



        Image logo = new Image("logo.png");

        root.getChildren().addAll(loginText,email,emailField, passwordText, passwordField, rankText, setRank, login, welcome_logo_view, upperLine, orText, lowerLine, signUpText, appEmailText, appEmailTextF, signUp,rankTextSign, setRankSign, forgotPass);

        Scene scene = new Scene(root);

        stage.getIcons().add(logo);
        stage.setTitle("Project Progress Tracker");
        stage.setScene(scene);
        stage.setHeight(900);
        stage.setWidth(1600);
        stage.setResizable(false);
        stage.show();
    }
    public void forgotPassAlert(){

    }

}
