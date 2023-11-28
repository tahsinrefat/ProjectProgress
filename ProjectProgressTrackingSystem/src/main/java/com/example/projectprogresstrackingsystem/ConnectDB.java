package com.example.projectprogresstrackingsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    Connection con;
    String databaseName = "sql12662458";
    String databaseUser = "sql12662458";
    String databasePass = "RU2m8qnffz";
    String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/"+databaseName;
    public Connection connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,databaseUser,databasePass);
            System.out.println("Connection Established Successfully!");

        } catch (Exception e){
//            e.printStackTrace();
            try {
                throw new CustomException("connect_db");
            } catch (CustomException ex) {
                throw new RuntimeException(ex);
            }
        }
        return con;
    }
}
//Host: sql12.freesqldatabase.com
//Database name: sql12653916
//Database user: sql12653916
//Database password: hi1zCVC4Ec
//Port number: 3306
