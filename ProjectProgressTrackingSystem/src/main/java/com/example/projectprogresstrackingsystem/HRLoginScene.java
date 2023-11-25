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
import java.util.HashMap;
import java.util.Map;

public class HRLoginScene extends SceneController {
    public void switchToHRLoginScene(Stage stage, String mail, String name, String phone, String rank) {
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

        Text detailText = new Text("Ongoing Projects");
        detailText.setFont(titleFont);
        detailText.setLayoutX(580);
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

        Button addProjectBtn = new Button("Add New Project");
        addProjectBtn.setFont(normalFont);
        addProjectBtn.setCursor(Cursor.HAND);
        addProjectBtn.setLayoutX(1400);
        addProjectBtn.setLayoutY(210);
        addProjectBtn.setOnAction(addProjectEvent -> {
            forAddProject(stage, mail, name, phone, rank);
        });

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setFont(normalFont);
        refreshBtn.setCursor(Cursor.HAND);
        refreshBtn.setLayoutX(1280);
        refreshBtn.setLayoutY(210);
        refreshBtn.setOnAction(refreshEvent -> {
            new HRLoginScene().switchToHRLoginScene(stage,mail, name, phone, rank);
        });

        String tableColumnStyle = "-fx-font-size: 25px; -fx-font-family: 'Ramaraja';";
        TableView<Projects> table = new TableView<>();
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setStyle("-fx-background-color: #1777e6;");
        table.setOnMouseClicked(event -> {
            ObservableList<TablePosition> selectedCells = table.getSelectionModel().getSelectedCells();
            int row = -1;
            int col = -1;
            String value = "";
            for (TablePosition tablePosition : selectedCells) {
                row = tablePosition.getRow();
                col = tablePosition.getColumn();
                value = table.getColumns().get(col).getCellData(row).toString();
                System.out.println("Selected Cell Value: " + value);
                System.out.println(row + " " + col);
            }
            System.out.println(value);
            new HRProjectDetails().switchToProjectDetailsScene(stage, mail, name, phone, rank, value);
        });
        TableColumn<Projects, String> projectName = new TableColumn<>("Project Name");
        projectName.setPrefWidth(600);
        projectName.setStyle(tableColumnStyle);
        projectName.setCellValueFactory(cellData -> cellData.getValue().projectNameProperty());
        projectName.setCellFactory(column -> new TableCell<>() {
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
        TableColumn<Projects, String> projectLeadName = new TableColumn<>("Project Lead");
        projectLeadName.setPrefWidth(250);
        projectLeadName.setStyle(tableColumnStyle);
        projectLeadName.setCellValueFactory(cellData -> cellData.getValue().projectLeadNameProperty());
        projectLeadName.setCellFactory(column -> new TableCell<>() {
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
        TableColumn<Projects, String> completion = new TableColumn<>("Completion");
        completion.setPrefWidth(250);
        completion.setStyle(tableColumnStyle);
        completion.setCellValueFactory(cellData -> cellData.getValue().completionProperty());
        completion.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(smallerTitleFont);
                }
            }
        });
        table.getColumns().addAll(projectName,projectLeadName, completion);
        ConnectDB fetch = new ConnectDB();
        Statement statement;
        String connectQuery = "SELECT * FROM Teamlead_TABLE";
        try (Connection con = fetch.connect()) {
            statement = con.createStatement();
            ResultSet rData = statement.executeQuery(connectQuery);
            ObservableList<Projects> data = FXCollections.observableArrayList();
            while (rData.next()){
                if (rData.getString("project")!=null){
                    Projects temp = new Projects(rData.getString("project"), rData.getString("name"), rData.getString("completion")+" %");
                    data.add(temp);
                }
            }
            table.setItems(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        projectName.setStyle("-fx-alignment: CENTER;");
        projectLeadName.setStyle("-fx-alignment: CENTER;");
        completion.setStyle("-fx-alignment: CENTER;");
        table.setLayoutX(260);
        table.setLayoutY(300);

        Image logo = new Image("logo.png");

        root.getChildren().addAll(logOutBtn, detailText, nameText, rankText, mailText, phoneText, addProjectBtn, refreshBtn, table);

        Scene scene = new Scene(root);

        stage.getIcons().add(logo);
        stage.setTitle("Project Progress Tracker");
        stage.setScene(scene);
        stage.setHeight(900);
        stage.setWidth(1600);
        stage.setResizable(false);
        stage.show();
    }

    public static class Projects {
        private final StringProperty projectName, projectLeadName, completion;
        public Projects(String projectName,String projectLeadName, String completion) {
            this.projectName = new SimpleStringProperty(projectName);
            this.projectLeadName = new SimpleStringProperty(projectLeadName);
            this.completion = new SimpleStringProperty(completion);
        }
        public String getProjectName() {
            return projectName.get();
        }
        public void setProjectName(String projectName) {
            this.projectName.set(projectName);
        }
        public String getProjectLeadName() {
            return projectLeadName.get();
        }
        public void setProjectLeadName(String projectLeadName) {
            this.projectLeadName.set(projectLeadName);
        }
        public StringProperty projectNameProperty() {
            return projectName;
        }
        public StringProperty projectLeadNameProperty() {
            return projectLeadName;
        }
        public String getCompletion() {
            return completion.get();
        }
        public void setCompletion(String completion) {
            this.completion.set(completion);
        }
        public StringProperty completionProperty() {
            return completion;
        }
    }
    public void forAddProject(Stage primaryStage, String mail, String name, String phone, String rank){
        String query = "SELECT * from Teamlead_TABLE";
        Map<String, String> lead_map = new HashMap<>();
        ConnectDB fetch = new ConnectDB();
        try (Connection con = fetch.connect()) {
            Statement statement = con.createStatement();
            ResultSet data = statement.executeQuery(query);
            while (data.next()){
                if (data.getString("project")==null && !data.getString("password").equals("N/A")){
                    lead_map.put(data.getString("email"),data.getString("name"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        new miniHRAddNewProject().HRAddNewProject(primaryStage,lead_map,mail,name,phone,rank);
    }
}
