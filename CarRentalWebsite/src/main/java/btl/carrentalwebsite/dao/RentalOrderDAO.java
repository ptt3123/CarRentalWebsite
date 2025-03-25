package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import btl.carrentalwebsite.model.RentalOrder;
import java.sql.ResultSet;
import java.sql.Types;

public class RentalOrderDAO extends DAO {
    
    public boolean create(RentalOrder order) throws SQLException {
        String sql = "INSERT INTO rental_order (createDate, rentalBeginDate, rentalEndDate, price, status, " +
                 "carId, brokenReportId, staffId, customerId, collateralId) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(order.getCreateDate().getTime()));
            stmt.setDate(2, new java.sql.Date(order.getRentalBeginDate().getTime()));
            stmt.setDate(3, new java.sql.Date(order.getRentalEndDate().getTime()));
            stmt.setDouble(4, order.getPrice());
            stmt.setString(5, order.getStatus().toString());
            stmt.setInt(6, order.getCarId());

            // Nếu `brokenReportId` hoặc `collateralId` là null, set thành NULL trong SQL
            int brokenReportId = order.getBrokenReportId();
            if (brokenReportId != 0) {
                stmt.setInt(7, brokenReportId);
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setInt(8, order.getStaffId());
            stmt.setInt(9, order.getCustomerId());
            
            int collateralId = order.getCollateralId();
            if (collateralId != 0) {
                stmt.setInt(10, collateralId);
            } else {
                stmt.setNull(10, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;
            
        } catch (SQLIntegrityConstraintViolationException  e) {
            System.out.println("SQLIntegrityException: " + e);
            throw new SQLIntegrityConstraintViolationException();
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
}
