package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import btl.carrentalwebsite.model.Partner;

public class PartnerDAO extends DAO{
    
    public boolean create(Partner partner) throws SQLException{
        
        String sql = "INSERT INTO partner (name, detail) VALUES (?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, partner.getName());
            stmt.setString(2, partner.getDetail());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLIntegrityConstraintViolationException e){
            System.out.println("SQLIntegrityException: " + e);
            throw new SQLIntegrityConstraintViolationException("Tên của đối tác đã tồn tại!");
            
        } catch (SQLException e){
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
            
        }
    }
    
    public boolean isPartnerIdExists(int partnerId){
        String sql = "SELECT id FROM partner WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, partnerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả, partnerId có tồn tại
            
        } catch (SQLException e) {
            System.out.println("Database Error!: " + e);
        }
        
        return false;
    }
}
