package btl.carrentalwebsite.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.sql.SQLException;
import java.util.Set;
import btl.carrentalwebsite.model.User;
import btl.carrentalwebsite.dao.UserDAO;
import btl.carrentalwebsite.util.FileUploadUtil;

@WebServlet("/register")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10, // 10MB
                 maxRequestSize = 1024 * 1024 * 50) // 50MB
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // Lấy dữ liệu
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String familyName = request.getParameter("familyName");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        Part filePart = request.getPart("avatar");
        
        // Convert ngày sinh
        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
        Date sqlDate = Date.valueOf(dateOfBirth);
        
        // Tạo link cho avatar
        String avatarPath = "0.jpg";
        if(filePart != null && filePart.getSize() > 0){
            
            // Lấy phần mở rộng của file
            String fileExtension = FileUploadUtil.getFileExtension(filePart);
            if (!FileUploadUtil.isValidFileType(fileExtension)) {
                request.setAttribute("errorMessage", "Định dạng file không hợp lệ!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
            
            avatarPath = UUID.randomUUID().toString() + fileExtension;
        }

        // Tạo đối tượng User
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setFamilyName(familyName);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setAvatar(avatarPath);
        user.setDateOfBirth(sqlDate);
        
        // Kiểm tra Validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Lỗi:<br>");
            for (ConstraintViolation<User> violation : violations) {
                errorMessage.append("- ").append(violation.getMessage()).append("<br>");
            }
            request.setAttribute("errorMessage", errorMessage.toString());
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Lưu vào database
        UserDAO userDAO = new UserDAO();
        try {
            if(userDAO.create(user)){
                if(!"0.jpg".equals(avatarPath)){
                    FileUploadUtil.saveUserAVT(filePart, avatarPath);
                }
                
                response.sendRedirect("login.jsp"); // Chuyển đến trang đăng nhập
                
            } 
            
        } catch (SQLException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
            
        } catch (Exception e){
            request.setAttribute("errorMessage", "Đăng ký thất bại, thử lại!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            
        } finally {
            userDAO.closeConnection();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
