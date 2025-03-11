package btl.carrentalwebsite.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvUtil {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("src/main/resources") // Thư mục chứa .env
            .ignoreIfMissing() // Không lỗi nếu thiếu file .env
            .load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}