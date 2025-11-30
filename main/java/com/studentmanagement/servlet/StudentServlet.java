package com.studentmanagement.servlet;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.exception.ValidationException;
import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;
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
 * StudentServlet - Xử lý CRUD cho Student
 */
@WebServlet(name = "StudentServlet", urlPatterns = {"/students", "/students/*"})
public class StudentServlet extends HttpServlet {
    private StudentService studentService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.studentService = new StudentService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");
        
        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                // Có path info: /students/{id} hoặc /students/{id}?action=...
                String studentId = pathInfo.substring(1);
                
                if ("edit".equals(action)) {
                    showEditForm(request, response, studentId);
                } else if ("delete".equals(action)) {
                    deleteStudent(request, response, studentId);
                } else {
                    showDetail(request, response, studentId);
                }
            } else if ("new".equals(action)) {
                showCreateForm(request, response);
            } else if ("search".equals(action)) {
                searchStudents(request, response);
            } else {
                listStudents(request, response);
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
                createStudent(request, response);
            } else if ("update".equals(action)) {
                updateStudent(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/students");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }
    
    /**
     * Hiển thị danh sách sinh viên
     */
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        List<Student> students = studentService.getAllStudents();
        request.setAttribute("students", students);
        request.getRequestDispatcher("/pages/student/list.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị chi tiết sinh viên
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response, String studentId)
            throws ServletException, IOException, DatabaseException, ValidationException {
        setUserToRequest(request);
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            request.setAttribute("error", "Không tìm thấy sinh viên với ID: " + studentId);
            listStudents(request, response);
            return;
        }
        request.setAttribute("student", student);
        request.getRequestDispatcher("/pages/student/detail.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form tạo mới
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUserToRequest(request);
        request.getRequestDispatcher("/pages/student/form.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form chỉnh sửa
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String studentId)
            throws ServletException, IOException, DatabaseException, ValidationException {
        setUserToRequest(request);
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            request.setAttribute("error", "Không tìm thấy sinh viên với ID: " + studentId);
            listStudents(request, response);
            return;
        }
        request.setAttribute("student", student);
        request.setAttribute("isEdit", true);
        request.getRequestDispatcher("/pages/student/form.jsp").forward(request, response);
    }
    
    /**
     * Tạo sinh viên mới
     */
    private void createStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Student student = parseStudentFromRequest(request);
        student.setStudentId(request.getParameter("studentId"));
        
        boolean success = studentService.createStudent(student);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Tạo sinh viên thành công!");
            response.sendRedirect(request.getContextPath() + "/students");
        } else {
            request.setAttribute("error", "Không thể tạo sinh viên. Vui lòng thử lại.");
            request.setAttribute("student", student);
            request.getRequestDispatcher("/pages/student/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Cập nhật sinh viên
     */
    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Student student = parseStudentFromRequest(request);
        String studentId = request.getParameter("studentId");
        
        if (studentId == null || studentId.isEmpty()) {
            request.setAttribute("error", "Student ID là bắt buộc!");
            request.setAttribute("student", student);
            request.getRequestDispatcher("/pages/student/form.jsp").forward(request, response);
            return;
        }
        
        student.setStudentId(studentId);
        boolean success = studentService.updateStudent(student);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật sinh viên thành công!");
            response.sendRedirect(request.getContextPath() + "/students");
        } else {
            request.setAttribute("error", "Không thể cập nhật sinh viên. Vui lòng thử lại.");
            request.setAttribute("student", student);
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/pages/student/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Xóa sinh viên
     */
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response, String studentId)
            throws ServletException, IOException, DatabaseException {
        try {
            boolean success = studentService.deleteStudent(studentId);
            if (success) {
                request.getSession().setAttribute("successMessage", "Xóa sinh viên thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể xóa sinh viên.");
            }
        } catch (ValidationException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/students");
    }
    
    /**
     * Tìm kiếm sinh viên
     */
    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        String searchTerm = request.getParameter("searchTerm");
        List<Student> students = studentService.searchStudentsByName(searchTerm != null ? searchTerm : "");
        request.setAttribute("students", students);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/pages/student/list.jsp").forward(request, response);
    }
    
    /**
     * Parse Student từ request parameters
     */
    private Student parseStudentFromRequest(HttpServletRequest request) {
        Student student = new Student();
        
        String name = request.getParameter("name");
        String dobStr = request.getParameter("dob");
        String phone = request.getParameter("phone");
        
        student.setName(name != null ? name.trim() : "");
        
        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ISO_DATE);
                student.setDob(dob);
            } catch (DateTimeParseException e) {
                // Giữ null nếu parse lỗi
            }
        }
        
        student.setPhone(phone != null ? phone.trim() : null);
        
        return student;
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
            listStudents(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

