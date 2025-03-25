package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.util.Date;
import btl.carrentalwebsite.model.Collateral;

public class CollateralDAO extends DAO {
    
    public boolean create(Collateral collateral) throws SQLException{
        String sql = "INSERT INTO collateral (name, price, createDate, detail, status, customerId) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setString(1, collateral.getName());

            if (collateral.getPrice() == 0.0f) {
                stmt.setNull(2, java.sql.Types.FLOAT);
            } else {
                stmt.setFloat(2, collateral.getPrice());
            }

            stmt.setDate(3, new java.sql.Date(collateral.getCreateDate().getTime()));
            stmt.setString(4, collateral.getDetail());
            stmt.setString(5, collateral.getStatus().toString()); 
            stmt.setInt(6, collateral.getCustomerId());

            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e){
            System.out.println("SQLIntegrityException: " + e);
            throw new SQLIntegrityConstraintViolationException("Không tìm thấy khách hàng!");
            
        } catch (SQLException e){
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
            
        }
    }
    
    public boolean isCollateralIdExists(int id){
        String sql = "SELECT id FROM collateral WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.out.println("Database Error!: " + e);
        }
        
        return false;
    }
    
    public boolean updateCollateralAfterEvaluate(int collateralId, float price) throws Exception{
        String sql = "UPDATE collateral SET price = ?, status = 'RECEIVE SUCCESSFULLY' WHERE id = ? AND status = 'WAITING EVALUATE'";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setFloat(1, price);
            stmt.setInt(2, collateralId);

            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isCollateralIdExists(collateralId)) {
                    throw new Exception("Không thể thay đổi giá trị sau khi đánh giá!");
                    
                } else {
                    throw new Exception("Vật thế chấp không tồn tại!");
                }
                
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    public boolean updateCollateralStatusToReturned(int collateralId) throws Exception{
        String sql = "UPDATE collateral SET status = 'RETURN SUCCESSFULLY' WHERE id = ? AND status <> 'SEIZED'";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, collateralId);

            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isCollateralIdExists(collateralId)) {
                    throw new Exception("Vật thế chấp đang bị tịch thu!");
                    
                } else {
                    throw new Exception("Vật thế chấp không tồn tại!");
                }
                
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    public boolean updateCollateralStatusToSeized(int collateralId) throws Exception{
        String sql = "UPDATE collateral SET status = 'SEIZED' WHERE id = ? AND status == 'RECEIVE SUCCESSFULLY'";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, collateralId);

            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isCollateralIdExists(collateralId)) {
                    throw new Exception("Không thể tịch thu khi chưa đánh giá!");
                    
                } else {
                    throw new Exception("Vật thế chấp không tồn tại!");
                }
                
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }

}
