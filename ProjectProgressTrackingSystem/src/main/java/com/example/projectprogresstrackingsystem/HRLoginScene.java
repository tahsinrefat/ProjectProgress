package com.example.projectprogresstrackingsystem;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

        Text detailText = new Text("Details");
        detailText.setFont(titleFont);
        detailText.setLayoutX(720);
        detailText.setLayoutY(120);

        Text nameText = new Text("Tahsin Ahmed");
        nameText.setFont(anTitleFont);
        nameText.setLayoutX(20);
        nameText.setLayoutY(200);

        Text mailText = new Text(mail);
        mailText.setFont(smallerTitleFont);
        mailText.setLayoutX(20);
        mailText.setLayoutY(225);

        Text phoneText = new Text("01521582006");
        phoneText.setFont(smallerTitleFont);
        phoneText.setLayoutX(20);
        phoneText.setLayoutY(250);

        Button addProjectBtn = new Button("Add New Project");
        addProjectBtn.setFont(normalFont);
        addProjectBtn.setCursor(Cursor.HAND);
        addProjectBtn.setLayoutX(1400);
        addProjectBtn.setLayoutY(210);

        Button deleteProject = new Button("Delete Project");
        deleteProject.setFont(normalFont);
        deleteProject.setCursor(Cursor.HAND);
        deleteProject.setLayoutX(1200);
        deleteProject.setLayoutY(210);

        String tableColumnStyle = "-fx-font-size: 25px; -fx-font-family: 'Ramaraja';";
        TableView<Projects> table = new TableView<>();
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setStyle("-fx-background-color: #1777e6;");
        table.setOnMouseClicked(event -> {
            ObservableList<TablePosition> selectedCells = table.getSelectionModel().getSelectedCells();
            for (TablePosition tablePosition : selectedCells) {
                int row = tablePosition.getRow();
                int col = tablePosition.getColumn();
                String value = table.getColumns().get(col).getCellData(row).toString();
                System.out.println("Selected Cell Value: " + value);
                System.out.println(row + " " + col);
            }
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
        TableColumn<Projects, Boolean> selection = new TableColumn<>("Selection");
        selection.setPrefWidth(150);
        selection.setStyle(tableColumnStyle);
        selection.setCellValueFactory(cellData -> cellData.getValue().selectionProperty());
        selection.setCellFactory(CheckBoxTableCell.forTableColumn(selection));
        table.getColumns().addAll(projectName, completion, selection);
        ObservableList<Projects> data = FXCollections.observableArrayList(
                new Projects("John", "Doe", false),
                new Projects("Jane", "Doe", true),
                new Projects("Bob", "Smith", false)
        );
        table.setItems(data);
        table.setLayoutX(300);
        table.setLayoutY(300);

        Image logo = new Image("logo.png");

        root.getChildren().addAll(logOutBtn, detailText, nameText, mailText, phoneText, addProjectBtn, deleteProject, table);

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
        private final StringProperty projectName, completion;
        private final SimpleBooleanProperty selection;
        public Projects(String projectName, String completion, boolean selection) {
            this.projectName = new SimpleStringProperty(projectName);
            this.completion = new SimpleStringProperty(completion);
            this.selection = new SimpleBooleanProperty(selection);
        }
        public String getProjectName() {
            return projectName.get();
        }
        public void setProjectName(String projectName) {
            this.projectName.set(projectName);
        }
        public StringProperty projectNameProperty() {
            return projectName;
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
        public Boolean getSelection() {
            return selection.get();
        }
        public void setSelection(boolean selection) {
            this.selection.set(selection);
        }
        public SimpleBooleanProperty selectionProperty() {
            return selection;
        }
    }
}
