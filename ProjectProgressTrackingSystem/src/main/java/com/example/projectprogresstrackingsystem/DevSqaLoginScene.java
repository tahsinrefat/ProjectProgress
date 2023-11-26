package com.example.projectprogresstrackingsystem;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

public class DevSqaLoginScene extends SceneController{
    @Override
    public void switchToDevSqaLoginScene(Stage stage, String mail, String name, String phone, String rank, String project, String feature, String devMsg, String sqaMsg, String status) {
        Pane root = new Pane();

        Font titleFont = new Font("Ramaraja", 70);
        Font anTitleFont = new Font("Ramaraja", 30);
        Font smallerTitleFont = new Font("Ramaraja", 25);
        Font normalFont = new Font(20);
        Font logOutBtnFont = new Font(20);

        Button logOutBtn = new Button("Logout");
        logOutBtn.setFont(logOutBtnFont);
        logOutBtn.setLayoutX(1460);
        logOutBtn.setLayoutY(20);
        logOutBtn.setStyle("-fx-background-color: red;-fx-text-fill: white;");
        logOutBtn.setCursor(Cursor.HAND);
        logOutBtn.setOnAction(logOutEvent -> {
            LoginSignScene backToLogin = new LoginSignScene();
            backToLogin.switchToLogSignScene(null, stage);
        });

        Text detailText = new Text("Ongoing Current Projects");
        detailText.setFont(titleFont);
        detailText.setLayoutX(495);
        detailText.setLayoutY(90);

        Text nameText = new Text(name);
        nameText.setFont(anTitleFont);
        nameText.setLayoutX(20);
        nameText.setLayoutY(165);

        Text rankText = new Text("("+rank+")");
        rankText.setFont(smallerTitleFont);
        rankText.setLayoutX(22+nameText.getLayoutBounds().getWidth());
        rankText.setLayoutY(165);

        Text mailText = new Text(mail);
        mailText.setFont(smallerTitleFont);
        mailText.setLayoutX(20);
        mailText.setLayoutY(190);

        Text phoneText = new Text(phone);
        phoneText.setFont(smallerTitleFont);
        phoneText.setLayoutX(20);
        phoneText.setLayoutY(215);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setFont(normalFont);
        refreshBtn.setCursor(Cursor.HAND);
        refreshBtn.setLayoutX(1480);
        refreshBtn.setLayoutY(175);
        refreshBtn.setOnAction(refreshEvent -> {
            new DevSqaLoginScene().switchToDevSqaLoginScene(stage,mail, name, phone, rank, project, feature, devMsg, sqaMsg, status);
        });

        Button objectionBtn = new Button("Object on the current feature");
        objectionBtn.setFont(normalFont);
        objectionBtn.setLayoutX(1150);
        objectionBtn.setLayoutY(175);

        String projectAndFeature = "";
        int xCorForProjAFeatTxt = 0;
        boolean addCommentArea = false;
        boolean dev = false;
        if (project==null || project.isEmpty()){
            projectAndFeature = "You are currently assigned to no project. Please wait while you are assigned to one!";
            xCorForProjAFeatTxt = 340;
        }
        else {
            if (rank.equals("SQA")){
                xCorForProjAFeatTxt = 130;
                projectAndFeature = "You are currently assigned to "+project+". Please complete your task of testing "+feature+" and provide necessary comment below:";
            }
            else{
                dev = true;
                xCorForProjAFeatTxt = 100;
                projectAndFeature = "You are currently assigned to "+project+". Please complete your task of implementing "+feature+" and provide necessary comment below:";
            }
            addCommentArea = true;
        }
        Text projectAndFeatureTxt = new Text(projectAndFeature);
        projectAndFeatureTxt.setFont(anTitleFont);
        projectAndFeatureTxt.setLayoutX(xCorForProjAFeatTxt);
        projectAndFeatureTxt.setLayoutY(250);

        objectionBtn.setOnAction( objecttionBtnEvent-> {
            if (status.equals("dev")){
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed!");
                failure.setHeaderText("Please let the Developer Implement It First!");
                failure.show();
                return;
            } else if (status.equals("teamlead")) {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed!");
                failure.setHeaderText("Please let the Teamlead Review It First!");
                failure.show();
                return;
            }
            new miniObjection().switchToMiniObjection(new Stage(),stage,feature, project,mail,name,phone,devMsg,status);
        });

        TextArea commentArea = new TextArea();
        commentArea.setFont(normalFont);
        commentArea.setLayoutX(370);
        commentArea.setLayoutY(280);
        commentArea.setWrapText(true);

        String buttonText = "";
        if (dev){
            buttonText = "Forward to SQA";
        }
        else {
            buttonText = "Forward to Teamlead";
        }
        Button postCommentBtn = new Button(buttonText);
        postCommentBtn.setFont(normalFont);
        postCommentBtn.setLayoutX(700);
        postCommentBtn.setLayoutY(560);
        final boolean devIden = dev;
        System.out.println(devIden);
        postCommentBtn.setOnAction( postCommentEvent -> {
            String devOrSQAPostMsg = commentArea.getText();
            Alert confirmLast = new Alert(Alert.AlertType.CONFIRMATION);
            String alertHead = "";
            if (devIden){
                alertHead = "Do you wish to forward this feature to the assigned SQA engineer?";
            }
            else {
                alertHead = "Do you wish to forward this feature to your teamlead?";
            }
            confirmLast.setTitle("Confirm");
            confirmLast.setHeaderText(alertHead);
            confirmLast.showAndWait().ifPresent( result->{
                if (result==ButtonType.OK){
                    postForTeamlead(stage,mail,name,phone,rank,project,feature,status,devOrSQAPostMsg,sqaMsg,devMsg,devIden);
                }
            } );
        });

        boolean addComment = false;
        String txtForTitle = "";
        if (!dev && project!=null){
            txtForTitle = "Message from Developer:";
        }
        else if (dev && project!=null){
            txtForTitle = "Message from SQA:";
        }
        Text msgFromSQAOrDevTxt = new Text(txtForTitle);
        msgFromSQAOrDevTxt.setFont(anTitleFont);
        msgFromSQAOrDevTxt.setLayoutX(960);
        msgFromSQAOrDevTxt.setLayoutY(298);
        String msg = "";
        if (dev && project!=null){
            if (sqaMsg!=null){
                if (!sqaMsg.isEmpty()) {
                    addComment = true;
                    msg = sqaMsg;
                }
            }
        }
        else if (!dev && project!=null){
            if (devMsg!=null){
                if (!devMsg.isEmpty()){
                    addComment = true;
                    msg = devMsg;
                }
            }
        }

        Text msgFromSQAOrDev = new Text(msg);
        msgFromSQAOrDev.setFont(smallerTitleFont);
        msgFromSQAOrDev.setLayoutX(960);
        msgFromSQAOrDev.setLayoutY(330);
        msgFromSQAOrDev.setWrappingWidth(600);

        root.getChildren().addAll(logOutBtn,detailText,nameText,rankText,mailText,phoneText,refreshBtn,projectAndFeatureTxt);
        if (addCommentArea && project!=null){
            root.getChildren().addAll(commentArea,postCommentBtn);
        }
        if (addComment && project!=null){
            commentArea.setLayoutX(100);
            postCommentBtn.setLayoutX(430);
            root.getChildren().addAll(msgFromSQAOrDevTxt,msgFromSQAOrDev);
        }
        if (!dev && project!=null){
            root.getChildren().addAll(objectionBtn);
        }

        Image logo = new Image("logo.png");

        Scene scene = new Scene(root);

        stage.getIcons().add(logo);
        stage.setTitle("Project Progress Tracker");
        stage.setScene(scene);
        stage.setHeight(900);
        stage.setWidth(1600);
        stage.setResizable(false);
        stage.show();
    }
    public void postForTeamlead(Stage stage,String mail,String name,String phone,String rank, String project, String feature, String status, String devOrSQAPostMsg,String sqaMsg,String devMsg, final boolean devIden){
        String postQuery = "";
        if (devOrSQAPostMsg==null || devOrSQAPostMsg.isEmpty()){
            Alert failure = new Alert(Alert.AlertType.ERROR);
            failure.setTitle("Failed!");
            failure.setHeaderText("Please Provide A Valid Comment!");
            failure.show();
            return;
        }
        if (devIden){
            System.out.println(devIden);
            if (status.equals("sqa")){
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed!");
                failure.setHeaderText("Please let the SQA Engineer Test It First!");
                failure.show();
                return;
            }
            else if (status.equals("teamlead")){
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed!");
                failure.setHeaderText("Please let the Teamlead Inspect It First!");
                failure.show();
                return;
            }
            postQuery = "UPDATE Project_TABLE SET Dev_comment='"+devOrSQAPostMsg+"', status='sqa' WHERE dev_mail='"+mail+"'";
        }
        else {
            if (status.equals("dev")){
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed!");
                failure.setHeaderText("Please let the Developer Implement It First!");
                failure.show();
                return;
            }
            else if (status.equals("teamlead")){
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed!");
                failure.setHeaderText("Please let the Teamlead Inspect It First!");
                failure.show();
                return;
            }
            postQuery = "UPDATE Project_TABLE SET Sqa_comment='"+devOrSQAPostMsg+"', status='teamlead' WHERE sqa_mail='"+mail+"'";
        }
        ConnectDB fetch = new ConnectDB();
        try (Connection con = fetch.connect()) {
            Statement postStatement = con.createStatement();
            int rows = 0;
            rows = postStatement.executeUpdate(postQuery);
            if (rows>0){
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success!");
                success.setHeaderText("Progress Successfully Recorded!");
                success.show();
                if (devIden){
                    new DevSqaLoginScene().switchToDevSqaLoginScene(stage,mail,name,phone,rank,project,feature,devOrSQAPostMsg,sqaMsg,"sqa");
                }
                else {
                    new DevSqaLoginScene().switchToDevSqaLoginScene(stage,mail,name,phone,rank,project,feature,devMsg,devOrSQAPostMsg,"teamlead");
                }
            }
            else {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Failed!");
                failure.setHeaderText("Progress Record Failed Please Try Again!");
                failure.show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
