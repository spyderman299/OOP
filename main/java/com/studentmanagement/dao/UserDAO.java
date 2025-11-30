package com.studentmanagement.dao;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Xử lý database cho User
 */
public class UserDAO extends BaseDAO<User> {
    
    @Override
    public List<User> findAll() throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all users: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    @Override
    public User findById(String id) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching user by ID: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new DatabaseException("Invalid user ID format: " + id, e);
        }
        
        return null;
    }
    
    /**
     * Tìm user theo username
     * @param username username
     * @return User hoặc null
     * @throws DatabaseException nếu có lỗi
     */
    public User findByUsername(String username) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching user by username: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean save(User user) throws DatabaseException {
        String sql = "INSERT INTO users (username, password_plain, role, teacher_id, student_id) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordPlain());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getTeacher() != null ? user.getTeacher().getTeacherId() : null);
            stmt.setString(5, user.getStudent() != null ? user.getStudent().getStudentId() : null);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error saving user: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean update(User user) throws DatabaseException {
        String sql = "UPDATE users SET username = ?, password_plain = ?, role = ?, teacher_id = ?, student_id = ? WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordPlain());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getTeacher() != null ? user.getTeacher().getTeacherId() : null);
            stmt.setString(5, user.getStudent() != null ? user.getStudent().getStudentId() : null);
            stmt.setInt(6, user.getUserId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String id) throws DatabaseException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, Integer.parseInt(id));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new DatabaseException("Invalid user ID format: " + id, e);
        }
    }
    
    /**
     * Map ResultSet thành User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordPlain(rs.getString("password_plain"));
        user.setRole(rs.getString("role"));
        
        // Load teacher_id và student_id để có thể load sau
        String teacherId = rs.getString("teacher_id");
        String studentId = rs.getString("student_id");
        
        // Lưu ID vào user để load sau (sẽ được load trong AuthenticationService)
        if (teacherId != null) {
            // Có thể set vào một field tạm hoặc load luôn
        }
        if (studentId != null) {
            // Tương tự
        }
        
        return user;
    }
    
    /**
     * Lấy student_id từ user_id
     */
    public String getStudentIdByUserId(int userId) throws DatabaseException {
        String sql = "SELECT student_id FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("student_id");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching student_id: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Lấy teacher_id từ user_id
     */
    public String getTeacherIdByUserId(int userId) throws DatabaseException {
        String sql = "SELECT teacher_id FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("teacher_id");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching teacher_id: " + e.getMessage(), e);
        }
        
        return null;
    }
}

