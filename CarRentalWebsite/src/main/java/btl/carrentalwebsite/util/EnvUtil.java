package btl.carrentalwebsite.util;

import jakarta.servlet.ServletContext;
import btl.carrentalwebsite.listener.AppContextListener;

public class EnvUtil {
    public static String get(String key) {
        ServletContext context = AppContextListener.getServletContext();
        if (context != null) {
            return context.getInitParameter(key);
        }
        return null;
    }
}