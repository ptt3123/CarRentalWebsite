package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
    
    public List<Partner> readAllPartner(int page, int pageSize) throws SQLException{
        List<Partner> partners = new ArrayList<>();
        String sql = "SELECT * FROM partner LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Partner partner = new Partner();
                    partner.setId(rs.getInt("id"));
                    partner.setName(rs.getString("name"));
                    partner.setDetail(rs.getString("detail"));
                    partners.add(partner);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Error!: " + e);
            throw new SQLException(e);
        }
        
        return partners;
    }
    
    public boolean update(Partner partner) throws SQLException{
        String sql = "UPDATE partner SET name = ?, detail = ? WHERE id = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setString(1, partner.getName());
            stmt.setString(2, partner.getDetail());
            stmt.setInt(3, partner.getId());

            return stmt.executeUpdate() > 0; 
            
        } catch (SQLException e) {
            System.out.println("Database Error!: " + e);
            throw new SQLException(e);
        }
    }
    
    public boolean delete(int partnerId) throws SQLException{
        String sql = "DELETE FROM partner WHERE id = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setInt(1, partnerId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Database Error!: " + e);
            throw new SQLException(e);
        }
    }
    
}
