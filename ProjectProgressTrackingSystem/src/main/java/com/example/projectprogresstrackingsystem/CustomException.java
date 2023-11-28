package com.example.projectprogresstrackingsystem;

import javafx.scene.control.Alert;

public class CustomException extends Exception{
    public CustomException(String message){
        if (message.equals("db_connect")){
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Connection Failed");
            db_connect.setHeaderText("Failed to connect to database, please try again.");
            db_connect.show();
        } else if (message.equals("invalidInput")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Invalid Input");
            db_connect.setHeaderText("At least One of the Inputs Are Invalid!");
            db_connect.show();
        } else if (message.equals("wrongCreds")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Wrong Credentials");
            db_connect.setHeaderText("Your Credentials doesn't match!");
            db_connect.show();
        } else if (message.equals("noMail")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Invalid Email");
            db_connect.setHeaderText("Email Not Found!");
            db_connect.show();
        } else if (message.equals("passNoChange")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Couldn't Change Password!");
            db_connect.show();
        } else if (message.equals("passNoMatch")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Passwords Doesn't Match!");
            db_connect.show();
        } else if (message.equals("proRecordFailed")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Progress Record Failed Please Try Again!");
            db_connect.show();
        } else if (message.equals("incompleteFeature")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Some features are yet to be implemented!");
            db_connect.show();
        } else if (message.equals("proCompFail")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Project Completion Failed!");
            db_connect.show();
        } else if (message.equals("notDeved")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Not Developed Yet!");
            db_connect.show();
        } else if (message.equals("notTested")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Not Tested Yet!");
            db_connect.show();
        } else if (message.equals("invalidComment")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Please provide a valid comment!");
            db_connect.show();
        } else if (message.equals("noDevSqaChange")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Please Change Developer or SQA First To Reassign!");
            db_connect.show();
        } else if (message.equals("reassignFail")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Reassigning Failed!");
            db_connect.show();
        } else if (message.equals("proFeatureFail")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Feature Completion Failed!!");
            db_connect.show();
        } else if (message.equals("addProjectFail")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Add Project Failed!");
            db_connect.show();
        } else if (message.equals("revRecordFailed")) {
            Alert db_connect = new Alert(Alert.AlertType.ERROR);
            db_connect.setTitle("Error");
            db_connect.setHeaderText("Review Record Failed Please Try Again!");
            db_connect.show();
        }
    }

}
