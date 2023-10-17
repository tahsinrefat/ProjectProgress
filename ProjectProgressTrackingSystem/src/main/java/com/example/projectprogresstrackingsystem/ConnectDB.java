package com.example.projectprogresstrackingsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    Connection con;
    String databaseName = "ProjectProgresDB";
    String databaseUser = "root";
    String databasePass = "password";
    String url = "jdbc:mysql://localhost:3306/"+databaseName;
    public Connection connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,databaseUser,databasePass);
            System.out.println("Connection Established Successfully!");

        } catch (Exception e){
            e.printStackTrace();
        }
        return con;
    }
}
