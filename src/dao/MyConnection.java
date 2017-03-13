/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Melody
 */
class MyConnection {
    
    private static Connection conn;
    
    public static Connection getConnection() {
        if (conn != null) {
            return conn;
        }
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            String host = "localhost";
            String port = "1521";
            String dbName = "xe"; //"coen280";
            String userName = "temp";
            String password = "temp";

            // Construct the JDBC URL 
            String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
            conn = DriverManager.getConnection(dbURL, userName, password);
            Logger.getLogger(MovieDao.class.getName()).log(Level.INFO, "DB connected successfully");
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(MyConnection.class.getName()).log(Level.SEVERE, "Can not connect to DB", ex);
            throw new RuntimeException(ex);
        }

    }
}
