package com.studentmanagement.dao;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.model.Teacher;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TeacherDAO - Xử lý database cho Teacher
 */
public class TeacherDAO extends BaseDAO<Teacher> {
    
    @Override
    public List<Teacher> findAll() throws DatabaseException {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY teacher_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                teachers.add(mapResultSetToTeacher(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all teachers: " + e.getMessage(), e);
        }
        
        return teachers;
    }
    
    @Override
    public Teacher findById(String id) throws DatabaseException {
        String sql = "SELECT * FROM teachers WHERE teacher_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTeacher(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching teacher by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean save(Teacher teacher) throws DatabaseException {
        String sql = "INSERT INTO teachers (teacher_id, name, dob, phone, email) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, teacher.getTeacherId());
            stmt.setString(2, teacher.getName());
            
            if (teacher.getDob() != null) {
                stmt.setDate(3, Date.valueOf(teacher.getDob()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            
            stmt.setString(4, teacher.getPhone());
            stmt.setString(5, teacher.getEmail());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving teacher: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Teacher teacher) throws DatabaseException {
        String sql = "UPDATE teachers SET name = ?, dob = ?, phone = ?, email = ? WHERE teacher_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, teacher.getName());
            
            if (teacher.getDob() != null) {
                stmt.setDate(2, Date.valueOf(teacher.getDob()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            
            stmt.setString(3, teacher.getPhone());
            stmt.setString(4, teacher.getEmail());
            stmt.setString(5, teacher.getTeacherId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating teacher: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String id) throws DatabaseException {
        String sql = "DELETE FROM teachers WHERE teacher_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting teacher: " + e.getMessage(), e);
        }
    }
    
    /**
     * Map ResultSet thành Teacher object
     */
    private Teacher mapResultSetToTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(rs.getString("teacher_id"));
        teacher.setName(rs.getString("name"));
        
        Date dob = rs.getDate("dob");
        if (dob != null) {
            teacher.setDob(dob.toLocalDate());
        }
        
        teacher.setPhone(rs.getString("phone"));
        teacher.setEmail(rs.getString("email"));
        
        return teacher;
    }
}

