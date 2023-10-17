package com.example.projectprogresstrackingsystem;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ForgotPass extends SceneController{
    public void switchToForgotPassScene(Stage stage){
        Pane root = new Pane();

        Image welcome_logo = new Image("logo.png");
        ImageView welcome_logo_view = new ImageView(welcome_logo);
        welcome_logo_view.setFitHeight(220);
        welcome_logo_view.setFitWidth(550);
        welcome_logo_view.setLayoutX(540);
        welcome_logo_view.setLayoutY(15);

        Font titleFont = new Font("Ramaraja",30);

        Text emailText = new Text("Please Provide Your Email");
        emailText.setFont(titleFont);
        emailText.setLayoutX(600);
        emailText.setLayoutY(250);

        root.getChildren().addAll(welcome_logo_view, emailText);

        Scene scene = new Scene(root);

        Image logo = new Image("logo.png");

        stage.getIcons().add(logo);
        stage.setTitle("Project Progress Tracker");
        stage.setScene(scene);
        stage.setHeight(900);
        stage.setWidth(1600);
        stage.setResizable(false);
        stage.show();
    }
}
