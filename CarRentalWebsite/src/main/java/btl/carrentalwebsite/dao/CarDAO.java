package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import btl.carrentalwebsite.model.Car;
import java.sql.ResultSet;

public class CarDAO extends DAO{
    
    public boolean create(Car car) throws SQLException{
        
        String sql = "INSERT INTO car (name, avatar, brand, type, licensePlate, detail, price, partnerRentalPricePerMonth, rentalPricePerDay, status, partnerId, contractId) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, car.getName());
            stmt.setString(2, car.getAvatar());
            stmt.setString(3, car.getBrand());
            stmt.setString(4, car.getType().toString());
            stmt.setString(5, car.getLicensePlate());
            stmt.setString(6, car.getDetail());
            stmt.setFloat(7, car.getPrice());
            stmt.setFloat(8, car.getPartnerRentalPricePerMonth());
            stmt.setFloat(9, car.getRentalPricePerDay());
            stmt.setString(10, car.getStatus().toString());
            stmt.setInt(11, car.getPartnerId());
            stmt.setInt(11, car.getContractId());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLIntegrityConstraintViolationException  e) {
            System.out.println("SQLIntegrityException: " + e);
            String err = "";
            if (this.isLicensePlateExists(car.getLicensePlate())){
                err += "Biển số xe đã tồn tại. Vui lòng kiếm tra!";
            }
            throw new SQLIntegrityConstraintViolationException(err);
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    private boolean isLicensePlateExists(String licensePlate){
        String sql = "SELECT id FROM car WHERE licensePlate = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả, licensePlate đã tồn tại
            
        } catch (SQLException e) {
            System.out.println("Database Error!: " + e);
        }
        
        return false;
    }
    
    public boolean isCarIdExists(int id){
        String sql = "SELECT id FROM car WHERE id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.out.println("Database Error!: " + e);
        }
        
        return false;
    }
    
    public boolean updateCarStatusToOnRenting(int carId) throws Exception{
        
        String sql = "UPDATE car SET status = 'ON RENTING' WHERE id = ? AND status = 'FREE'";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            
            stmt.setInt(1, carId);

            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isCarIdExists(carId)) {
                    throw new Exception("Xe đang trong trạng thái không thể thuê!");
                    
                } else {
                    throw new Exception("Xe không tồn tại!");
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    public boolean updateCarStatusToOnRepairing(int carId) throws Exception{
        
        String sql = "UPDATE car SET status = 'ON REPAIRING' WHERE id = ? AND status <> 'RETURNED'";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            
            stmt.setInt(1, carId);

            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isCarIdExists(carId)) {
                    throw new Exception("Xe đã được trả về cho đối tác!");
                    
                } else {
                    throw new Exception("Xe không tồn tại!");
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }
    
    public boolean updateCarStatusToReturned(int carId) throws Exception{
        
        String sql = "UPDATE car SET status = 'RETURNED' WHERE id = ? AND status = 'FREE'";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            
            stmt.setInt(1, carId);

            if (stmt.executeUpdate() > 0){
                return true;
                
            } else {
                
                if (isCarIdExists(carId)) {
                    throw new Exception("Xe đag trong trạng thái ko thể hoàn trả!");
                    
                } else {
                    throw new Exception("Xe không tồn tại!");
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
        }
    }

}
