package com.example.projectprogresstrackingsystem;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

public class miniHRAddNewProject {
    public void HRAddNewProject(Stage stage, Map<String,String>lead_map, String mail, String name, String phone, String rank){
        Stage mini_hr_add_project_stage = new Stage();
        Pane root = new Pane();

        Font titleFont = new Font("Ramaraja", 30);
        Font normalFont = new Font(20);

        Text pNameText = new Text("Project Name");
        pNameText.setFont(titleFont);
        pNameText.setLayoutX(230);
        pNameText.setLayoutY(100);

        TextField pNameTextField = new TextField();
        pNameTextField.setFont(normalFont);
        pNameTextField.setLayoutX(175);
        pNameTextField.setLayoutY(120);

        ComboBox<String> setLead = new ComboBox<>();
        setLead.getItems().addAll("--Select a Lead--");
        for (Map.Entry<String,String>entry : lead_map.entrySet()) {
            setLead.getItems().add(entry.getValue());
        }
        setLead.setValue("--Select a Lead--");
        setLead.setLayoutX(193);
        setLead.setLayoutY(170);
        setLead.setStyle("-fx-font-size: 18 px;-fx-font-weight: bold;");
        setLead.setCursor(Cursor.HAND);

        Button addProjectBtn = new Button("Add Project");
        addProjectBtn.setFont(normalFont);
        addProjectBtn.setLayoutX(235);
        addProjectBtn.setLayoutY(210);

        addProjectBtn.setOnAction(addProjectEvent -> {
            String projectName = pNameTextField.getText();
            String lead = setLead.getValue();
            String lead_mail="";
            for (Map.Entry<String,String>entry : lead_map.entrySet()) {
                if (entry.getValue().equals(lead)){
                    lead_mail = entry.getKey();
                    break;
                }
            }

            if (lead.equals("--Select a Lead--") || projectName.isEmpty()){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Please don't leave any field blank and select a valid lead!");
                error.showAndWait();
            }
            else {
                String queryLeadTable = "UPDATE Teamlead_TABLE SET project = '"+projectName+"' WHERE email = '"+lead_mail+"'";
                ConnectDB fetch = new ConnectDB();
                try (Connection con = fetch.connect()) {
                    Statement statement = con.createStatement();
                    int rows = statement.executeUpdate(queryLeadTable);
                    if (rows!=0){
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success!");
                        success.setHeaderText("Project Added Successfully!");
                        success.show();
                    }
                    else {
                        Alert success = new Alert(Alert.AlertType.ERROR);
                        success.setTitle("Error!");
                        success.setHeaderText("Failed to add project!");
                        success.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                mini_hr_add_project_stage.close();
                new HRLoginScene().switchToHRLoginScene(stage, mail, name, phone, rank);
            }
        });

        root.getChildren().addAll(pNameText,pNameTextField,setLead,addProjectBtn);

        Scene scene = new Scene(root);

        mini_hr_add_project_stage.setTitle("Add New Project");
        mini_hr_add_project_stage.setScene(scene);
        mini_hr_add_project_stage.setHeight(400);
        mini_hr_add_project_stage.setWidth(600);
        mini_hr_add_project_stage.setResizable(false);
        mini_hr_add_project_stage.show();
    }
}
