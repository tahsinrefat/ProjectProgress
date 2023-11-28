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
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TeamleadLoginScene extends SceneController{
    @Override
    public void switchToTeamleadLoginScene(Stage stage, String mail, String name, String phone, String rank, String project) throws CustomException{
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
            try {
                backToLogin.switchToLogSignScene(null, stage);
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
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
            try {
                new TeamleadLoginScene().switchToTeamleadLoginScene(stage,mail, name, phone, rank, project);
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
        });

        Button completeProjectBtn = new Button("Mark Project As Complete");
        completeProjectBtn.setFont(normalFont);
        completeProjectBtn.setStyle("-fx-background-color: red;-fx-text-fill: white;");
        completeProjectBtn.setLayoutX(1180);
        completeProjectBtn.setLayoutY(175);
        completeProjectBtn.setCursor(Cursor.HAND);

        Pair<Integer,Integer>pair = new Pair<>(-1,-1);
        String forProjectName = null;
        if (project==null){
            forProjectName = "Sorry, no projects assigned yet.";
        }
        else {
            pair = completedFeatures(mail,project);
            forProjectName = "Current Project Name: "+project+"("+pair.getValue()+"/"+pair.getKey()+")";
        }
        Text curProjectNameText = new Text(forProjectName);
        curProjectNameText.setFont(anTitleFont);
        curProjectNameText.setLayoutX(20);
        curProjectNameText.setLayoutY(245);

        final int feature = pair.getKey();
        final int completedFeature = pair.getValue();
        completeProjectBtn.setOnAction( completeProjectBtnEvent-> {
            Alert confirmLast = new Alert(Alert.AlertType.CONFIRMATION);
            String alertHead = "Do you want to mark this project as complete?";
            confirmLast.setTitle("Confirm");
            confirmLast.setHeaderText(alertHead);
            confirmLast.showAndWait().ifPresent( result->{
                if (result== ButtonType.OK){
                    if (feature==completedFeature){
                        completeCurrentProject(mail,project);
                        try {
                            new TeamleadLoginScene().switchToTeamleadLoginScene(stage,mail,name,phone,rank,project);
                        } catch (CustomException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
//                        Alert failure = new Alert(Alert.AlertType.ERROR);
//                        failure.setTitle("Failed!");
//                        failure.setHeaderText("Some features still needs to be completed first!");
//                        failure.show();
                        try {
                            throw new CustomException("incompleteFeature");
                        } catch (CustomException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } );
        });

        Text info = new Text("Please click on the features below to edit or assign people.");
        info.setFont(anTitleFont);
        info.setLayoutX(20);
        info.setLayoutY(275);

        Button addFeatureBtn = new Button("Add New Feature");
        addFeatureBtn.setFont(normalFont);
        addFeatureBtn.setLayoutX(1005);
        addFeatureBtn.setLayoutY(240);
        addFeatureBtn.setCursor(Cursor.HAND);
        final String curProject = project;
        addFeatureBtn.setOnAction( addFeatureEvent -> {
            new miniLeadAddFeature().miniAddFeatureEvent(stage, mail, curProject, name, phone, rank, project);
        });

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
                throw new CustomException("connect_db");
            }
        }

        Text completedProjectsText = new Text("Completed Projects:");
        completedProjectsText.setFont(anTitleFont);
        completedProjectsText.setLayoutX(1220);
        completedProjectsText.setLayoutY(275);

        TableView<ComProjects> comProjectTable = new TableView<>();
        comProjectTable.getSelectionModel().setCellSelectionEnabled(true);
        comProjectTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        comProjectTable.setStyle("-fx-background-color: #1777e6;");
        comProjectTable.setOnMouseClicked(event -> {
            ObservableList<TablePosition> selectedCells = comProjectTable.getSelectionModel().getSelectedCells();
            int row = -1;
            int col = -1;
            String value = "";
            for (TablePosition tablePosition : selectedCells) {
                row = tablePosition.getRow();
                col = tablePosition.getColumn();
                value = comProjectTable.getColumns().get(col).getCellData(row).toString();
                System.out.println("Selected Cell Value: " + value);
                System.out.println(row + " " + col);
            }
            System.out.println(value);
//            new HRProjectDetails().switchToProjectDetailsScene(stage, mail, name, phone, rank, value);
        });

        TableColumn<ComProjects, String> comProjectName = new TableColumn<>("Project Title");
        comProjectName.setPrefWidth(350);
        comProjectName.setStyle(tableColumnStyle);
        comProjectName.setCellValueFactory(cellData -> cellData.getValue().comProjectNamesProperty());
        comProjectName.setCellFactory(column -> new TableCell<>() {
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
        comProjectTable.getColumns().addAll(comProjectName);
        comProjectName.setStyle("-fx-alignment: CENTER;");
        comProjectTable.setLayoutX(1220);
        comProjectTable.setLayoutY(300);

        String fetchCompleteProjectsQuery = "SELECT * FROM Project_Record_TABLE WHERE lead='"+mail+"'";
        ConnectDB fetch = new ConnectDB();
        try (Connection con = fetch.connect()) {
            Statement fetchCompleteProjectsStatement = con.createStatement();
            ObservableList<ComProjects> curData = FXCollections.observableArrayList();
            ResultSet completeProjectData = fetchCompleteProjectsStatement.executeQuery(fetchCompleteProjectsQuery);
            while (completeProjectData.next()){
                ComProjects temp = new ComProjects(completeProjectData.getString("name"));
                curData.add(temp);
            }
            comProjectTable.setItems(curData);
        } catch (Exception e){
//            e.printStackTrace();
            throw new CustomException("db_connect");
        }

        Image logo = new Image("logo.png");

        root.getChildren().addAll(logOutBtn, detailText, nameText, rankText, mailText, phoneText,refreshBtn, curProjectNameText, info, curProjectTable, addFeatureBtn, completedProjectsText, comProjectTable,completeProjectBtn);

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
    public static class ComProjects {
        private final StringProperty comProjectNames;
        public ComProjects(String comProjectNames){
            this.comProjectNames = new SimpleStringProperty(comProjectNames);
        }
        public String getComProjectNames() {return comProjectNames.get(); }
        public void setComProjectNames(String comProjectNames) { this.comProjectNames.set(comProjectNames); }
        public StringProperty comProjectNamesProperty() { return comProjectNames; }
    }
    public Pair<Integer, Integer> completedFeatures(String lead_mail, String project){
        ConnectDB fetch = new ConnectDB();
        Pair<Integer, Integer> pair = new Pair<>(-1,-1);
        try (Connection con = fetch.connect()) {
            String findOutQuery = "SELECT * FROM Project_TABLE WHERE lead_mail='"+lead_mail+"' AND name='"+project+"'";
            Statement findOutStatement = con.createStatement();
            ResultSet findOutData = findOutStatement.executeQuery(findOutQuery);
            int feature=0, completed_feature=0;
            while (findOutData.next()){
                feature++;
                if (findOutData.getString("status").equals("complete")){
                    completed_feature++;
                }
            }
            pair = new Pair<>(feature,completed_feature);
        } catch (Exception e){
//            e.printStackTrace();
            try {
                throw new Exception("db_connect");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return pair;
    }
    public void completeCurrentProject(String mail, String project){
        String teamLeadReleaseQuery = "UPDATE Teamlead_TABLE SET project=NULL WHERE email='"+mail+"'";
        String completedProjectRecordQuery = "INSERT INTO Project_Record_TABLE(name,lead) VALUES ('"+project+"','"+mail+"')";
        ConnectDB fetch = new ConnectDB();
        try (Connection con = fetch.connect()) {
            Statement completedProjectRecordStatement = con.createStatement();
            int rowsCompletedProjectRecord = 0;
            rowsCompletedProjectRecord = completedProjectRecordStatement.executeUpdate(completedProjectRecordQuery);
            if (rowsCompletedProjectRecord>0){
                Statement teamLeadReleaseStatement = con.createStatement();
                int rowsTeamLeadRelease = 0;
                rowsTeamLeadRelease = teamLeadReleaseStatement.executeUpdate(teamLeadReleaseQuery);
                if (rowsTeamLeadRelease>0){
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success!");
                    success.setHeaderText("Projection Completion Was A Success!");
                    success.show();
                }
                else {
//                    System.out.println("Completed Table");
//                    Alert failure = new Alert(Alert.AlertType.ERROR);
//                    failure.setTitle("Failed!");
//                    failure.setHeaderText("Project Completion Failed Please Try Again!");
//                    failure.show();
                    throw new CustomException("proCompFail");
                }

            }
            else {
//                System.out.println("Teamlead Release");
//                Alert failure = new Alert(Alert.AlertType.ERROR);
//                failure.setTitle("Failed!");
//                failure.setHeaderText("Project Completion Failed Please Try Again!");
//                failure.show();
                throw new CustomException("proComFail");
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
