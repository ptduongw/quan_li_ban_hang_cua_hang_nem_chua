package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/shop_management";
    
    private static final String USER = "root";
    
    private static final String PASS = "611111"; 
   
    public static Connection getConnection() {
        try {
            //Nạp driver của MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            return conn;
        } catch (ClassNotFoundException ex) {
            System.err.println("Lỗi: Không tìm thấy MySQL JDBC Driver!");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Lỗi: Kết nối tới cơ sở dữ liệu thất bại!");
            ex.printStackTrace();
        }
        return null;
    }
}

