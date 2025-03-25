package btl.carrentalwebsite.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import btl.carrentalwebsite.model.Video;

public class VideoDAO extends DAO {
    
    public boolean create(Video video) throws SQLException {
        String sql = "INSERT INTO video (link, detail, carId) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, video.getLink());
            stmt.setString(2, video.getDetail());
            stmt.setInt(3, video.getCarId());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLIntegrityConstraintViolationException e){
            System.out.println("SQLIntegrityException: " + e);
            throw new SQLIntegrityConstraintViolationException("Không tìm thấy CarId!");
            
        } catch (SQLException e){
            System.out.println("Database Error: " + e.getMessage());
            throw new SQLException(e);
            
        }
    }
    
}
