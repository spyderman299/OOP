package com.studentmanagement.dao;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SubjectDAO - Xử lý database cho Subject
 */
public class SubjectDAO extends BaseDAO<Subject> {
    
    @Override
    public List<Subject> findAll() throws DatabaseException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects ORDER BY subject_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                subjects.add(mapResultSetToSubject(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all subjects: " + e.getMessage(), e);
        }
        
        return subjects;
    }
    
    @Override
    public Subject findById(String id) throws DatabaseException {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSubject(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching subject by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean save(Subject subject) throws DatabaseException {
        String sql = "INSERT INTO subjects (subject_id, name, total_sessions) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, subject.getSubjectId());
            stmt.setString(2, subject.getName());
            stmt.setInt(3, subject.getTotalSessions());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving subject: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Subject subject) throws DatabaseException {
        String sql = "UPDATE subjects SET name = ?, total_sessions = ? WHERE subject_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, subject.getName());
            stmt.setInt(2, subject.getTotalSessions());
            stmt.setString(3, subject.getSubjectId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating subject: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String id) throws DatabaseException {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting subject: " + e.getMessage(), e);
        }
    }
    
    /**
     * Map ResultSet thành Subject object
     */
    private Subject mapResultSetToSubject(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setSubjectId(rs.getString("subject_id"));
        subject.setName(rs.getString("name"));
        subject.setTotalSessions(rs.getInt("total_sessions"));
        
        return subject;
    }
}

