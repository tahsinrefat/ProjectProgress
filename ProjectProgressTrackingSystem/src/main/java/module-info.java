module com.example.projectprogresstrackingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.projectprogresstrackingsystem to javafx.fxml;
    exports com.example.projectprogresstrackingsystem;
}