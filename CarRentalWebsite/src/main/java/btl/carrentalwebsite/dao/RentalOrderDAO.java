package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import btl.carrentalwebsite.model.RentalOrder;
import btl.carrentalwebsite.model.RentalOrderStatus;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class RentalOrderDAO extends DAO {
    
    public boolean create(RentalOrder order) throws SQLException {
        String sql = "INSERT INTO rental_order (createDate, rentalBeginDate, rentalEndDate, price, status, " +
                 "carId, brokenReportId, staffId, customerId, collateralId) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setDate(1, new Date(order.getCreateDate().getTime()));
            stmt.setDate(2, new Date(order.getRentalBeginDate().getTime()));
            stmt.setDate(3, new Date(order.getRentalEndDate().getTime()));
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
    
    public boolean isRentalOrderIdExists(int rentalOrderId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rental_order WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rentalOrderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
        
        return false;
    }

    
    public boolean updateStatusToWaitingVerifyCollateral(int rentalOrderId) throws Exception {
        String sql = "UPDATE rental_order SET status = 'WAITING VERIFY COLLATERAL' WHERE id = ? AND status = 'WAITING VERIFY ORDER'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rentalOrderId);
            
            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isRentalOrderIdExists(rentalOrderId)) {
                    throw new Exception("Trạng thái Order phải là chờ được verify!");
                    
                } else {
                    throw new Exception("Order không tồn tại!");
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    public boolean updateStatusToVerifySuccessfully(int rentalOrderId) throws Exception {
        String sql = "UPDATE rental_order SET status = 'VERIFY SUCCESSFULLY' WHERE id = ? AND status = 'WAITING VERIFY COLLATERAL'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rentalOrderId);
            
            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isRentalOrderIdExists(rentalOrderId)) {
                    throw new Exception("Trạng thái Order phải được xác nhận vật thế chấp!");
                    
                } else {
                    throw new Exception("Order không tồn tại!");
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    
    public boolean updateStatusToBegin(int rentalOrderId, Date receiveDate) throws Exception {
        String sql = "UPDATE rental_order SET status = 'BEGIN', receiveDate = ? WHERE id = ? AND status = 'VERIFY SUCCESSFULLY'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, receiveDate);
            stmt.setInt(2, rentalOrderId);
            
            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isRentalOrderIdExists(rentalOrderId)) {
                    throw new Exception("Trạng thái Order phải được xác nhận thành công!");
                    
                } else {
                    throw new Exception("Order không tồn tại!");
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    public boolean updateStatusToEnd(int rentalOrderId, Date returnDate) throws Exception {
        String sql = "UPDATE rental_order SET status = 'END', returnDate = ? WHERE id = ? AND status = 'BEGIN'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, returnDate);
            stmt.setInt(2, rentalOrderId);
            
            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isRentalOrderIdExists(rentalOrderId)) {
                    throw new Exception("Trạng thái Order phải được bắt đầu!");
                    
                } else {
                    throw new Exception("Order không tồn tại!");
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    public List<RentalOrder> readRentalOrdersByStatus(String status, int page, int pageSize) throws SQLException {
        List<RentalOrder> rentalOrders = new ArrayList<>();
        String sql = "SELECT * FROM rental_order WHERE status = ? ORDER BY rentalBeginDate DESC LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rentalOrders.add(mapResultSetToRentalOrder(rs));
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
        return rentalOrders;
    }
    public List<RentalOrder> readOrdersByCustomerId(int customerId, int page, int pageSize) throws SQLException {
        List<RentalOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM rental_order " +
                "WHERE customerId = ? " +
                "ORDER BY FIELD(status, 'WAITING VERIFY ORDER', 'WAITING VERIFY COLLATERAL', " +
                "'VERIFY SUCCESSFULLY', 'BEGIN', 'END'), " +
                "rentalBeginDate DESC " +
                "LIMIT ? OFFSET ?;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, pageSize);
            stmt.setInt(3, (page - 1) * pageSize);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToRentalOrder(rs));
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
        return orders;
    }

    private RentalOrder mapResultSetToRentalOrder(ResultSet rs) throws SQLException {
        RentalOrder rentalOrder = new RentalOrder();
        
        rentalOrder.setId(rs.getInt("id"));
        rentalOrder.setCreateDate(rs.getDate("createDate"));
        rentalOrder.setRentalBeginDate(rs.getDate("rentalBeginDate"));
        rentalOrder.setRentalEndDate(rs.getDate("rentalEndDate"));
        rentalOrder.setPrice(rs.getDouble("price"));
        rentalOrder.setStatus(RentalOrderStatus.valueOf(rs.getString("status")));
        rentalOrder.setReceiveDate(rs.getDate("receiveDate") != null ? rs.getDate("receiveDate") : null);
        rentalOrder.setReturnDate(rs.getDate("returnDate") != null ? rs.getDate("returnDate") : null);
        rentalOrder.setCarId(rs.getInt("carId"));
        rentalOrder.setBrokenReportId(rs.getInt("brokenReportId"));
        rentalOrder.setStaffId(rs.getInt("staffId"));
        rentalOrder.setCustomerId(rs.getInt("customerId"));
        rentalOrder.setCollateralId(rs.getInt("collateralId"));

        return rentalOrder;
    }

}
