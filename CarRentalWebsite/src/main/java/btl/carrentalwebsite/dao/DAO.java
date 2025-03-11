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
            throw new RuntimeException("Không tìm thấy biến môi trường!");
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công!");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver không tìm thấy!", e);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kết nối database!", e);
        }
    }
    
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Kết nối đã đóng!!!!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Đã xảy ra lỗi khi đóng Connection!", e);
        }
    } 
}
