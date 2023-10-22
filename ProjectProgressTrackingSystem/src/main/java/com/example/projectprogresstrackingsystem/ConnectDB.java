package com.example.projectprogresstrackingsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    Connection con;
    String databaseName = "sql12653916";
    String databaseUser = "sql12653916";
    String databasePass = "hi1zCVC4Ec";
    String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/"+databaseName;
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
//Host: sql12.freesqldatabase.com
//Database name: sql12653916
//Database user: sql12653916
//Database password: hi1zCVC4Ec
//Port number: 3306
