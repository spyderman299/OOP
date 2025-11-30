package com.studentmanagement.servlet;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.dao.TeacherDAO;
import com.studentmanagement.dao.UserDAO;
import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.exception.ValidationException;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.Teacher;
import com.studentmanagement.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserServlet - Xử lý CRUD cho User (chỉ ADMIN)
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/users", "/users/*"})
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.userDAO = new UserDAO();
        this.studentDAO = new StudentDAO();
        this.teacherDAO = new TeacherDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        // Chỉ ADMIN mới được truy cập
        if (!"ADMIN".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập!");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");
        
        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                String userId = pathInfo.substring(1);
                
                if ("edit".equals(action)) {
                    showEditForm(request, response, userId);
                } else if ("delete".equals(action)) {
                    deleteUser(request, response, userId);
                } else {
                    showDetail(request, response, userId);
                }
            } else if ("new".equals(action)) {
                showCreateForm(request, response);
            } else {
                listUsers(request, response);
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        if (!"ADMIN".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền!");
            return;
        }
        
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                createUser(request, response);
            } else if ("update".equals(action)) {
                updateUser(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }
    
    /**
     * Hiển thị danh sách users
     */
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        List<User> users = userDAO.findAll();
        
        // Load thông tin Student và Teacher cho mỗi user
        for (User user : users) {
            if ("STUDENT".equals(user.getRole())) {
                String studentId = userDAO.getStudentIdByUserId(user.getUserId());
                if (studentId != null) {
                    user.setStudent(studentDAO.findById(studentId));
                }
            } else if ("TEACHER".equals(user.getRole())) {
                String teacherId = userDAO.getTeacherIdByUserId(user.getUserId());
                if (teacherId != null) {
                    user.setTeacher(teacherDAO.findById(teacherId));
                }
            }
        }
        
        request.setAttribute("users", users);
        request.getRequestDispatcher("/pages/user/list.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị chi tiết user
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response, String userId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        User user = userDAO.findById(userId);
        if (user == null) {
            request.setAttribute("error", "Không tìm thấy người dùng với ID: " + userId);
            listUsers(request, response);
            return;
        }
        
        // Load thông tin Student hoặc Teacher
        if ("STUDENT".equals(user.getRole())) {
            String studentId = userDAO.getStudentIdByUserId(user.getUserId());
            if (studentId != null) {
                user.setStudent(studentDAO.findById(studentId));
            }
        } else if ("TEACHER".equals(user.getRole())) {
            String teacherId = userDAO.getTeacherIdByUserId(user.getUserId());
            if (teacherId != null) {
                user.setTeacher(teacherDAO.findById(teacherId));
            }
        }
        
        request.setAttribute("userDetail", user);
        request.getRequestDispatcher("/pages/user/detail.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form tạo mới
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        loadDropdowns(request);
        request.getRequestDispatcher("/pages/user/form.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form chỉnh sửa
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String userId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        User user = userDAO.findById(userId);
        if (user == null) {
            request.setAttribute("error", "Không tìm thấy người dùng với ID: " + userId);
            listUsers(request, response);
            return;
        }
        
        // Load thông tin Student hoặc Teacher
        if ("STUDENT".equals(user.getRole())) {
            String studentId = userDAO.getStudentIdByUserId(user.getUserId());
            if (studentId != null) {
                user.setStudent(studentDAO.findById(studentId));
            }
        } else if ("TEACHER".equals(user.getRole())) {
            String teacherId = userDAO.getTeacherIdByUserId(user.getUserId());
            if (teacherId != null) {
                user.setTeacher(teacherDAO.findById(teacherId));
            }
        }
        
        request.setAttribute("userDetail", user);
        request.setAttribute("isEdit", true);
        loadDropdowns(request);
        request.getRequestDispatcher("/pages/user/form.jsp").forward(request, response);
    }
    
    /**
     * Load dữ liệu cho dropdown
     */
    private void loadDropdowns(HttpServletRequest request) throws DatabaseException {
        request.setAttribute("students", studentDAO.findAll());
        request.setAttribute("teachers", teacherDAO.findAll());
    }
    
    /**
     * Tạo user mới
     */
    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        User user = parseUserFromRequest(request);
        
        // Kiểm tra username đã tồn tại chưa
        User existing = userDAO.findByUsername(user.getUsername());
        if (existing != null) {
            request.setAttribute("error", "Username đã tồn tại!");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("userDetail", user);
            request.getRequestDispatcher("/pages/user/form.jsp").forward(request, response);
            return;
        }
        
        boolean success = userDAO.save(user);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Tạo tài khoản thành công!");
            response.sendRedirect(request.getContextPath() + "/users");
        } else {
            request.setAttribute("error", "Không thể tạo tài khoản. Vui lòng thử lại.");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("userDetail", user);
            request.getRequestDispatcher("/pages/user/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Cập nhật user
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        User user = parseUserFromRequest(request);
        String userId = request.getParameter("userId");
        
        if (userId == null || userId.isEmpty()) {
            request.setAttribute("error", "User ID là bắt buộc!");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("userDetail", user);
            request.getRequestDispatcher("/pages/user/form.jsp").forward(request, response);
            return;
        }
        
        user.setUserId(Integer.parseInt(userId));
        
        // Kiểm tra username đã tồn tại chưa (trừ user hiện tại)
        User existing = userDAO.findByUsername(user.getUsername());
        if (existing != null && existing.getUserId() != user.getUserId()) {
            request.setAttribute("error", "Username đã tồn tại!");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("userDetail", user);
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/pages/user/form.jsp").forward(request, response);
            return;
        }
        
        boolean success = userDAO.update(user);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật tài khoản thành công!");
            response.sendRedirect(request.getContextPath() + "/users");
        } else {
            request.setAttribute("error", "Không thể cập nhật tài khoản. Vui lòng thử lại.");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("userDetail", user);
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/pages/user/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Xóa user
     */
    private void deleteUser(HttpServletRequest request, HttpServletResponse response, String userId)
            throws ServletException, IOException, DatabaseException {
        try {
            // Không cho xóa chính mình
            HttpSession session = request.getSession(false);
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null && String.valueOf(currentUser.getUserId()).equals(userId)) {
                request.getSession().setAttribute("errorMessage", "Không thể xóa chính mình!");
                response.sendRedirect(request.getContextPath() + "/users");
                return;
            }
            
            boolean success = userDAO.delete(userId);
            if (success) {
                request.getSession().setAttribute("successMessage", "Xóa tài khoản thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể xóa tài khoản.");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/users");
    }
    
    /**
     * Parse User từ request
     */
    private User parseUserFromRequest(HttpServletRequest request) throws DatabaseException, ValidationException {
        User user = new User();
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String studentId = request.getParameter("studentId");
        String teacherId = request.getParameter("teacherId");
        
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username là bắt buộc!");
        }
        user.setUsername(username.trim());
        
        // Password chỉ required khi tạo mới
        String userIdParam = request.getParameter("userId");
        boolean isEdit = userIdParam != null && !userIdParam.isEmpty();
        if (!isEdit && (password == null || password.isEmpty())) {
            throw new ValidationException("Password là bắt buộc!");
        }
        if (password != null && !password.isEmpty()) {
            user.setPasswordPlain(password);
        } else if (isEdit) {
            // Khi edit, nếu không nhập password thì giữ nguyên password cũ
            User existingUser = userDAO.findById(userIdParam);
            if (existingUser != null) {
                user.setPasswordPlain(existingUser.getPasswordPlain());
            }
        }
        
        if (role == null || role.trim().isEmpty()) {
            throw new ValidationException("Vai trò là bắt buộc!");
        }
        user.setRole(role.trim());
        
        // Set Student hoặc Teacher dựa trên role
        if ("STUDENT".equals(role)) {
            if (studentId != null && !studentId.trim().isEmpty()) {
                Student student = studentDAO.findById(studentId.trim());
                if (student == null) {
                    throw new ValidationException("Sinh viên không tồn tại!");
                }
                user.setStudent(student);
            }
        } else if ("TEACHER".equals(role)) {
            if (teacherId != null && !teacherId.trim().isEmpty()) {
                Teacher teacher = teacherDAO.findById(teacherId.trim());
                if (teacher == null) {
                    throw new ValidationException("Giáo viên không tồn tại!");
                }
                user.setTeacher(teacher);
            }
        }
        
        return user;
    }
    
    private void setUserToRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            request.setAttribute("user", session.getAttribute("user"));
        }
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("error", "Lỗi: " + e.getMessage());
        setUserToRequest(request);
        
        try {
            listUsers(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

