package btl.carrentalwebsite.listener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.sql.Driver;

public class AppContextListener implements ServletContextListener {
    
    private static ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Ứng dụng đang khởi động ...");
        
        context = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Ứng dụng đang shutdown...");
        
        context = null;
        
        try {
            // Dừng thread dọn dẹp kết nối MySQL
            AbandonedConnectionCleanupThread.checkedShutdown();

            // Đóng driver MySQL còn tồn tại
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                    System.out.println("Đã gỡ bỏ driver: " + driver);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static ServletContext getServletContext() {
        return context;
    }
    
}
