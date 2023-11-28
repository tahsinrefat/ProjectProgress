package com.example.projectprogresstrackingsystem;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

public class LoginSignScene extends SceneController{
    @Override
    public void switchToLogSignScene(ActionEvent event, Stage stage) throws CustomException{
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
        setRank.setCursor(Cursor.HAND);

        Button login = new Button("Login");
        login.setLayoutX(250);
        login.setLayoutY(570);
        login.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");
        login.setCursor(Cursor.HAND);
        login.setOnAction(loginEvent -> {
            String inEmail = emailField.getText();
            String inPassword = passwordField.getText();
            String rankSet = setRank.getValue();
            if (inEmail==null || inPassword==null || rankSet.equals("--Select a Rank--")){
//                Alert error = new Alert(Alert.AlertType.ERROR);
//                error.setTitle("Error!");
//                error.setHeaderText("At least One of the Inputs Are Invalid!");
//                error.show();
                try {
//                    System.out.println("here I print also");
                    throw new CustomException("invalidInput");
                } catch (CustomException e) {
//                    System.out.println("here I printed");
                    throw new RuntimeException(e);
                }
            }
            else{
                System.out.println(inEmail+" "+ inPassword+ " "+ rankSet);
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
//                                System.out.println("Welcome "+data.getString("email")+"!"+" Rank is "+rankSet);
                                if (rankSet.equals("HR")){
                                    new HRLoginScene().switchToHRLoginScene(stage,data.getString("email"),data.getString("name"),data.getString("phone"),rankSet);
                                    return;
                                } else if (rankSet.equals("Teamlead")) {
                                    new TeamleadLoginScene().switchToTeamleadLoginScene(stage,data.getString("email"),data.getString("name"),data.getString("phone"),rankSet,data.getString("project"));
                                    return;
                                } else if (rankSet.equals("Developer") || rankSet.equals("SQA")) {
                                    String feature="";
                                    String dev_msg="";
                                    String sqa_msg="";
                                    String status="";
                                    String featureQuery = "";
                                    if (rankSet.equals("Developer")){
                                        featureQuery = "SELECT feature,Dev_comment,Sqa_comment,status FROM Project_TABLE WHERE dev_mail='"+data.getString("email")+"'";
                                    }
                                    else{
                                        featureQuery = "SELECT feature,Dev_comment,Sqa_comment,status FROM Project_TABLE WHERE sqa_mail='"+data.getString("email")+"'";
                                    }
                                    Statement featureStatement = con.createStatement();
                                    ResultSet featureData = featureStatement.executeQuery(featureQuery);
                                    while (featureData.next()){
                                        feature = featureData.getString("feature");
                                        dev_msg = featureData.getString("Dev_comment");
                                        sqa_msg = featureData.getString("Sqa_comment");
                                        status = featureData.getString("status");
                                    }
                                    new DevSqaLoginScene().switchToDevSqaLoginScene(stage,data.getString("email"),data.getString("name"),data.getString("phone"),rankSet,data.getString("project"),feature,dev_msg,sqa_msg,status);
                                    return;
                                }
                            }
                        }
                    }
//                    Alert error = new Alert(Alert.AlertType.ERROR);
//                    error.setTitle("Error!");
//                    error.setHeaderText("Wrong Credentials Provided!");
//                    error.show();
                    throw new CustomException("wrongCreds");
                } catch (Exception e){
                    try {
                        throw new CustomException("db_connect");
                    } catch (CustomException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        Button forgotPass = new Button("Forgot Password?");
        forgotPass.setLayoutX(350);
        forgotPass.setLayoutY(570);
        forgotPass.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");
        forgotPass.setCursor(Cursor.HAND);
        forgotPass.setOnAction((forgotPassEvent) -> {
            final ArrayList<String> OTP = forForgotPass(forgotPass);
            if (OTP != null) {
                final boolean change = OTPMatch(OTP.get(0),forgotPass);
                if (change){
                    System.out.println("Will Change");
                    ChangePassword changePassword = new ChangePassword();
                    changePassword.switchToChangePass(stage,OTP.get(1), OTP.get(2));
                }else {
                    System.out.println("Won't Change");
                }
            }
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
        setRankSign.getItems().addAll("--Select a Rank--","HR", "Teamlead", "Developer", "SQA");
        setRankSign.setValue("--Select a Rank--");
        setRankSign.setLayoutX(1175);
        setRankSign.setLayoutY(480);
        setRankSign.setStyle("-fx-font-size: 15 px;-fx-font-weight: bold;");
        setRankSign.setCursor(Cursor.HAND);

        Button signUp = new Button("Sign Up");
        signUp.setLayoutX(1125);
        signUp.setLayoutY(520);
        signUp.setStyle("-fx-font-size: 25;-fx-font-weight: bold;");
        signUp.setCursor(Cursor.HAND);
        signUp.setOnAction(signUpEvent-> {
            String signMail = appEmailTextF.getText();
            String rank = setRankSign.getValue();
            String connectQuery = "SELECT email FROM "+rank+"_TABLE WHERE password='N/A'";
            ConnectDB fetch = new ConnectDB();
            Statement statement;
            try (Connection con = fetch.connect()) {
                statement = con.createStatement();
                ResultSet data = statement.executeQuery(connectQuery);
                while (data.next()){
                    SendMail sendMail = new SendMail();
                    Session newSession = sendMail.setupServerProperties();
                    String subject = "Signup on Project Progress Tracking System";
                    int OTP = sendMail.draftEmail(newSession, signMail, subject);
                    String otp = Integer.toString(OTP);
                    boolean result = OTPMatch(otp,signUp);
                    if (result){
                        System.out.println("Will Change!");
                        SignupScene signupScene = new SignupScene();
                        signupScene.switchToSignupScene(stage,rank,signMail);
                        return;
                    }else {
                        System.out.println("Won't Change!");
                    }

                }
//                Alert error = new Alert(Alert.AlertType.ERROR);
//                error.setTitle("Error!");
//                error.setHeaderText("Your Email Ain't in Database for Sign Up!");
//                error.show();
                throw new CustomException("noMail");

            }catch (Exception e){
//                e.printStackTrace();
                try {
                    throw new CustomException("db_connect");
                } catch (CustomException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


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

    private boolean OTPMatch(String OTP, Button forgotPass) {
        TextInputDialog forOTPMatch = new TextInputDialog();
        forOTPMatch.setTitle("OTP");
        forOTPMatch.setHeaderText("Enter Your OTP");
        forOTPMatch.setContentText("OTP: ");

        forOTPMatch.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        forOTPMatch.initModality(Modality.APPLICATION_MODAL);
        Optional<String> result = forOTPMatch.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            String curOTP = result.get();
            if (!OTP.equals(curOTP)){
                Alert noMail = new Alert(Alert.AlertType.ERROR);
                noMail.setTitle("Error!");
                noMail.setHeaderText("Please Provide the Correct OTP Next Time!");
                noMail.showAndWait();
                forOTPMatch.close();
                forgotPass.fire();
                return false;
            }else{
                return true;
            }
        } else if (!result.isPresent()) {
            forOTPMatch.close();
            return false;
        } else{
            Alert noMail = new Alert(Alert.AlertType.ERROR);
            noMail.setTitle("Error!");
            noMail.setHeaderText("Please Provide the Correct OTP Next Time!");
            noMail.showAndWait();
            forOTPMatch.close();
            forgotPass.fire();
            return false;
        }
    }

    public ArrayList<String> forForgotPass(Button forgotPass){
        ArrayList<String> returnable = new ArrayList<>();
        TextInputDialog forForgotPass = new TextInputDialog();
        forForgotPass.setTitle("Forgot Password");
        forForgotPass.setHeaderText("Enter Your Email");
        forForgotPass.setContentText("Email: ");

        forForgotPass.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        forForgotPass.initModality(Modality.APPLICATION_MODAL);
        Optional<String> result = forForgotPass.showAndWait();
        int OTP = 0;
        if (result.isPresent() && !result.get().isEmpty()) {
            String mail = result.get();
            ConnectDB checkMail = new ConnectDB();
            try (Connection con = checkMail.connect()) {
                Statement statement = con.createStatement();
                String[] tables = {"HR", "Developer", "SQA", "Teamlead"};
                for (int i = 0; i < 4; i++) {
                    String connectQuery = "SELECT * FROM "+tables[i]+"_TABLE";
                    ResultSet data = statement.executeQuery(connectQuery);
                    while (data.next()){
                        if (data.getString("email").equals(mail)){
                            String subject = "Password Reset";
                            SendMail sendMail = new SendMail();
                            Session newSession = sendMail.setupServerProperties();
                            try {
                                OTP = sendMail.draftEmail(newSession, mail, subject);
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                            returnable.add(Integer.toString(OTP));
                            returnable.add(mail);
                            returnable.add(tables[i]);
                            return returnable;
                        }
                    }
//                    System.out.println(connectQuery);
                }
                Alert noMail = new Alert(Alert.AlertType.ERROR);
                noMail.setTitle("Error!");
                noMail.setHeaderText("Please Provide a Valid Email!");
                noMail.showAndWait();
                forgotPass.fire();
                return returnable;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (!result.isPresent()) {
            forForgotPass.close();
        } else{
            Alert noMail = new Alert(Alert.AlertType.ERROR);
            noMail.setTitle("Error!");
            noMail.setHeaderText("Please Provide a Valid Email!");
            noMail.showAndWait();
            forgotPass.fire();
        }
        return returnable;
    }

}
