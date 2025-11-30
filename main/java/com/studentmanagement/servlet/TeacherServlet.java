package com.studentmanagement.servlet;

import com.studentmanagement.dao.TeacherDAO;
import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.exception.ValidationException;
import com.studentmanagement.model.Teacher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * TeacherServlet - Xử lý CRUD cho Teacher (Giáo viên)
 */
@WebServlet(name = "TeacherServlet", urlPatterns = {"/teachers", "/teachers/*"})
public class TeacherServlet extends HttpServlet {
    private TeacherDAO teacherDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
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
        
        setUserToRequest(request);
        
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");
        
        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                String teacherId = pathInfo.substring(1);
                
                if ("edit".equals(action)) {
                    showEditForm(request, response, teacherId);
                } else if ("delete".equals(action)) {
                    deleteTeacher(request, response, teacherId);
                } else {
                    showDetail(request, response, teacherId);
                }
            } else if ("new".equals(action)) {
                showCreateForm(request, response);
            } else {
                listTeachers(request, response);
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
                createTeacher(request, response);
            } else if ("update".equals(action)) {
                updateTeacher(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/teachers");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }
    
    /**
     * Hiển thị danh sách giáo viên
     */
    private void listTeachers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        List<Teacher> teachers = teacherDAO.findAll();
        request.setAttribute("teachers", teachers);
        request.getRequestDispatcher("/pages/teacher/list.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị chi tiết giáo viên
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response, String teacherId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        Teacher teacher = teacherDAO.findById(teacherId);
        if (teacher == null) {
            request.setAttribute("error", "Không tìm thấy giáo viên với ID: " + teacherId);
            listTeachers(request, response);
            return;
        }
        request.setAttribute("teacher", teacher);
        request.getRequestDispatcher("/pages/teacher/detail.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form tạo mới
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUserToRequest(request);
        request.getRequestDispatcher("/pages/teacher/form.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form chỉnh sửa
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String teacherId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        Teacher teacher = teacherDAO.findById(teacherId);
        if (teacher == null) {
            request.setAttribute("error", "Không tìm thấy giáo viên với ID: " + teacherId);
            listTeachers(request, response);
            return;
        }
        request.setAttribute("teacher", teacher);
        request.setAttribute("isEdit", true);
        request.getRequestDispatcher("/pages/teacher/form.jsp").forward(request, response);
    }
    
    /**
     * Tạo giáo viên mới
     */
    private void createTeacher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Teacher teacher = parseTeacherFromRequest(request);
        
        boolean success = teacherDAO.save(teacher);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Tạo giáo viên thành công!");
            response.sendRedirect(request.getContextPath() + "/teachers");
        } else {
            request.setAttribute("error", "Không thể tạo giáo viên. Vui lòng thử lại.");
            setUserToRequest(request);
            request.setAttribute("teacher", teacher);
            request.getRequestDispatcher("/pages/teacher/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Cập nhật giáo viên
     */
    private void updateTeacher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Teacher teacher = parseTeacherFromRequest(request);
        String teacherId = request.getParameter("teacherId");
        
        if (teacherId == null || teacherId.isEmpty()) {
            request.setAttribute("error", "Teacher ID là bắt buộc!");
            setUserToRequest(request);
            request.setAttribute("teacher", teacher);
            request.getRequestDispatcher("/pages/teacher/form.jsp").forward(request, response);
            return;
        }
        
        teacher.setTeacherId(teacherId);
        boolean success = teacherDAO.update(teacher);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật giáo viên thành công!");
            response.sendRedirect(request.getContextPath() + "/teachers");
        } else {
            request.setAttribute("error", "Không thể cập nhật giáo viên. Vui lòng thử lại.");
            setUserToRequest(request);
            request.setAttribute("teacher", teacher);
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/pages/teacher/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Xóa giáo viên
     */
    private void deleteTeacher(HttpServletRequest request, HttpServletResponse response, String teacherId)
            throws ServletException, IOException, DatabaseException {
        try {
            boolean success = teacherDAO.delete(teacherId);
            if (success) {
                request.getSession().setAttribute("successMessage", "Xóa giáo viên thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể xóa giáo viên.");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/teachers");
    }
    
    /**
     * Parse Teacher từ request parameters
     */
    private Teacher parseTeacherFromRequest(HttpServletRequest request) throws ValidationException {
        Teacher teacher = new Teacher();
        
        String teacherId = request.getParameter("teacherId");
        String name = request.getParameter("name");
        String dobStr = request.getParameter("dob");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        
        if (teacherId != null && !teacherId.trim().isEmpty()) {
            teacher.setTeacherId(teacherId.trim());
        } else if (request.getParameter("action") != null && request.getParameter("action").equals("create")) {
            throw new ValidationException("Mã giáo viên là bắt buộc!");
        }
        
        if (name != null && !name.trim().isEmpty()) {
            teacher.setName(name.trim());
        } else {
            throw new ValidationException("Tên giáo viên là bắt buộc!");
        }
        
        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ISO_DATE);
                teacher.setDob(dob);
            } catch (DateTimeParseException e) {
                throw new ValidationException("Ngày sinh không hợp lệ!");
            }
        }
        
        if (phone != null && !phone.trim().isEmpty()) {
            teacher.setPhone(phone.trim());
        }
        
        if (email != null && !email.trim().isEmpty()) {
            teacher.setEmail(email.trim());
        }
        
        return teacher;
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
            listTeachers(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

