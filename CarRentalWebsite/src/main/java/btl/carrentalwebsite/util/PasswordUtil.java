package btl.carrentalwebsite.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Hàm mã hóa mật khẩu
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 là độ mạnh của salt
    }

    // Hàm kiểm tra mật khẩu khi đăng nhập
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
