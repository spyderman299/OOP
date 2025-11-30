package com.studentmanagement.servlet;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.model.User;
import com.studentmanagement.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LoginServlet - Xử lý đăng nhập
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private AuthenticationService authService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.authService = new AuthenticationService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nếu đã đăng nhập, redirect đến dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Chuyển đến trang login
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        String errorMessage = null;
        User user = null;
        
        try {
            // Xác thực
            user = authService.authenticate(username, password);
            
            if (user == null) {
                errorMessage = "Username hoặc password không đúng!";
            }
        } catch (DatabaseException e) {
            errorMessage = "Lỗi hệ thống: " + e.getMessage();
            e.printStackTrace();
        }
        
        if (user != null) {
            // Đăng nhập thành công
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            
            // Load thông tin đầy đủ nếu cần
            try {
                if (user.isStudent()) {
                    authService.loadStudentForUser(user);
                } else if (user.isTeacher()) {
                    authService.loadTeacherForUser(user);
                }
            } catch (DatabaseException e) {
                // Log error nhưng vẫn cho đăng nhập
                System.err.println("Error loading user details: " + e.getMessage());
            }
            
            // Redirect theo role
            String redirectPath = getRedirectPath(user.getRole());
            response.sendRedirect(request.getContextPath() + redirectPath);
        } else {
            // Đăng nhập thất bại
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Lấy đường dẫn redirect dựa trên role
     */
    private String getRedirectPath(String role) {
        if ("ADMIN".equals(role)) {
            return "/dashboard";
        } else if ("TEACHER".equals(role)) {
            return "/dashboard";
        } else if ("STUDENT".equals(role)) {
            return "/dashboard";
        }
        return "/pages/login.jsp";
    }
}

