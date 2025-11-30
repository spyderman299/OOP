package com.studentmanagement.servlet;

import com.studentmanagement.dao.SubjectDAO;
import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.exception.ValidationException;
import com.studentmanagement.model.Subject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * SubjectServlet - Xử lý CRUD cho Subject (Môn học)
 */
@WebServlet(name = "SubjectServlet", urlPatterns = {"/subjects", "/subjects/*"})
public class SubjectServlet extends HttpServlet {
    private SubjectDAO subjectDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.subjectDAO = new SubjectDAO();
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
                String subjectId = pathInfo.substring(1);
                
                if ("edit".equals(action)) {
                    showEditForm(request, response, subjectId);
                } else if ("delete".equals(action)) {
                    deleteSubject(request, response, subjectId);
                } else {
                    showDetail(request, response, subjectId);
                }
            } else if ("new".equals(action)) {
                showCreateForm(request, response);
            } else {
                listSubjects(request, response);
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
                createSubject(request, response);
            } else if ("update".equals(action)) {
                updateSubject(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/subjects");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }
    
    /**
     * Hiển thị danh sách môn học
     */
    private void listSubjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        List<Subject> subjects = subjectDAO.findAll();
        request.setAttribute("subjects", subjects);
        request.getRequestDispatcher("/pages/subject/list.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị chi tiết môn học
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response, String subjectId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        Subject subject = subjectDAO.findById(subjectId);
        if (subject == null) {
            request.setAttribute("error", "Không tìm thấy môn học với ID: " + subjectId);
            listSubjects(request, response);
            return;
        }
        request.setAttribute("subject", subject);
        request.getRequestDispatcher("/pages/subject/detail.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form tạo mới
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUserToRequest(request);
        request.getRequestDispatcher("/pages/subject/form.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form chỉnh sửa
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String subjectId)
            throws ServletException, IOException, DatabaseException {
        setUserToRequest(request);
        Subject subject = subjectDAO.findById(subjectId);
        if (subject == null) {
            request.setAttribute("error", "Không tìm thấy môn học với ID: " + subjectId);
            listSubjects(request, response);
            return;
        }
        request.setAttribute("subject", subject);
        request.setAttribute("isEdit", true);
        request.getRequestDispatcher("/pages/subject/form.jsp").forward(request, response);
    }
    
    /**
     * Tạo môn học mới
     */
    private void createSubject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Subject subject = parseSubjectFromRequest(request);
        
        boolean success = subjectDAO.save(subject);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Tạo môn học thành công!");
            response.sendRedirect(request.getContextPath() + "/subjects");
        } else {
            request.setAttribute("error", "Không thể tạo môn học. Vui lòng thử lại.");
            setUserToRequest(request);
            request.setAttribute("subject", subject);
            request.getRequestDispatcher("/pages/subject/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Cập nhật môn học
     */
    private void updateSubject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, ValidationException {
        Subject subject = parseSubjectFromRequest(request);
        String subjectId = request.getParameter("subjectId");
        
        if (subjectId == null || subjectId.isEmpty()) {
            request.setAttribute("error", "Subject ID là bắt buộc!");
            setUserToRequest(request);
            request.setAttribute("subject", subject);
            request.getRequestDispatcher("/pages/subject/form.jsp").forward(request, response);
            return;
        }
        
        subject.setSubjectId(subjectId);
        boolean success = subjectDAO.update(subject);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật môn học thành công!");
            response.sendRedirect(request.getContextPath() + "/subjects");
        } else {
            request.setAttribute("error", "Không thể cập nhật môn học. Vui lòng thử lại.");
            setUserToRequest(request);
            request.setAttribute("subject", subject);
            request.setAttribute("isEdit", true);
            request.getRequestDispatcher("/pages/subject/form.jsp").forward(request, response);
        }
    }
    
    /**
     * Xóa môn học
     */
    private void deleteSubject(HttpServletRequest request, HttpServletResponse response, String subjectId)
            throws ServletException, IOException, DatabaseException {
        try {
            boolean success = subjectDAO.delete(subjectId);
            if (success) {
                request.getSession().setAttribute("successMessage", "Xóa môn học thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Không thể xóa môn học.");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi xóa: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/subjects");
    }
    
    /**
     * Parse Subject từ request parameters
     */
    private Subject parseSubjectFromRequest(HttpServletRequest request) throws ValidationException {
        Subject subject = new Subject();
        
        String subjectId = request.getParameter("subjectId");
        String name = request.getParameter("name");
        String totalSessionsStr = request.getParameter("totalSessions");
        
        if (subjectId != null && !subjectId.trim().isEmpty()) {
            subject.setSubjectId(subjectId.trim());
        } else if (request.getParameter("action") != null && request.getParameter("action").equals("create")) {
            throw new ValidationException("Mã môn học là bắt buộc!");
        }
        
        if (name != null && !name.trim().isEmpty()) {
            subject.setName(name.trim());
        } else {
            throw new ValidationException("Tên môn học là bắt buộc!");
        }
        
        if (totalSessionsStr != null && !totalSessionsStr.trim().isEmpty()) {
            try {
                int totalSessions = Integer.parseInt(totalSessionsStr.trim());
                if (totalSessions < 0) {
                    throw new ValidationException("Số buổi học phải >= 0!");
                }
                subject.setTotalSessions(totalSessions);
            } catch (NumberFormatException e) {
                throw new ValidationException("Số buổi học phải là số nguyên!");
            }
        } else {
            throw new ValidationException("Số buổi học là bắt buộc!");
        }
        
        return subject;
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
            listSubjects(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

