package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import btl.carrentalwebsite.model.Bill;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Date;

public class BillDAO extends DAO {
    
    public boolean createBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO Bill (detail, createDate, rentalPrice, lateFee, repairFee, totalPrice, paymentDate, rentalOrderId, staffId, customerId) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, bill.getDetail());
            stmt.setDate(2, new Date(bill.getCreateDate().getTime()));
            stmt.setFloat(3, bill.getRentalPrice());
            stmt.setFloat(4, bill.getLateFee());
            stmt.setFloat(5, bill.getRepairFee());
            stmt.setFloat(6, bill.getTotalPrice());

            
            if (bill.getPaymentDate() != null) {
                stmt.setDate(7, new Date(bill.getPaymentDate().getTime()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            stmt.setInt(8, bill.getRentalOrderId());
            stmt.setInt(9, bill.getStaffId());
            stmt.setInt(10, bill.getCustomerId());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLIntegrityConstraintViolationException  e) {
            System.out.println("SQLIntegrityException: " + e);
            throw new SQLIntegrityConstraintViolationException();
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    public boolean updatePaymentDate(int billId, Date paymentDate) throws Exception{
        String sql = "UPDATE Bill SET paymentDate = ? WHERE id = ? AND paymentDate IS NULL";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setDate(1, new Date(paymentDate.getTime()));
            stmt.setInt(2, billId);

            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isBillIdExists(billId)) {
                    throw new Exception("Không thể thay đổi ngày xác nhận thanh toán!");
                    
                } else {
                    throw new Exception("Bill không tồn tại!");
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    public boolean isBillIdExists(int id){
        String sql = "SELECT id FROM bill WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.out.println("Database Error!: " + e);
        }
        
        return false;
    }

}
