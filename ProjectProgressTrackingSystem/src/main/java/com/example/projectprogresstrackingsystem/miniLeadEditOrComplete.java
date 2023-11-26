package com.example.projectprogresstrackingsystem;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class miniLeadEditOrComplete {
    public void switchToMiniLeadEditOrComplete(Stage miniLeadEditOrComoleteStage, Stage stage, String mail, String name, String phone, String rank, String project, String selectedFeature, String status){
        Pane root = new Pane();

        Font titleFont = new Font("Ramaraja", 70);
        Font anTitleFont = new Font("Ramaraja", 30);
        Font smallerTitleFont = new Font("Ramaraja", 25);
        Font normalFont = new Font(20);
        Font logOutBtnFont = new Font(20);

        Text titleTxt = new Text("Edit, Reassign or mark feature as complete");
        titleTxt.setFont(anTitleFont);
        titleTxt.setLayoutX(270);
        titleTxt.setLayoutY(40);

        Button completeFeatureBtn = new Button("Mark As Complete");
        completeFeatureBtn.setFont(logOutBtnFont);
        completeFeatureBtn.setLayoutX(760);
        completeFeatureBtn.setLayoutY(15);
        completeFeatureBtn.setStyle("-fx-background-color: red;-fx-text-fill: white;");

        Text changeDevTxt = new Text("Change Developer: ");
        changeDevTxt.setFont(anTitleFont);
        changeDevTxt.setLayoutX(115);
        changeDevTxt.setLayoutY(140);

        ComboBox<String> freeDevs = new ComboBox<>();
        ConnectDB fetch = new ConnectDB();
        Map<String,String>freeDevsMap = new HashMap<>();
        String currentDevsMail = "";
        String currentDevsName = "";
        try (Connection con = fetch.connect()) {
            Statement freeDevsStatement = con.createStatement();
            Statement currentDevsStatement = con.createStatement();
            String freeDevsQuery = "SELECT * FROM Developer_TABLE WHERE project IS NULL";
            String currentDevsQuery = "SELECT * FROM Project_TABLE WHERE name='"+project+"' AND feature='"+selectedFeature+"'";
            ResultSet freeDevsData = freeDevsStatement.executeQuery(freeDevsQuery);
            ResultSet currentDevsData = currentDevsStatement.executeQuery(currentDevsQuery);
            while (freeDevsData.next()){
                freeDevsMap.put(freeDevsData.getString("email"),freeDevsData.getString("name"));
            }
            while (currentDevsData.next()){
                currentDevsMail = currentDevsData.getString("dev_mail");
            }
            String currentDevsNameQuery = "SELECT name FROM Developer_TABLE WHERE email='"+currentDevsMail+"'";
            Statement currentDevNameStatement = con.createStatement();
            ResultSet currentDevsNameData = currentDevNameStatement.executeQuery(currentDevsNameQuery);
            while (currentDevsNameData.next()){
                currentDevsName = currentDevsNameData.getString("name");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        for (Map.Entry<String,String>entry : freeDevsMap.entrySet()) {
            freeDevs.getItems().add(entry.getValue());
        }
        freeDevs.getItems().add(currentDevsName);
        freeDevs.setValue(currentDevsName);
        freeDevs.setStyle("-fx-font-size: 18 px;-fx-font-weight: bold;");
        freeDevs.setLayoutX(340);
        freeDevs.setLayoutY(120);

        Text changeSQATxt  = new Text("Change SQA:");
        changeSQATxt.setFont(anTitleFont);
        changeSQATxt.setLayoutX(128);
        changeSQATxt.setLayoutY(200);

        ComboBox<String> freeSQA = new ComboBox<>();
        Map<String,String>freeSqaMap = new HashMap<>();
        String currentSqaMail = "";
        String currentSqaName = "";
        try (Connection con = fetch.connect()) {
            Statement freeSqaStatement = con.createStatement();
            Statement currentSqaStatement = con.createStatement();
            String freeSqaQuery = "SELECT * FROM SQA_TABLE WHERE project IS NULL";
            String currentSqaQuery = "SELECT * FROM Project_TABLE WHERE name='"+project+"' AND feature='"+selectedFeature+"'";
            ResultSet freeSqaData = freeSqaStatement.executeQuery(freeSqaQuery);
            ResultSet currentSqaData = currentSqaStatement.executeQuery(currentSqaQuery);
            while (freeSqaData.next()){
                freeSqaMap.put(freeSqaData.getString("email"),freeSqaData.getString("name"));
            }
            while (currentSqaData.next()){
                currentSqaMail = currentSqaData.getString("sqa_mail");
            }
            String currentSqaNameQuery = "SELECT name FROM SQA_TABLE WHERE email='"+currentSqaMail+"'";
            Statement currentSqaNameStatement = con.createStatement();
            ResultSet currentSqaNameData = currentSqaNameStatement.executeQuery(currentSqaNameQuery);
            while (currentSqaNameData.next()){
                currentSqaName = currentSqaNameData.getString("name");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        for (Map.Entry<String,String>entry : freeSqaMap.entrySet()) {
            freeSQA.getItems().add(entry.getValue());
        }
        freeSQA.getItems().add(currentSqaName);
        freeSQA.setValue(currentSqaName);
        freeSQA.setStyle("-fx-font-size: 18 px;-fx-font-weight: bold;");
        freeSQA.setLayoutX(288);
        freeSQA.setLayoutY(180);

        Button reassignBtn = new Button("Reassign Selected");
        reassignBtn.setFont(normalFont);
        reassignBtn.setLayoutX(210);
        reassignBtn.setLayoutY(240);
        final String currentDevNameForLambda = currentDevsName;
        final String currentDevMailForLambda = currentDevsMail;
        final String currentSqaNameForLambda = currentSqaName;
        final String currentSqaMailForLambda = currentSqaMail;
        reassignBtn.setOnAction( reassignBtnEvent-> {
            String newDev = freeDevs.getValue();
            String newSqa = freeSQA.getValue();
            Alert confirmLast = new Alert(Alert.AlertType.CONFIRMATION);
            String alertHead = "Do you want to change developer/sqa for this feature?";
            confirmLast.setTitle("Confirm");
            confirmLast.setHeaderText(alertHead);
            confirmLast.showAndWait().ifPresent( result->{
                if (result== ButtonType.OK){
                    changeDevOrSQA(newDev,newSqa,currentDevNameForLambda,currentDevMailForLambda,currentSqaNameForLambda,currentSqaMailForLambda,freeDevsMap,freeSqaMap,status,project,selectedFeature);
                    miniLeadEditOrComoleteStage.close();
                    new TeamleadLoginScene().switchToTeamleadLoginScene(stage,mail,name,phone,rank,project);
                }
            } );
        });

        completeFeatureBtn.setOnAction( completeFeatureBtnEvent-> {
            Alert confirmLast = new Alert(Alert.AlertType.CONFIRMATION);
            String alertHead = "Do you want to mark this feature as completed?";
            confirmLast.setTitle("Confirm");
            confirmLast.setHeaderText(alertHead);
            confirmLast.showAndWait().ifPresent( result->{
                if (result== ButtonType.OK){
                    if (status.equals("Submitted to Teamlead")){
                        completeFeature(mail,currentDevMailForLambda,currentSqaMailForLambda,project,selectedFeature);
                        miniLeadEditOrComoleteStage.close();
                        new TeamleadLoginScene().switchToTeamleadLoginScene(stage,mail,name,phone,rank,project);
                    } else if (status.equals("Under Development")) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error!");
                        error.setHeaderText("Not Developed Yet!");
                        error.show();
                    }
                    else if (status.equals("Under Testing")){
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error!");
                        error.setHeaderText("Not Tested Yet!");
                        error.show();
                    }
                }
            } );
        });

        Line line1 = new Line(520,60,520,150);
        line1.setStrokeWidth(2);

        Text orText = new Text("OR");
        orText.setFont(anTitleFont);
        orText.setLayoutX(500);
        orText.setLayoutY(185);

        Line line2 = new Line(520,200,520,300);
        line2.setStrokeWidth(2);

        Text commentTxt = new Text("Please provide detailed info on what is wrong before reassigning the feature:");
        commentTxt.setFont(smallerTitleFont);
        commentTxt.setLayoutX(525);
        commentTxt.setLayoutY(80);
        commentTxt.setWrappingWidth(450);

        TextArea commentArea = new TextArea();
        commentArea.setFont(normalFont);
        commentArea.setWrapText(true);
        commentArea.setLayoutX(545);
        commentArea.setLayoutY(140);
        commentArea.setPrefWidth(420);
        commentArea.setPrefHeight(200);

        Button reassignToDevBtn = new Button("Reassign to Developer");
        reassignToDevBtn.setFont(normalFont);
        reassignToDevBtn.setLayoutX(640);
        reassignToDevBtn.setLayoutY(350);
        reassignToDevBtn.setOnAction( reassignToDevBtnEvent-> {
            String leadComment = commentArea.getText();
            if (leadComment==null || leadComment.isEmpty()){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Please Provide a valid comment!");
                error.show();
            }
            else {
                Alert confirmLast = new Alert(Alert.AlertType.CONFIRMATION);
                String alertHead = "Do you want to change developer/sqa for this feature?";
                confirmLast.setTitle("Confirm");
                confirmLast.setHeaderText(alertHead);
                confirmLast.showAndWait().ifPresent( result->{
                    if (result== ButtonType.OK){
                        reAssignToDev(leadComment,mail,project,selectedFeature);;
                        miniLeadEditOrComoleteStage.close();
                        new TeamleadLoginScene().switchToTeamleadLoginScene(stage,mail,name,phone,rank,project);
                    }
                } );
            }
        });

        root.getChildren().addAll(titleTxt,freeDevs,changeDevTxt,changeSQATxt,freeSQA,reassignBtn,line1,orText,line2,commentTxt,commentArea,reassignToDevBtn, completeFeatureBtn);
        Scene scene = new Scene(root);

        miniLeadEditOrComoleteStage.setTitle("Edit or Complete Project");
        miniLeadEditOrComoleteStage.setScene(scene);
        miniLeadEditOrComoleteStage.setHeight(500);
        miniLeadEditOrComoleteStage.setWidth(1000);
        miniLeadEditOrComoleteStage.setResizable(false);
        miniLeadEditOrComoleteStage.show();
    }
    public void changeDevOrSQA(String newDev, String newSqa, final String currentDevNameForLambda, final String currentDevMailForLambda,final String currentSqaNameForLambda, String currentSqaMailForLambda, Map<String,String>freeDev, Map<String,String>freeSqa, final String status, String project, String feature){
        String clearDevTable = "";
        String setDevTable = "";
        String clearSqaTable = "";
        String setSqaTable = "";
        String changeProjectTableData = "";
        String newDevMail = "";
        String newSqaMail = "";
        ConnectDB fetch = new ConnectDB();
        for (Map.Entry<String,String>entry : freeDev.entrySet()) {
            if (entry.getValue().equals(newDev)){
                newDevMail = entry.getKey();
            }
        }
        for (Map.Entry<String,String>entry : freeSqa.entrySet()) {
            if (entry.getValue().equals(newSqa)){
                newSqaMail = entry.getKey();
            }
        }
        if (newDev.equals(currentDevNameForLambda) && newSqa.equals(currentSqaNameForLambda)){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText("Please Change Developer or SQA First To Reassign!");
            error.show();
        } else if (!newDev.equals(currentDevNameForLambda) && !newSqa.equals(currentSqaNameForLambda) && !status.equals("Completed")) {
            clearDevTable = "UPDATE Developer_TABLE SET project=NULL,feature=NULL WHERE email='"+currentDevMailForLambda+"'";
            setDevTable = "UPDATE Developer_TABLE SET project='"+project+"', feature='"+feature+"' WHERE email='"+newDevMail+"'";
            clearSqaTable = "UPDATE SQA_TABLE SET project=NULL,feature=NULL WHERE email='"+currentSqaMailForLambda+"'";
            setSqaTable = "UPDATE SQA_TABLE SET project='"+project+"', feature='"+feature+"' WHERE email='"+newSqaMail+"'";
            changeProjectTableData = "UPDATE Project_TABLE SET dev_mail='"+newDevMail+"', sqa_mail='"+newSqaMail+"' WHERE name='"+project+"' AND feature='"+feature+"'";
            try (Connection con = fetch.connect()) {
                Statement devClearStatement = con.createStatement();
                int rowsForDevClear = 0;
                rowsForDevClear = devClearStatement.executeUpdate(clearDevTable);
                if (rowsForDevClear>0){
                    Statement devSetStatement = con.createStatement();
                    int rowsForDevSet = 0;
                    rowsForDevSet = devSetStatement.executeUpdate(setDevTable);
                    if (rowsForDevSet>0){
                        Statement sqaClearStatement = con.createStatement();
                        int rowsForSqaClear = 0;
                        rowsForSqaClear = sqaClearStatement.executeUpdate(clearSqaTable);
                        if (rowsForSqaClear>0){
                            Statement sqaSetStatement = con.createStatement();
                            int rowsForSqaSet = 0;
                            rowsForSqaSet = sqaSetStatement.executeUpdate(setSqaTable);
                            if (rowsForSqaSet>0){
                                Statement projectTableChange = con.createStatement();
                                int rowsForProjectTable = 0;
                                rowsForProjectTable = projectTableChange.executeUpdate(changeProjectTableData);
                                if (rowsForProjectTable>0){
                                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                                    success.setTitle("Success!");
                                    success.setHeaderText("Reassigning Completed!");
                                    success.show();
                                }else {
                                    Alert error = new Alert(Alert.AlertType.ERROR);
                                    error.setTitle("Error!");
                                    error.setHeaderText("Reassigning Failed!");
                                    error.show();
                                }
                            }
                            else {
                                Alert error = new Alert(Alert.AlertType.ERROR);
                                error.setTitle("Error!");
                                error.setHeaderText("Reassigning Failed!");
                                error.show();
                            }
                        }
                        else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error!");
                            error.setHeaderText("Reassigning Failed!");
                            error.show();
                        }
                    }
                    else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error!");
                        error.setHeaderText("Reassigning Failed!");
                        error.show();
                    }
                }
                else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error!");
                    error.setHeaderText("Reassigning Failed!");
                    error.show();
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (!newDev.equals(currentDevNameForLambda) && !status.equals("Completed")) {
            clearDevTable = "UPDATE Developer_TABLE SET project=NULL,feature=NULL WHERE email='"+currentDevMailForLambda+"'";
            setDevTable = "UPDATE Developer_TABLE SET project='"+project+"', feature='"+feature+"' WHERE email='"+newDevMail+"'";
            changeProjectTableData = "UPDATE Project_TABLE SET dev_mail='"+newDevMail+"' WHERE name='"+project+"' AND feature='"+feature+"'";
            try (Connection con = fetch.connect()) {
                Statement devClearStatement = con.createStatement();
                int rowsForDevClear = 0;
                rowsForDevClear = devClearStatement.executeUpdate(clearDevTable);
                if (rowsForDevClear>0){
                    Statement devSetStatement = con.createStatement();
                    int rowsForDevSet = 0;
                    rowsForDevSet = devSetStatement.executeUpdate(setDevTable);
                    if (rowsForDevSet>0){
                        Statement projectTableChange = con.createStatement();
                        int rowsForProjectTable = 0;
                        rowsForProjectTable = projectTableChange.executeUpdate(changeProjectTableData);
                        if (rowsForProjectTable>0){
                            Alert success = new Alert(Alert.AlertType.INFORMATION);
                            success.setTitle("Success!");
                            success.setHeaderText("Reassigning Completed!");
                            success.show();
                        }
                        else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error!");
                            error.setHeaderText("Reassigning Failed!");
                            error.show();
                        }
                    }
                    else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error!");
                        error.setHeaderText("Reassigning Failed!");
                        error.show();
                    }
                }
                else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error!");
                    error.setHeaderText("Reassigning Failed!");
                    error.show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (!newSqa.equals(currentSqaNameForLambda) && !status.equals("Completed")) {
            clearSqaTable = "UPDATE SQA_TABLE SET project=NULL,feature=NULL WHERE email='"+currentSqaMailForLambda+"'";
            setSqaTable = "UPDATE SQA_TABLE SET project='"+project+"', feature='"+feature+"' WHERE email='"+newSqaMail+"'";
            changeProjectTableData = "UPDATE Project_TABLE SET sqa_mail='"+newSqaMail+"' WHERE name='"+project+"' AND feature='"+feature+"'";
            try (Connection con = fetch.connect()) {
                Statement sqaClearStatement = con.createStatement();
                int rowsForSqaClear = 0;
                rowsForSqaClear = sqaClearStatement.executeUpdate(clearSqaTable);
                if (rowsForSqaClear>0){
                    Statement sqaSetStatement = con.createStatement();
                    int rowsForSqaSet = 0;
                    rowsForSqaSet = sqaSetStatement.executeUpdate(setSqaTable);
                    if (rowsForSqaSet>0){
                        Statement projectTableChange = con.createStatement();
                        int rowsForProjectTable = 0;
                        rowsForProjectTable = projectTableChange.executeUpdate(changeProjectTableData);
                        if (rowsForProjectTable>0){
                            Alert success = new Alert(Alert.AlertType.INFORMATION);
                            success.setTitle("Success!");
                            success.setHeaderText("Reassigning Completed!");
                            success.show();
                        }else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Error!");
                            error.setHeaderText("Reassigning Failed!");
                            error.show();
                        }
                    }
                    else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error!");
                        error.setHeaderText("Reassigning Failed!");
                        error.show();
                    }
                }
                else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error!");
                    error.setHeaderText("Reassigning Failed!");
                    error.show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void reAssignToDev(String leadComment, String mail, String project, String feature){
        ConnectDB fetch = new ConnectDB();
        String reAssignToDevQuery = "UPDATE Project_TABLE SET Lead_comment='"+leadComment+"', status='dev' WHERE lead_mail='"+mail+"' AND name='"+project+"' AND feature='"+feature+"'";
        try (Connection con = fetch.connect()) {
            Statement reAssignToDevStatement = con.createStatement();
            int rows = 0;
            rows = reAssignToDevStatement.executeUpdate(reAssignToDevQuery);
            if (rows>0){
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success!");
                success.setHeaderText("Reassigned To Developer Successfully!");
                success.show();
            }
            else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Reassigning To Developer Failed!");
                error.show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void completeFeature(String leadMail,String currentDevMail, String currentSqaMail, String project, String feature){
        ConnectDB fetch = new ConnectDB();
        String releaseDev = "UPDATE Developer_TABLE SET project=NULL, feature=NULL WHERE email='"+currentDevMail+"'";
        String releaseSqa = "UPDATE SQA_TABLE SET project=NULL, feature=NULL WHERE email='"+currentSqaMail+"'";
        String projectTableUpdate = "UPDATE Project_TABLE SET status='complete', Lead_comment='Done' WHERE lead_mail='"+leadMail+"' AND dev_mail='"+currentDevMail+"' AND sqa_mail='"+currentSqaMail+"' AND name='"+project+"' AND feature='"+feature+"'";
        try (Connection con = fetch.connect()) {
            Statement releaseDevStatement = con.createStatement();
            int rowsReleaseDev = 0;
            rowsReleaseDev = releaseDevStatement.executeUpdate(releaseDev);
            if (rowsReleaseDev>0){
                Statement releaseSqaStatement = con.createStatement();
                int rowsReleaseSqa = 0;
                rowsReleaseSqa = releaseSqaStatement.executeUpdate(releaseSqa);
                if (rowsReleaseSqa>0){
                    Statement projectTableUpdateStatement = con.createStatement();
                    int rowsProjectTableUpdate = 0;
                    rowsProjectTableUpdate = projectTableUpdateStatement.executeUpdate(projectTableUpdate);
                    if (rowsProjectTableUpdate>0){
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success!");
                        success.setHeaderText("Feature Completion Was A Success!");
                        success.show();
                    }
                    else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error!");
                        error.setHeaderText("Feature Completion Failed!");
                        error.show();
                    }
                }
                else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error!");
                    error.setHeaderText("Feature Completion Failed!");
                    error.show();
                }
            }
            else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Feature Completion Failed!");
                error.show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
