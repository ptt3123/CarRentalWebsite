package btl.carrentalwebsite.dao;

import btl.carrentalwebsite.util.PasswordUtil;
import btl.carrentalwebsite.model.User;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;

public class UserDAO extends DAO{
    
    public boolean create(User user) throws Exception{
        boolean res = false;
        
        String sql = "INSERT INTO user (username, password, name, familyName, phoneNumber, address, avatar, dateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, PasswordUtil.hashPassword(user.getPassword())); // Mã hóa mật khẩu
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getFamilyName());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getAvatar());
            stmt.setDate(8, new java.sql.Date(user.getDateOfBirth().getTime()));

            if (stmt.executeUpdate() > 0) {
                res = true;
            }
            
        } catch (SQLIntegrityConstraintViolationException e) {
            String err = "";
            if(this.isUsernameExists(user.getUsername())){
                err += "Username, ";
            }
            if(this.isPhoneNumberExists(user.getPhoneNumber())){
                err += "PhoneNumber, ";
            }
            if(err.endsWith(", ")){
                err = err.substring(0, err.length() - 2) + " đã tồn tại!";
            }
            throw new SQLIntegrityConstraintViolationException(err);
            
        } catch (SQLException e) {
            System.out.println("Lỗi khi truy vấn CSDL!: " + e);
            throw new SQLException("Lỗi khi truy vấn CSDL!");
        } 
        
        return res;
    }
    
    public boolean isUsernameExists(String username) {
        String sql = "SELECT id FROM user WHERE username = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả, username đã tồn tại
            
        } catch (SQLException e) {
            System.out.println("Lỗi khi truy vấn CSDL!: " + e);
            return false;
            
        }
    }
    
    public boolean isPhoneNumberExists(String phonenumber) {
        String sql = "SELECT id FROM user WHERE phoneNumber = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, phonenumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả, phoneNumber đã tồn tại
            
        } catch (SQLException e) {
            System.out.println("Lỗi khi truy vấn CSDL!: " + e);
            return false;
            
        }
    }
    
}
