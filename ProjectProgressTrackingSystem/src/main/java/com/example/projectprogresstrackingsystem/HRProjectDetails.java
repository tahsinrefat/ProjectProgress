package com.example.projectprogresstrackingsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HRProjectDetails extends SceneController{
    public void switchToHRProjectDetails(Stage stage, String mail, String name, String phone, String rank, String project){
        Pane root = new Pane();

        Font titleFont = new Font("Ramaraja", 70);
        Font anTitleFont = new Font("Ramaraja", 30);
        Font smallerTitleFont = new Font("Ramaraja", 25);
        Font normalFont = new Font(20);
        Font logOutBtnFont = new Font(20);
        Font backBtnFont = new Font(20);

        Button backBtn = new Button("<< Back");
        backBtn.setFont(backBtnFont);
        backBtn.setLayoutX(20);
        backBtn.setLayoutY(20);
        backBtn.setCursor(Cursor.HAND);
        backBtn.setOnAction(backEvent -> {
            new HRLoginScene().switchToHRLoginScene(stage, name, mail, phone, rank);
        });

        Button logOutBtn = new Button("Logout");
        logOutBtn.setFont(logOutBtnFont);
        logOutBtn.setLayoutX(1460);
        logOutBtn.setLayoutY(20);
        logOutBtn.setStyle("-fx-background-color: red;-fx-text-fill: white;");
        logOutBtn.setCursor(Cursor.HAND);
        logOutBtn.setOnAction(logOutEvent -> {
            LoginSignScene backToLogin = new LoginSignScene();
            try {
                backToLogin.switchToLogSignScene(null, stage);
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
        });

        Text detailText = new Text("Project Details");
        detailText.setFont(titleFont);
        detailText.setLayoutX(620);
        detailText.setLayoutY(120);

        Text nameText = new Text(name);
        nameText.setFont(anTitleFont);
        nameText.setLayoutX(20);
        nameText.setLayoutY(200);

        Text rankText = new Text("("+rank+")");
        rankText.setFont(smallerTitleFont);
        rankText.setLayoutX(22+nameText.getLayoutBounds().getWidth());
        rankText.setLayoutY(200);

        Text mailText = new Text(mail);
        mailText.setFont(smallerTitleFont);
        mailText.setLayoutX(20);
        mailText.setLayoutY(225);

        Text phoneText = new Text(phone);
        phoneText.setFont(smallerTitleFont);
        phoneText.setLayoutX(20);
        phoneText.setLayoutY(250);

        Text projectNameTxt = new Text("Project Name is: "+project);
        projectNameTxt.setFont(anTitleFont);
        projectNameTxt.setLayoutX(20);
        projectNameTxt.setLayoutY(285);

        //need to do work later
        String tableColumnStyle = "-fx-font-size: 25px; -fx-font-family: 'Ramaraja';";
        TableView<CurrentProjects> curProjectTable = new TableView<>();
        curProjectTable.getSelectionModel().setCellSelectionEnabled(true);
        curProjectTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        curProjectTable.setStyle("-fx-background-color: #1777e6;");
        curProjectTable.setOnMouseClicked(event -> {
            ObservableList<TablePosition> selectedCells = curProjectTable.getSelectionModel().getSelectedCells();
            int row = -1;
            int col = -1;
            String value = "";
            String status = "";
            for (TablePosition tablePosition : selectedCells) {
                row = tablePosition.getRow();
                col = tablePosition.getColumn();
                value = curProjectTable.getColumns().get(0).getCellData(row).toString();
                status = curProjectTable.getColumns().get(5).getCellData(row).toString();
                System.out.println("Selected Cell Value: " + value);
                System.out.println(row + " " + col);
            }
            System.out.println(value);
            new miniLeadEditOrComplete().switchToMiniLeadEditOrComplete(new Stage(),stage,mail,name,phone,rank,project,value,status);
//            new HRProjectDetails().switchToProjectDetailsScene(stage, mail, name, phone, rank, value);
        });

        TableColumn<CurrentProjects, String> featureName = new TableColumn<>("Feature Name");
        featureName.setPrefWidth(150);
        featureName.setStyle(tableColumnStyle);
        featureName.setCellValueFactory(cellData -> cellData.getValue().featureNameProperty());
        featureName.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    int row = getIndex();
                    setFont(smallerTitleFont);
                }
            }
        });

        TableColumn<CurrentProjects, String> sqaName = new TableColumn<>("SQA Name");
        sqaName.setPrefWidth(120);
        sqaName.setStyle(tableColumnStyle);
        sqaName.setCellValueFactory(cellData -> cellData.getValue().sqaNameProperty());
        sqaName.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    int row = getIndex();
                    setFont(smallerTitleFont);
                }
            }
        });

        TableColumn<CurrentProjects, String> sqaMsg = new TableColumn<>("SQA Commit Message");
        sqaMsg.setPrefWidth(250);
        sqaMsg.setStyle(tableColumnStyle);
        sqaMsg.setCellValueFactory(cellData -> cellData.getValue().sqaComMsgProperty());
        sqaMsg.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    int row = getIndex();
                    setFont(smallerTitleFont);
                }
            }
        });

        TableColumn<CurrentProjects, String> devName = new TableColumn<>("Developer Name");
        devName.setPrefWidth(150);
        devName.setStyle(tableColumnStyle);
        devName.setCellValueFactory(cellData -> cellData.getValue().devNameProperty());
        devName.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    int row = getIndex();
                    setFont(smallerTitleFont);
                }
            }
        });

        TableColumn<CurrentProjects, String> devMsg = new TableColumn<>("Developer Commit Message");
        devMsg.setPrefWidth(260);
        devMsg.setStyle(tableColumnStyle);
        devMsg.setCellValueFactory(cellData -> cellData.getValue().devComMsgProperty());
        devMsg.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    int row = getIndex();
                    setFont(smallerTitleFont);
                }
            }
        });

        TableColumn<CurrentProjects, String> leadMsg = new TableColumn<>("Teamlead Commit Message");
        leadMsg.setPrefWidth(250);
        leadMsg.setStyle(tableColumnStyle);
        leadMsg.setCellValueFactory(cellData -> cellData.getValue().statusOfTheFeatureProperty());
        leadMsg.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    int row = getIndex();
                    setFont(smallerTitleFont);
                }
            }
        });
        curProjectTable.getColumns().addAll(featureName, sqaName, sqaMsg, devName, devMsg, leadMsg);
        featureName.setStyle("-fx-alignment: CENTER;");
        sqaName.setStyle("-fx-alignment: CENTER;");
        devName.setStyle("-fx-alignment: CENTER;");
        sqaMsg.setStyle("-fx-alignment: CENTER;");
        devMsg.setStyle("-fx-alignment: CENTER;");
        leadMsg.setStyle("-fx-alignment: CENTER;");
        curProjectTable.setLayoutX(20);
        curProjectTable.setLayoutY(300);

        String projectDetailsFetchQuery = "SELECT * FROM Project_TABLE WHERE lead_mail='"+mail+"' AND name='"+project+"'";
        if (project!=null){
            ConnectDB fetch = new ConnectDB();
            try (Connection con = fetch.connect()) {
                Statement projectTableStatement = con.createStatement();
                ResultSet projectTableData = projectTableStatement.executeQuery(projectDetailsFetchQuery);
                ObservableList<CurrentProjects> curData = FXCollections.observableArrayList();
                while (projectTableData.next()){
                    String sqaNameForTable=null;
                    String devNameForTable=null;
                    String sqaQuery = "SELECT name FROM SQA_TABLE WHERE email='"+projectTableData.getString("sqa_mail")+"'";
                    String devQuery = "SELECT name FROM Developer_TABLE WHERE email='"+projectTableData.getString("dev_mail")+"'";
                    Statement devSqaStatement = con.createStatement();
                    ResultSet sqaData = devSqaStatement.executeQuery(sqaQuery);
                    while (sqaData.next()){
                        sqaNameForTable = sqaData.getString("name");
                    }
                    ResultSet devData = devSqaStatement.executeQuery(devQuery);
                    while (devData.next()){
                        devNameForTable = devData.getString("name");
                    }
                    String status = "";
                    if (projectTableData.getString("status").equals("dev")){
                        status = "Under Development";
                    } else if (projectTableData.getString("status").equals("sqa")) {
                        status = "Under Testing";
                    }
                    else if (projectTableData.getString("status").equals("teamlead")){
                        status = "Submitted to Teamlead";
                    }
                    else {
                        status = "Completed";
                    }
                    CurrentProjects temp = new CurrentProjects(projectTableData.getString("feature"),sqaNameForTable,devNameForTable,projectTableData.getString("Sqa_comment"),projectTableData.getString("Dev_comment"),status);
                    curData.add(temp);
                }
                curProjectTable.setItems(curData);
            } catch (Exception e){
//                e.printStackTrace();
                try {
                    throw new CustomException("connect_db");
                } catch (CustomException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        Image logo = new Image("logo.png");

        root.getChildren().addAll(logOutBtn, detailText, nameText, rankText, mailText, phoneText, backBtn, curProjectTable,projectNameTxt);

        Scene scene = new Scene(root);

        stage.getIcons().add(logo);
        stage.setTitle("Project Progress Tracker");
        stage.setScene(scene);
        stage.setHeight(900);
        stage.setWidth(1600);
        stage.setResizable(false);
        stage.show();
    }

    public static class CurrentProjects {
        private final StringProperty featureName, sqaName, devName, sqaComMsg, devComMsg, statusOfTheFeature;
        public CurrentProjects(String featureName,String sqaName, String devName, String sqaComMsg, String devComMsg, String statusOfTheFeature) {
            this.featureName = new SimpleStringProperty(featureName);
            this.sqaName = new SimpleStringProperty(sqaName);
            this.devName = new SimpleStringProperty(devName);
            this.sqaComMsg = new SimpleStringProperty(sqaComMsg);
            this.devComMsg = new SimpleStringProperty(devComMsg);
            this.statusOfTheFeature = new SimpleStringProperty(statusOfTheFeature);
        }
        public String getFeatureName() {
            return featureName.get();
        }
        public void setFeatureName(String featureName) {
            this.featureName.set(featureName);
        }
        public String getSqaName() {
            return sqaName.get();
        }
        public void setSqaName(String sqaName) {
            this.sqaName.set(sqaName);
        }
        public StringProperty featureNameProperty() {
            return featureName;
        }
        public StringProperty sqaNameProperty() {
            return sqaName;
        }
        public String getDevName() {
            return devName.get();
        }
        public void setDevName(String devName) {
            this.devName.set(devName);
        }
        public StringProperty devNameProperty() {
            return devName;
        }
        public String getSqaComMsg() {
            return sqaComMsg.get();
        }
        public void setSqaComMsg(String sqaComMsg) {
            this.sqaComMsg.set(sqaComMsg);
        }
        public StringProperty sqaComMsgProperty() {
            return sqaComMsg;
        }
        public String getDevComMsg() {
            return devComMsg.get();
        }
        public void setDevComMsg(String devComMsg) { this.devComMsg.set(devComMsg); }
        public StringProperty devComMsgProperty() {
            return devComMsg;
        }
        public String getStatusOfTheFeature() {
            return statusOfTheFeature.get();
        }
        public void setStatusOfTheFeature(String statusOfTheFeature) {
            this.statusOfTheFeature.set(statusOfTheFeature);
        }
        public StringProperty statusOfTheFeatureProperty() {
            return statusOfTheFeature;
        }
    }

//    public static class Projects {
//        private final StringProperty feature, completion;
//        public Projects(String feature, String completion) {
//            this.feature = new SimpleStringProperty(feature);
//            this.completion = new SimpleStringProperty(completion);
//        }
//        public String getProjectName() {
//            return feature.get();
//        }
//        public void setProjectName(String feature) {
//            this.feature.set(feature);
//        }
//        public StringProperty projectNameProperty() {
//            return feature;
//        }
//        public String getCompletion() {
//            return completion.get();
//        }
//        public void setCompletion(String completion) {
//            this.completion.set(completion);
//        }
//        public StringProperty completionProperty() {
//            return completion;
//        }
//    }
}
