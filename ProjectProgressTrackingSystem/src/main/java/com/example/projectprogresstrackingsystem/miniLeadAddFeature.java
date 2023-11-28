package com.example.projectprogresstrackingsystem;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class miniLeadAddFeature {
    public void miniAddFeatureEvent(Stage stage, String mail, String curProjectName, String name, String phone, String rank, String project){
        Stage mini_lead_add_feature_stage = new Stage();

        Pane root = new Pane();

        Font anTitleFont = new Font("Ramaraja", 30);
        Font normalFont = new Font(20);

        Text featureNameText = new Text("Feature Name");
        featureNameText.setFont(anTitleFont);
        featureNameText.setLayoutX(230);
        featureNameText.setLayoutY(50);

        TextField featureNameField = new TextField();
        featureNameField.setFont(normalFont);
        featureNameField.setLayoutX(230);
        featureNameField.setLayoutY(60);

        Text assignDevText = new Text("Assign a Developer:");
        assignDevText.setFont(anTitleFont);
        assignDevText.setLayoutX(230);
        assignDevText.setLayoutY(130);

        ComboBox<String>assignDev = new ComboBox<>();
        assignDev.getItems().addAll("--Choose a Developer--");
        String devQuery = "SELECT * FROM Developer_TABLE", sqaQuery = "SELECT * FROM SQA_TABLE";
        ConnectDB fetch = new ConnectDB();
        Map<String,String> freeDevMap = new HashMap<>();
        Map<String,String> freeSqaMap = new HashMap<>();
        try (Connection con = fetch.connect()) {
            Statement statement = con.createStatement();
            ResultSet devData = statement.executeQuery(devQuery);
            while (devData.next()){
                if (devData.getString("project")==null && !devData.getString("password").equals("N/A")){
                    freeDevMap.put(devData.getString("email"), devData.getString("name"));
                }
            }
            ResultSet sqaData = statement.executeQuery(sqaQuery);
            while (sqaData.next()){
                if (sqaData.getString("project")==null && !sqaData.getString("password").equals("N/A")){
                    freeSqaMap.put(sqaData.getString("email"), sqaData.getString("name"));
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        for (Map.Entry<String,String>entry : freeDevMap.entrySet()) {
            assignDev.getItems().add(entry.getValue());
        }
        assignDev.setValue("--Choose a Developer--");
        assignDev.setStyle("-fx-font-size: 18 px;-fx-font-weight: bold;");
        assignDev.setLayoutX(230);
        assignDev.setLayoutY(145);

        Text assignSqaText = new Text("Assign an SQA:");
        assignSqaText.setFont(anTitleFont);
        assignSqaText.setLayoutX(230);
        assignSqaText.setLayoutY(210);

        ComboBox<String>assignSqa = new ComboBox<>();
        assignSqa.getItems().addAll("--Choose an SQA--");
        assignSqa.setValue("--Choose an SQA--");
        for (Map.Entry<String,String>entry : freeSqaMap.entrySet()) {
            assignSqa.getItems().add(entry.getValue());
        }
        assignSqa.setStyle("-fx-font-size: 18 px;-fx-font-weight: bold;");
        assignSqa.setLayoutX(230);
        assignSqa.setLayoutY(225);

        Button addFeatureBtn = new Button("Add This Feature");
        addFeatureBtn.setFont(normalFont);
        addFeatureBtn.setLayoutX(245);
        addFeatureBtn.setLayoutY(270);
        addFeatureBtn.setOnAction( addFeatureEvent -> {
            String featureName = featureNameField.getText();
            String dev = assignDev.getValue();
            String sqa = assignSqa.getValue();
            String dev_mail = "";
            String sqa_mail = "";
            for (Map.Entry<String,String>entry : freeDevMap.entrySet()) {
                if (entry.getValue().equals(dev)){
                    dev_mail = entry.getKey();
                }
            }
            for (Map.Entry<String,String>entry : freeSqaMap.entrySet()) {
                if (entry.getValue().equals(sqa)){
                    sqa_mail = entry.getKey();
                }
            }
            if (featureName!=null && !dev.equals("--Choose a Developer--") && !sqa.equals("--Choose an SQA--")){
                String devUpQuery = "UPDATE Developer_TABLE SET project='"+curProjectName+"', feature='"+featureName+"' WHERE email='"+dev_mail+"'";
                String sqaUpQuery = "UPDATE SQA_TABLE SET project='"+curProjectName+"', feature='"+featureName+"' WHERE email='"+sqa_mail+"'";
                String projectAddQuery = "INSERT INTO Project_TABLE(lead_mail,dev_mail,sqa_mail,name,feature,status) VALUES('"+mail+"','"+dev_mail+"','"+sqa_mail+"','"+curProjectName+"','"+featureName+"','dev')";
                try (Connection con = fetch.connect()) {
                    Statement statement = con.createStatement();
                    int rowDev = statement.executeUpdate(devUpQuery);
                    int rowSqa =  statement.executeUpdate(sqaUpQuery);
                    int rowProject = statement.executeUpdate(projectAddQuery);
                    if (rowDev!=0 && rowSqa!=0 && rowProject!=0){
                        mini_lead_add_feature_stage.close();
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success!");
                        success.setHeaderText("Feature Successfully Added!");
                        success.showAndWait();
                    }
                    else{
                        Alert failure = new Alert(Alert.AlertType.ERROR);
                        failure.setTitle("Error!");
                        failure.setHeaderText("Feature Addition Failed!");
                        failure.show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    new TeamleadLoginScene().switchToTeamleadLoginScene(stage, mail, name, phone, rank,project);
                } catch (CustomException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        root.getChildren().addAll(featureNameText,featureNameField,assignDevText,assignDev,assignSqaText,assignSqa,addFeatureBtn);

        Scene scene = new Scene(root);

        mini_lead_add_feature_stage.setTitle("Add New Project");
        mini_lead_add_feature_stage.setScene(scene);
        mini_lead_add_feature_stage.setHeight(400);
        mini_lead_add_feature_stage.setWidth(700);
        mini_lead_add_feature_stage.setResizable(false);
        mini_lead_add_feature_stage.show();
    }
}
