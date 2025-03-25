package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import btl.carrentalwebsite.model.BrokenReportImage;
import java.sql.ResultSet;

public class BrokenReportImageDAO extends DAO {
    
    public boolean createBrokenReportImage(BrokenReportImage image) throws SQLException{
        
        String sql = "INSERT INTO broken_report_image (link, detail, brokenReportId) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setString(1, image.getLink());
            stmt.setString(2, image.getDetail());
            stmt.setInt(3, image.getBrokenReportId());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLIntegrityConstraintViolationException  e) {
            System.out.println("SQLIntegrityException: " + e);
            throw new SQLIntegrityConstraintViolationException("Broken Report không tồn tại!");
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }

}
