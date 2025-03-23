package btl.carrentalwebsite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import btl.carrentalwebsite.util.EnvUtil;

public class DAO {
    protected Connection connection;
    
    public DAO() {
        String URL = EnvUtil.get("DB_URL");
        String USER = EnvUtil.get("DB_USER");
        String PASSWORD = EnvUtil.get("DB_PASSWORD");
        
        if (URL == null || USER == null || PASSWORD == null) {
            System.out.println("⚠ Không tìm thấy biến môi trường!");
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver không tìm thấy!: " + e);
            
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối database!: " + e);
            
        }
    }
    
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Kết nối đã đóng!!!!");
            }
            
        } catch (SQLException e) {
            System.out.println("Đã xảy ra lỗi khi đóng Connection!: " + e);
            
        }
    } 
}
