package com.studentmanagement.servlet;

import com.studentmanagement.dao.HomeroomDAO;
import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.dao.SubjectDAO;
import com.studentmanagement.dao.TeacherDAO;
import com.studentmanagement.exception.DatabaseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.studentmanagement.config.DatabaseConnection;

/**
 * DashboardServlet - Load statistics for dashboard
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }
        
        try {
            // Get statistics
            int studentCount = getStudentCount();
            int teacherCount = getTeacherCount();
            int subjectCount = getSubjectCount();
            int classCount = getClassCount();
            
            // Get chart data
            java.util.Map<String, Integer> studentsBySubject = getStudentsBySubject();
            java.util.Map<String, Integer> passFailStats = getPassFailStatistics();
            
            // Set attributes
            request.setAttribute("studentCount", studentCount);
            request.setAttribute("teacherCount", teacherCount);
            request.setAttribute("subjectCount", subjectCount);
            request.setAttribute("classCount", classCount);
            request.setAttribute("studentsBySubject", studentsBySubject);
            request.setAttribute("passFailStats", passFailStats);
            
            // Forward to dashboard
            request.getRequestDispatcher("/pages/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải thống kê: " + e.getMessage());
            request.getRequestDispatcher("/pages/dashboard.jsp").forward(request, response);
        }
    }
    
    private int getStudentCount() throws DatabaseException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM students")) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            throw new DatabaseException("Error getting student count: " + e.getMessage(), e);
        }
        return 0;
    }
    
    private int getTeacherCount() throws DatabaseException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM teachers")) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            throw new DatabaseException("Error getting teacher count: " + e.getMessage(), e);
        }
        return 0;
    }
    
    private int getSubjectCount() throws DatabaseException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM subjects")) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            throw new DatabaseException("Error getting subject count: " + e.getMessage(), e);
        }
        return 0;
    }
    
    private int getClassCount() throws DatabaseException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM homerooms")) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            throw new DatabaseException("Error getting class count: " + e.getMessage(), e);
        }
        return 0;
    }
    
    /**
     * Lấy số lượng sinh viên theo từng môn học (cho biểu đồ cột)
     */
    private Map<String, Integer> getStudentsBySubject() throws DatabaseException {
        Map<String, Integer> result = new HashMap<>();
        String sql = "SELECT s.subject_id, s.name, COUNT(DISTINCT e.student_id) as student_count " +
                     "FROM subjects s " +
                     "LEFT JOIN homerooms h ON s.subject_id = h.subject_id " +
                     "LEFT JOIN enrollments e ON h.class_id = e.class_id " +
                     "GROUP BY s.subject_id, s.name " +
                     "ORDER BY s.subject_id";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String subjectName = rs.getString("name");
                int count = rs.getInt("student_count");
                result.put(subjectName, count);
            }
        } catch (Exception e) {
            throw new DatabaseException("Error getting students by subject: " + e.getMessage(), e);
        }
        
        return result;
    }
    
    /**
     * Lấy thống kê tỷ lệ đạt/không đạt (cho biểu đồ tròn)
     */
    private Map<String, Integer> getPassFailStatistics() throws DatabaseException {
        Map<String, Integer> result = new HashMap<>();
        int passed = 0;
        int failed = 0;
        
        // Tính điểm cuối kỳ và kết quả dựa trên công thức: 0.1*attendance + 0.2*homework + 0.3*mid_term + 0.4*end_term
        String sql = "SELECT " +
                     "  SUM(CASE WHEN (0.1 * attendance + 0.2 * homework + 0.3 * mid_term + 0.4 * end_term) >= 5 THEN 1 ELSE 0 END) as passed, " +
                     "  SUM(CASE WHEN (0.1 * attendance + 0.2 * homework + 0.3 * mid_term + 0.4 * end_term) < 5 THEN 1 ELSE 0 END) as failed " +
                     "FROM enrollments " +
                     "WHERE attendance IS NOT NULL AND homework IS NOT NULL AND mid_term IS NOT NULL AND end_term IS NOT NULL";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                passed = rs.getInt("passed");
                failed = rs.getInt("failed");
            }
        } catch (Exception e) {
            throw new DatabaseException("Error getting pass/fail statistics: " + e.getMessage(), e);
        }
        
        result.put("Đạt", passed);
        result.put("Không đạt", failed);
        
        return result;
    }
}

