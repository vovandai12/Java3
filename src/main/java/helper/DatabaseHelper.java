/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

/**
 *
 * @author ACER
 */
public class DatabaseHelper {
    
//    public static void main(String[] args) throws Exception {
//        Connection conn = openConnection();
//            if (conn != null) {
//                System.out.println("Connected");
//                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
//                System.out.println("Driver name: " + dm.getDriverName());
//                System.out.println("Driver version: " + dm.getDriverVersion());
//                System.out.println("Product name: " + dm.getDatabaseProductName());
//                System.out.println("Product version: " + dm.getDatabaseProductVersion());
//            }
//    }
    
    public static Connection openConnection() throws Exception{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost;database=AssignmentJava3";
        String username = "*****";
        String password = "*****";
        Connection conn = DriverManager.getConnection(connectionUrl, username, password);
        return conn;
    }
}
