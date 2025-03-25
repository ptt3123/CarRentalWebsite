package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import btl.carrentalwebsite.model.BrokenReport;
import java.sql.ResultSet;

public class BrokenReportDAO extends DAO {
    
    public boolean createBrokenReport(BrokenReport brokenReport) throws SQLException{
        
        String sql = "INSERT INTO broken_report (detail, price, carId) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setString(1, brokenReport.getDetail());
            stmt.setFloat(2, brokenReport.getPrice());
            stmt.setInt(3, brokenReport.getCarId());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLIntegrityConstraintViolationException  e) {
            System.out.println("SQLIntegrityException: " + e);
            throw new SQLIntegrityConstraintViolationException("Car không tồn tại!");
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
}
