package com.example.projectprogresstrackingsystem;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HRLoginScene extends SceneController{
    public void switchToHRLoginScene(Stage stage, String mail){
        Pane root = new Pane();

        Image logo = new Image("logo.png");

        root.getChildren().addAll();

        Scene scene = new Scene(root);

        stage.getIcons().add(logo);
        stage.setTitle("Project Progress Tracker");
        stage.setScene(scene);
        stage.setHeight(900);
        stage.setWidth(1600);
        stage.setResizable(false);
        stage.show();
    }
}
