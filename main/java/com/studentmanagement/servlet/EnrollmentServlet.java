package com.studentmanagement.servlet;

import com.studentmanagement.dao.EnrollmentDAO;
import com.studentmanagement.dao.HomeroomDAO;
import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.dao.UserDAO;
import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.exception.ValidationException;
import com.studentmanagement.model.Enrollment;
import com.studentmanagement.model.Homeroom;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * EnrollmentServlet - Xử lý CRUD cho Enrollment (Điểm số)
 */
@WebServlet(name = "EnrollmentServlet", urlPatterns = {"/enrollments", "/enrollments/*"})
public class EnrollmentServlet extends HttpServlet {
    private EnrollmentDAO enrollmentDAO;
    private StudentDAO studentDAO;
    private HomeroomDAO homeroomDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.enrollmentDAO = new EnrollmentDAO();
        this.studentDAO = new StudentDAO();
        this.homeroomDAO = new HomeroomDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }
        
        setUserToRequest(request);
        
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");
        String classId = request.getParameter("classId");
        String studentId = request.getParameter("studentId");
        
        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                // Path format: /enrollments/{classId}/{studentId}
                String[] parts = pathInfo.substring(1).split("/");
                if (parts.length >= 2) {
                    String pathClassId = parts[0];
                    String pathStudentId = parts[1];
                    
                    if ("edit".equals(action)) {
                        showEditForm(request, response, pathClassId, pathStudentId);
                    } else if ("delete".equals(action)) {
                        deleteEnrollment(request, response, pathClassId, pathStudentId);
                    } else {
                        showDetail(request, response, pathClassId, pathStudentId);
                    }
                    return;
                }
            }
            
            if ("new".equals(action)) {
                showCreateForm(request, response, classId, studentId);
            } else {
                listEnrollments(request, response, classId, studentId);
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
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                createEnrollment(request, response);
            } else if ("update".equals(action)) {
                updateEnrollment(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/enrollments");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }
    
    /**
     * Hiển thị danh sách enrollments
     */
    private void listEnrollments(HttpServletRequest request, HttpServletResponse response, 
                                  String classId, String studentId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        
        List<Enrollment> enrollments;
        
        // Nếu user là STUDENT, chỉ hiển thị điểm của chính họ
        if (currentUser != null && "STUDENT".equals(currentUser.getRole())) {
            // Lấy studentId từ user
            String userStudentId = getUserStudentId(currentUser);
            if (userStudentId != null) {
                enrollments = enrollmentDAO.findByStudentId(userStudentId);
                Student student = studentDAO.findById(userStudentId);
                request.setAttribute("student", student);
                request.setAttribute("isStudentView", true); // Flag để ẩn filter và buttons trong JSP
            } else {
                enrollments = new java.util.ArrayList<>();
            }
        } else if (classId != null && !classId.isEmpty()) {
            enrollments = enrollmentDAO.findByClassId(classId);
            Homeroom homeroom = homeroomDAO.findById(classId);
            request.setAttribute("homeroom", homeroom);
        } else if (studentId != null && !studentId.isEmpty()) {
            enrollments = enrollmentDAO.findByStudentId(studentId);
            Student student = studentDAO.findById(studentId);
            request.setAttribute("student", student);
        } else {
            enrollments = enrollmentDAO.findAll();
        }
        
        request.setAttribute("enrollments", enrollments);
        request.setAttribute("selectedClassId", classId);
        request.setAttribute("selectedStudentId", studentId);
        
        // Load dropdowns for filter (chỉ cho ADMIN/TEACHER)
        if (currentUser == null || !"STUDENT".equals(currentUser.getRole())) {
            request.setAttribute("homerooms", homeroomDAO.findAll());
            request.setAttribute("students", studentDAO.findAll());
        }
        
        request.getRequestDispatcher("/pages/enrollment/list.jsp").forward(request, response);
    }
    
    /**
     * Lấy studentId từ User object
     */
    private String getUserStudentId(User user) throws DatabaseException {
        if (user.getStudent() != null) {
            return user.getStudent().getStudentId();
        }
        
        // Nếu chưa load student, load từ database
        UserDAO userDAO = new UserDAO();
        return userDAO.getStudentIdByUserId(user.getUserId());
    }
    
    /**
     * Hiển thị chi tiết enrollment
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response, 
                            String classId, String studentId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        Enrollment enrollment = enrollmentDAO.findById(classId, studentId);
        if (enrollment == null) {
            request.setAttribute("error", "Không tìm thấy đăng ký học phần.");
            listEnrollments(request, response, null, null);
            return;
        }
        request.setAttribute("enrollment", enrollment);
        request.getRequestDispatcher("/pages/enrollment/detail.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form tạo mới
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response,
                                String classId, String studentId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        loadDropdowns(request);
        request.setAttribute("presetClassId", classId);
        request.setAttribute("presetStudentId", studentId);
        request.getRequestDispatcher("/pages/enrollment/form.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form chỉnh sửa
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response,
                              String classId, String studentId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        Enrollment enrollment = enrollmentDAO.findById(classId, studentId);
        if (enrollment == null) {
            request.setAttribute("error", "Không tìm thấy đăng ký học phần.");
            listEnrollments(request, response, null, null);
            return;
        }
        request.setAttribute("enrollment", enrollment);
        request.setAttribute("isEdit", true);
        loadDropdowns(request);
        request.getRequestDispatcher("/pages/enrollment/form.jsp").forward(request, response);
    }
    
    /**
     * Load dữ liệu cho dropdown
     */
    private void loadDropdowns(HttpServletRequest request) throws DatabaseException {
        request.setAttribute("homerooms", homeroomDAO.findAll());
        request.setAttribute("students", studentDAO.findAll());
    }
    
    /**
     * Tạo enrollment mới
     */
    private void createEnrollment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Enrollment enrollment = parseEnrollmentFromRequest(request);
        
        // Kiểm tra enrollment đã tồn tại chưa
        Enrollment existing = enrollmentDAO.findById(
            enrollment.getHomeroom().getClassId(),
            enrollment.getStudent().getStudentId()
        );
        if (existing != null) {
            request.setAttribute("error", "Sinh viên đã đăng ký lớp học này rồi!");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("enrollment", enrollment);
            request.getRequestDispatcher("/pages/enrollment/form.jsp").forward(request, response);
            return;
        }
        
        boolean success = enrollmentDAO.save(enrollment);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Tạo đăng ký học phần thành công!");
            response.sendRedirect(request.getContextPath() + "/enrollments");
        } else {
            request.setAttribute("error", "Không thể tạo đăng ký học phần. Vui lòng thử lại.");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("enrollment", enrollment);
            request.getRequestDispatcher("/pages/enrollment/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Cập nhật enrollment
     */
    private void updateEnrollment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Enrollment enrollment = parseEnrollmentFromRequest(request);
        String classId = request.getParameter("classId");
        String studentId = request.getParameter("studentId");
        
        if (classId == null || classId.isEmpty() || studentId == null || studentId.isEmpty()) {
            request.setAttribute("error", "Class ID và Student ID là bắt buộc!");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("enrollment", enrollment);
            request.getRequestDispatcher("/pages/enrollment/form.jsp").forward(request, response);
            return;
        }
        
        boolean success = enrollmentDAO.update(enrollment);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật điểm số thành công!");
            response.sendRedirect(request.getContextPath() + "/enrollments");
        } else {
            request.setAttribute("error", "Không thể cập nhật điểm số. Vui lòng thử lại.");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("enrollment", enrollment);
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/pages/enrollment/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Xóa enrollment
     */
    private void deleteEnrollment(HttpServletRequest request, HttpServletResponse response,
                                  String classId, String studentId)
            throws ServletException, IOException, DatabaseException {
        try {
            boolean success = enrollmentDAO.delete(classId, studentId);
            if (success) {
                request.getSession().setAttribute("successMessage", "Xóa đăng ký học phần thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể xóa đăng ký học phần.");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/enrollments");
    }
    
    /**
     * Parse Enrollment từ request parameters
     */
    private Enrollment parseEnrollmentFromRequest(HttpServletRequest request) 
            throws DatabaseException, ValidationException {
        Enrollment enrollment = new Enrollment();
        
        String classId = request.getParameter("classId");
        String studentId = request.getParameter("studentId");
        String attendanceStr = request.getParameter("attendance");
        String homeworkStr = request.getParameter("homework");
        String midTermStr = request.getParameter("midTerm");
        String endTermStr = request.getParameter("endTerm");
        
        if (classId != null && !classId.trim().isEmpty()) {
            Homeroom homeroom = homeroomDAO.findById(classId.trim());
            if (homeroom == null) {
                throw new ValidationException("Lớp học không tồn tại!");
            }
            enrollment.setHomeroom(homeroom);
        } else {
            throw new ValidationException("Lớp học là bắt buộc!");
        }
        
        if (studentId != null && !studentId.trim().isEmpty()) {
            Student student = studentDAO.findById(studentId.trim());
            if (student == null) {
                throw new ValidationException("Sinh viên không tồn tại!");
            }
            enrollment.setStudent(student);
        } else {
            throw new ValidationException("Sinh viên là bắt buộc!");
        }
        
        try {
            if (attendanceStr != null && !attendanceStr.trim().isEmpty()) {
                BigDecimal attendance = new BigDecimal(attendanceStr.trim());
                enrollment.setAttendance(attendance);
            }
            if (homeworkStr != null && !homeworkStr.trim().isEmpty()) {
                BigDecimal homework = new BigDecimal(homeworkStr.trim());
                enrollment.setHomework(homework);
            }
            if (midTermStr != null && !midTermStr.trim().isEmpty()) {
                BigDecimal midTerm = new BigDecimal(midTermStr.trim());
                enrollment.setMidTerm(midTerm);
            }
            if (endTermStr != null && !endTermStr.trim().isEmpty()) {
                BigDecimal endTerm = new BigDecimal(endTermStr.trim());
                enrollment.setEndTerm(endTerm);
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Điểm số phải là số hợp lệ (0-10)!");
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage());
        }
        
        return enrollment;
    }
    
    /**
     * Helper method: Set user từ session vào request
     */
    private void setUserToRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            request.setAttribute("user", session.getAttribute("user"));
        }
    }
    
    /**
     * Xử lý lỗi
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("error", "Lỗi: " + e.getMessage());
        setUserToRequest(request);
        
        try {
            listEnrollments(request, response, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

