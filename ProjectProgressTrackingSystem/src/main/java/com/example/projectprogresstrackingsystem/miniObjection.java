package com.example.projectprogresstrackingsystem;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

public class miniObjection {
    public void switchToMiniObjection(Stage miniObjectionStage, Stage stage, String feature, String project, String mail,String name,String phone,String devMsg,String status){
        Pane root = new Pane();

        Font anTitleFont = new Font("Ramaraja", 30);
        Font normalFont = new Font(20);

        Text titleText = new Text("Please provide a detailed overview below:");
        titleText.setFont(anTitleFont);
        titleText.setLayoutX(420);
        titleText.setLayoutY(30);

        TextArea commentArea = new TextArea();
        commentArea.setFont(normalFont);
        commentArea.setLayoutX(130);
        commentArea.setLayoutY(60);
        commentArea.setWrapText(true);

        Button postObjectionBtn = new Button("Post Objection");
        postObjectionBtn.setFont(normalFont);
        postObjectionBtn.setLayoutX(470);
        postObjectionBtn.setLayoutY(350);
        postObjectionBtn.setOnAction( postObjectionBtnEvent-> {
            String sqaMsg = commentArea.getText();
            Alert confirmLast = new Alert(Alert.AlertType.CONFIRMATION);
            String alertHead = "Do you want to send the feature back to developer for fixing";
            confirmLast.setTitle("Confirm");
            confirmLast.setHeaderText(alertHead);
            confirmLast.showAndWait().ifPresent( result->{
                if (result== ButtonType.OK){
                    postBackToDeveloper(miniObjectionStage, stage,sqaMsg,feature,project,mail,name,phone,devMsg,status);
                }
            } );
        });

        root.getChildren().addAll(titleText,commentArea,postObjectionBtn);

        Scene scene = new Scene(root);

        miniObjectionStage.setTitle("Object on "+feature+" of "+project);
        miniObjectionStage.setScene(scene);
        miniObjectionStage.setHeight(500);
        miniObjectionStage.setWidth(1100);
        miniObjectionStage.setResizable(false);
        miniObjectionStage.show();
    }

    public void postBackToDeveloper(Stage miniObjectionStage, Stage stage,String sqaMsg, String feature, String project, String mail,String name,String phone,String devMsg,String status){
        String updateQuery = "UPDATE Project_TABLE SET status='dev', Sqa_comment='"+sqaMsg+"' WHERE sqa_mail='"+mail+"'";
        ConnectDB fetch = new ConnectDB();
        try (Connection con = fetch.connect()) {
            Statement updateStatement = con.createStatement();
            int rows = 0;
            rows = updateStatement.executeUpdate(updateQuery);
            if (rows>0){
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success!");
                success.setHeaderText("Review Successfully Recorded!");
                success.show();
                miniObjectionStage.close();
                new DevSqaLoginScene().switchToDevSqaLoginScene(stage,mail,name,phone,"SQA",project,feature,devMsg,sqaMsg,"dev");
            }
            else {
//                Alert failure = new Alert(Alert.AlertType.ERROR);
//                failure.setTitle("Failed!");
//                failure.setHeaderText("Review Record Failed Please Try Again!");
//                failure.show();
                throw new CustomException("revRecordFailed");
            }
        } catch (Exception e){
//            e.printStackTrace();
            try {
                throw new CustomException("db_connect");
            } catch (CustomException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
