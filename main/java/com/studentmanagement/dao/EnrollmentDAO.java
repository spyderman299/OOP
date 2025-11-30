package com.studentmanagement.dao;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.model.Enrollment;
import com.studentmanagement.model.Homeroom;
import com.studentmanagement.model.Student;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EnrollmentDAO - Xử lý database cho Enrollment
 */
public class EnrollmentDAO extends BaseDAO<Enrollment> {
    private StudentDAO studentDAO = new StudentDAO();
    private HomeroomDAO homeroomDAO = new HomeroomDAO();
    
    @Override
    public List<Enrollment> findAll() throws DatabaseException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments ORDER BY class_id, student_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all enrollments: " + e.getMessage(), e);
        }
        
        return enrollments;
    }
    
    /**
     * Tìm enrollment theo class_id và student_id
     */
    public Enrollment findById(String classId, String studentId) throws DatabaseException {
        String sql = "SELECT * FROM enrollments WHERE class_id = ? AND student_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, classId);
            stmt.setString(2, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEnrollment(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching enrollment: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public Enrollment findById(String id) throws DatabaseException {
        // Not applicable for composite key
        throw new UnsupportedOperationException("Use findById(String classId, String studentId) instead");
    }
    
    @Override
    public boolean save(Enrollment enrollment) throws DatabaseException {
        String sql = "INSERT INTO enrollments (class_id, student_id, attendance, homework, mid_term, end_term) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, enrollment.getHomeroom() != null ? enrollment.getHomeroom().getClassId() : null);
            stmt.setString(2, enrollment.getStudent() != null ? enrollment.getStudent().getStudentId() : null);
            stmt.setBigDecimal(3, enrollment.getAttendance());
            stmt.setBigDecimal(4, enrollment.getHomework());
            stmt.setBigDecimal(5, enrollment.getMidTerm());
            stmt.setBigDecimal(6, enrollment.getEndTerm());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving enrollment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Enrollment enrollment) throws DatabaseException {
        String sql = "UPDATE enrollments SET attendance = ?, homework = ?, mid_term = ?, end_term = ? WHERE class_id = ? AND student_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, enrollment.getAttendance());
            stmt.setBigDecimal(2, enrollment.getHomework());
            stmt.setBigDecimal(3, enrollment.getMidTerm());
            stmt.setBigDecimal(4, enrollment.getEndTerm());
            stmt.setString(5, enrollment.getHomeroom() != null ? enrollment.getHomeroom().getClassId() : null);
            stmt.setString(6, enrollment.getStudent() != null ? enrollment.getStudent().getStudentId() : null);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating enrollment: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String id) throws DatabaseException {
        // Not applicable for composite key
        throw new UnsupportedOperationException("Use delete(String classId, String studentId) instead");
    }
    
    /**
     * Xóa enrollment theo class_id và student_id
     */
    public boolean delete(String classId, String studentId) throws DatabaseException {
        String sql = "DELETE FROM enrollments WHERE class_id = ? AND student_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, classId);
            stmt.setString(2, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting enrollment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lấy danh sách enrollment theo class_id
     */
    public List<Enrollment> findByClassId(String classId) throws DatabaseException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE class_id = ? ORDER BY student_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, classId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching enrollments by class ID: " + e.getMessage(), e);
        }
        
        return enrollments;
    }
    
    /**
     * Lấy danh sách enrollment theo student_id
     */
    public List<Enrollment> findByStudentId(String studentId) throws DatabaseException {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ? ORDER BY class_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching enrollments by student ID: " + e.getMessage(), e);
        }
        
        return enrollments;
    }
    
    /**
     * Map ResultSet thành Enrollment object
     */
    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException, DatabaseException {
        Enrollment enrollment = new Enrollment();
        
        // Load student
        String studentId = rs.getString("student_id");
        if (studentId != null) {
            Student student = studentDAO.findById(studentId);
            enrollment.setStudent(student);
        }
        
        // Load homeroom
        String classId = rs.getString("class_id");
        if (classId != null) {
            Homeroom homeroom = homeroomDAO.findById(classId);
            enrollment.setHomeroom(homeroom);
        }
        
        BigDecimal attendance = rs.getBigDecimal("attendance");
        if (attendance != null) {
            enrollment.setAttendance(attendance);
        }
        
        BigDecimal homework = rs.getBigDecimal("homework");
        if (homework != null) {
            enrollment.setHomework(homework);
        }
        
        BigDecimal midTerm = rs.getBigDecimal("mid_term");
        if (midTerm != null) {
            enrollment.setMidTerm(midTerm);
        }
        
        BigDecimal endTerm = rs.getBigDecimal("end_term");
        if (endTerm != null) {
            enrollment.setEndTerm(endTerm);
        }
        
        return enrollment;
    }
}

