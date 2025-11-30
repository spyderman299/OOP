package com.studentmanagement.servlet;

import com.studentmanagement.dao.HomeroomDAO;
import com.studentmanagement.dao.SubjectDAO;
import com.studentmanagement.dao.TeacherDAO;
import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.exception.ValidationException;
import com.studentmanagement.model.Homeroom;
import com.studentmanagement.model.Subject;
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
 * ClassServlet - Xử lý CRUD cho Homeroom (Lớp học)
 */
@WebServlet(name = "ClassServlet", urlPatterns = {"/classes", "/classes/*"})
public class ClassServlet extends HttpServlet {
    private HomeroomDAO homeroomDAO;
    private SubjectDAO subjectDAO;
    private TeacherDAO teacherDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.homeroomDAO = new HomeroomDAO();
        this.subjectDAO = new SubjectDAO();
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
                String classId = pathInfo.substring(1);
                
                if ("edit".equals(action)) {
                    showEditForm(request, response, classId);
                } else if ("delete".equals(action)) {
                    deleteClass(request, response, classId);
                } else {
                    showDetail(request, response, classId);
                }
            } else if ("new".equals(action)) {
                showCreateForm(request, response);
            } else {
                listClasses(request, response);
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
                createClass(request, response);
            } else if ("update".equals(action)) {
                updateClass(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/classes");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }
    
    /**
     * Hiển thị danh sách lớp học
     */
    private void listClasses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        List<Homeroom> classes = homeroomDAO.findAll();
        request.setAttribute("classes", classes);
        request.getRequestDispatcher("/pages/class/list.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị chi tiết lớp học
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response, String classId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        Homeroom homeroom = homeroomDAO.findById(classId);
        if (homeroom == null) {
            request.setAttribute("error", "Không tìm thấy lớp học với ID: " + classId);
            listClasses(request, response);
            return;
        }
        request.setAttribute("homeroom", homeroom);
        request.getRequestDispatcher("/pages/class/detail.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form tạo mới
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        loadDropdowns(request);
        request.getRequestDispatcher("/pages/class/form.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form chỉnh sửa
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String classId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        Homeroom homeroom = homeroomDAO.findById(classId);
        if (homeroom == null) {
            request.setAttribute("error", "Không tìm thấy lớp học với ID: " + classId);
            listClasses(request, response);
            return;
        }
        request.setAttribute("homeroom", homeroom);
        request.setAttribute("isEdit", true);
        loadDropdowns(request);
        request.getRequestDispatcher("/pages/class/form.jsp").forward(request, response);
    }
    
    /**
     * Load dữ liệu cho dropdown (subjects, teachers)
     */
    private void loadDropdowns(HttpServletRequest request) throws DatabaseException {
        List<Subject> subjects = subjectDAO.findAll();
        List<Teacher> teachers = teacherDAO.findAll();
        request.setAttribute("subjects", subjects);
        request.setAttribute("teachers", teachers);
    }
    
    /**
     * Tạo lớp học mới
     */
    private void createClass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Homeroom homeroom = parseHomeroomFromRequest(request);
        homeroom.setClassId(request.getParameter("classId"));
        
        boolean success = homeroomDAO.save(homeroom);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Tạo lớp học thành công!");
            response.sendRedirect(request.getContextPath() + "/classes");
        } else {
            request.setAttribute("error", "Không thể tạo lớp học. Vui lòng thử lại.");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("homeroom", homeroom);
            request.getRequestDispatcher("/pages/class/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Cập nhật lớp học
     */
    private void updateClass(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Homeroom homeroom = parseHomeroomFromRequest(request);
        String classId = request.getParameter("classId");
        
        if (classId == null || classId.isEmpty()) {
            request.setAttribute("error", "Class ID là bắt buộc!");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("homeroom", homeroom);
            request.getRequestDispatcher("/pages/class/form.jsp").forward(request, response);
            return;
        }
        
        homeroom.setClassId(classId);
        boolean success = homeroomDAO.update(homeroom);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật lớp học thành công!");
            response.sendRedirect(request.getContextPath() + "/classes");
        } else {
            request.setAttribute("error", "Không thể cập nhật lớp học. Vui lòng thử lại.");
            setUserToRequest(request);
            loadDropdowns(request);
            request.setAttribute("homeroom", homeroom);
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/pages/class/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Xóa lớp học
     */
    private void deleteClass(HttpServletRequest request, HttpServletResponse response, String classId)
            throws ServletException, IOException, DatabaseException {
        try {
            boolean success = homeroomDAO.delete(classId);
            if (success) {
                request.getSession().setAttribute("successMessage", "Xóa lớp học thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể xóa lớp học.");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/classes");
    }
    
    /**
     * Parse Homeroom từ request parameters
     */
    private Homeroom parseHomeroomFromRequest(HttpServletRequest request) throws DatabaseException, ValidationException {
        Homeroom homeroom = new Homeroom();
        
        String subjectId = request.getParameter("subjectId");
        String teacherId = request.getParameter("teacherId");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        if (subjectId != null && !subjectId.trim().isEmpty()) {
            Subject subject = subjectDAO.findById(subjectId.trim());
            if (subject == null) {
                throw new ValidationException("Môn học không tồn tại!");
            }
            homeroom.setSubject(subject);
        } else {
            throw new ValidationException("Môn học là bắt buộc!");
        }
        
        if (teacherId != null && !teacherId.trim().isEmpty()) {
            Teacher teacher = teacherDAO.findById(teacherId.trim());
            if (teacher == null) {
                throw new ValidationException("Giáo viên không tồn tại!");
            }
            homeroom.setTeacher(teacher);
        } else {
            throw new ValidationException("Giáo viên là bắt buộc!");
        }
        
        if (startDateStr != null && !startDateStr.trim().isEmpty()) {
            try {
                LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_DATE);
                homeroom.setStartDate(startDate);
            } catch (DateTimeParseException e) {
                throw new ValidationException("Ngày bắt đầu không hợp lệ!");
            }
        } else {
            throw new ValidationException("Ngày bắt đầu là bắt buộc!");
        }
        
        if (endDateStr != null && !endDateStr.trim().isEmpty()) {
            try {
                LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_DATE);
                
                // Validate: end date must be AFTER start date
                if (homeroom.getStartDate() != null) {
                    if (!endDate.isAfter(homeroom.getStartDate())) {
                        throw new ValidationException("Ngày kết thúc phải sau ngày bắt đầu!");
                    }
                }
                
                homeroom.setEndDate(endDate);
            } catch (DateTimeParseException e) {
                throw new ValidationException("Ngày kết thúc không hợp lệ!");
            }
        } else {
            throw new ValidationException("Ngày kết thúc là bắt buộc!");
        }
        
        return homeroom;
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
            listClasses(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

