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
    public void switchToProjectDetailsScene(Stage stage, String mail, String name, String phone, String rank, String project_name){
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
            backToLogin.switchToLogSignScene(null, stage);
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

        String tableColumnStyle = "-fx-font-size: 25px; -fx-font-family: 'Ramaraja';";
        TableView<HRLoginScene.Projects> table = new TableView<>();
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setStyle("-fx-background-color: #1777e6;");
        table.setOnMouseClicked(event -> {
            ObservableList<TablePosition> selectedCells = table.getSelectionModel().getSelectedCells();
            int row = -1;
            int col = -1;
            for (TablePosition tablePosition : selectedCells) {
                row = tablePosition.getRow();
                col = tablePosition.getColumn();
                String value = table.getColumns().get(col).getCellData(row).toString();
                System.out.println("Selected Cell Value: " + value);
                System.out.println(row + " " + col);
            }


        });
        TableColumn<HRLoginScene.Projects, String> feature = new TableColumn<>("Features");
        feature.setPrefWidth(600);
        feature.setStyle(tableColumnStyle);
        feature.setCellValueFactory(cellData -> cellData.getValue().projectNameProperty());
        feature.setCellFactory(column -> new TableCell<>() {
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
        TableColumn<HRLoginScene.Projects, String> completion = new TableColumn<>("Completed");
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
        table.getColumns().addAll(feature, completion);
        ConnectDB fetch = new ConnectDB();
        Statement statement;
        String connectQuery = "SELECT Feature FROM Project_TABLE WHERE name='"+project_name+"'";
        try (Connection con = fetch.connect()) {
            statement = con.createStatement();
            ResultSet rData = statement.executeQuery(connectQuery);
            ObservableList<HRLoginScene.Projects> data = FXCollections.observableArrayList();
            while (rData.next()){
                //HRLoginScene.Projects temp = new HRLoginScene.Projects(rData.getString("feature"),"YES");
                //data.add(temp);
            }
            table.setItems(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        feature.setStyle("-fx-alignment: CENTER;");
        completion.setStyle("-fx-alignment: CENTER;");
        table.setLayoutX(380);
        table.setLayoutY(300);


        Image logo = new Image("logo.png");

        root.getChildren().addAll(logOutBtn, detailText, nameText, rankText, mailText, phoneText, backBtn, table);

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
        private final StringProperty feature, completion;
        public Projects(String feature, String completion) {
            this.feature = new SimpleStringProperty(feature);
            this.completion = new SimpleStringProperty(completion);
        }
        public String getProjectName() {
            return feature.get();
        }
        public void setProjectName(String feature) {
            this.feature.set(feature);
        }
        public StringProperty projectNameProperty() {
            return feature;
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
}
