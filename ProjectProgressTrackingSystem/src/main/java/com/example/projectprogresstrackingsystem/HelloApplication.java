package com.example.projectprogresstrackingsystem;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        Text welcome = new Text("Welcome to Project Progress Tracker!");
        Font font = Font.font("Ramaraja",50);
        welcome.layoutXProperty().bind(root.widthProperty().subtract(welcome.getBoundsInLocal().getWidth()).divide(2).subtract(210));
        welcome.layoutYProperty().bind(root.heightProperty().subtract(welcome.getBoundsInLocal().getHeight()).divide(2));
        welcome.setFont(font);

        Image logo = new Image("logo.png");

        Image welcome_logo = new Image("logo.png");
        ImageView welcome_logo_view = new ImageView(welcome_logo);
        welcome_logo_view.setFitHeight(250);
        welcome_logo_view.setFitWidth(550);
        welcome_logo_view.setLayoutX(570);
        welcome_logo_view.setLayoutY(150);

        ProgressBar progress = new ProgressBar(-1);
        progress.setLayoutX(570);
        progress.setLayoutY(475);
        progress.setPrefHeight(35);
        progress.setPrefWidth(500);

        root.getChildren().add(welcome);
        root.getChildren().add(welcome_logo_view);
        root.getChildren().add(progress);
        Scene scene = new Scene(root);
        stage.getIcons().add(logo);
        stage.setTitle("Project Progress Tracker");
        stage.setScene(scene);
        stage.setHeight(900);
        stage.setWidth(1600);
        stage.setResizable(false);
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            try{
                LoginSignScene toLogin = new LoginSignScene();
                toLogin.switchToLogSignScene(null, stage);
//                ChangePassword changePassword = new ChangePassword();
//                changePassword.switchToChangePass(stage);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        pause.play();
    }

    public static void main(String[] args) {
        launch();
    }
}