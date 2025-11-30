package com.studentmanagement.dao;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.model.Homeroom;
import com.studentmanagement.model.Subject;
import com.studentmanagement.model.Teacher;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * HomeroomDAO - Xử lý database cho Homeroom
 */
public class HomeroomDAO extends BaseDAO<Homeroom> {
    private SubjectDAO subjectDAO = new SubjectDAO();
    private TeacherDAO teacherDAO = new TeacherDAO();
    
    @Override
    public List<Homeroom> findAll() throws DatabaseException {
        List<Homeroom> homerooms = new ArrayList<>();
        String sql = "SELECT * FROM homerooms ORDER BY class_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                homerooms.add(mapResultSetToHomeroom(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all homerooms: " + e.getMessage(), e);
        }
        
        return homerooms;
    }
    
    @Override
    public Homeroom findById(String id) throws DatabaseException {
        String sql = "SELECT * FROM homerooms WHERE class_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToHomeroom(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching homeroom by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean save(Homeroom homeroom) throws DatabaseException {
        String sql = "INSERT INTO homerooms (class_id, subject_id, teacher_id, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, homeroom.getClassId());
            stmt.setString(2, homeroom.getSubject() != null ? homeroom.getSubject().getSubjectId() : null);
            stmt.setString(3, homeroom.getTeacher() != null ? homeroom.getTeacher().getTeacherId() : null);
            stmt.setDate(4, Date.valueOf(homeroom.getStartDate()));
            stmt.setDate(5, Date.valueOf(homeroom.getEndDate()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving homeroom: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Homeroom homeroom) throws DatabaseException {
        String sql = "UPDATE homerooms SET subject_id = ?, teacher_id = ?, start_date = ?, end_date = ? WHERE class_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, homeroom.getSubject() != null ? homeroom.getSubject().getSubjectId() : null);
            stmt.setString(2, homeroom.getTeacher() != null ? homeroom.getTeacher().getTeacherId() : null);
            stmt.setDate(3, Date.valueOf(homeroom.getStartDate()));
            stmt.setDate(4, Date.valueOf(homeroom.getEndDate()));
            stmt.setString(5, homeroom.getClassId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating homeroom: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String id) throws DatabaseException {
        String sql = "DELETE FROM homerooms WHERE class_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting homeroom: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lấy danh sách lớp học theo giáo viên
     * @param teacherId ID giáo viên
     * @return List homerooms
     * @throws DatabaseException nếu có lỗi
     */
    public List<Homeroom> findByTeacherId(String teacherId) throws DatabaseException {
        List<Homeroom> homerooms = new ArrayList<>();
        String sql = "SELECT * FROM homerooms WHERE teacher_id = ? ORDER BY class_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, teacherId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                homerooms.add(mapResultSetToHomeroom(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching homerooms by teacher ID: " + e.getMessage(), e);
        }
        
        return homerooms;
    }
    
    /**
     * Map ResultSet thành Homeroom object
     */
    private Homeroom mapResultSetToHomeroom(ResultSet rs) throws SQLException, DatabaseException {
        Homeroom homeroom = new Homeroom();
        homeroom.setClassId(rs.getString("class_id"));
        
        // Load subject
        String subjectId = rs.getString("subject_id");
        if (subjectId != null) {
            Subject subject = subjectDAO.findById(subjectId);
            homeroom.setSubject(subject);
        }
        
        // Load teacher
        String teacherId = rs.getString("teacher_id");
        if (teacherId != null) {
            Teacher teacher = teacherDAO.findById(teacherId);
            homeroom.setTeacher(teacher);
        }
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            homeroom.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            homeroom.setEndDate(endDate.toLocalDate());
        }
        
        return homeroom;
    }
}

