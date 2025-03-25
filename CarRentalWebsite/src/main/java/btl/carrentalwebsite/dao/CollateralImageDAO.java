package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import btl.carrentalwebsite.model.CollateralImage;
import java.sql.ResultSet;

public class CollateralImageDAO extends DAO {
    
    public boolean create(CollateralImage image) throws SQLException{
        
        String sql = "INSERT INTO collateral_image (link, detail, collateralId) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setString(1, image.getLink());
            stmt.setString(2, image.getDetail());
            stmt.setInt(3, image.getCollateralId());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLIntegrityConstraintViolationException  e) {
            System.out.println("SQLIntegrityException: " + e);
            throw new SQLIntegrityConstraintViolationException("Vật thế chấp không tồn tại!");
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
}
