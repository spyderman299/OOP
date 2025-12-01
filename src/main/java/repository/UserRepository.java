package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private enum SQL {
        FIND_BY_USERNAME("SELECT * FROM users WHERE username = ?"),
        FIND_BY_ID("SELECT * FROM users WHERE user_id = ?"),
        FIND_ALL("SELECT * FROM users"),
        INSERT("INSERT INTO users (username, password_plain, role, teacher_id, student_id) VALUES (?, ?, ?, ?, ?)"),
        UPDATE("UPDATE users SET username = ?, password_plain = ?, role = ?, teacher_id = ?, student_id = ? WHERE user_id = ?"),
        DELETE("DELETE FROM users WHERE user_id = ?");

        final String query;

        SQL(String q) {
            this.query = q;
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordPlain(rs.getString("password_plain"));
        user.setRole(rs.getString("role"));
        user.setTeacherId(rs.getString("teacher_id"));
        user.setStudentId(rs.getString("student_id"));
        return user;
    }

    public User findByUsername(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_USERNAME.query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
        }
        return null;
    }

    public User findById(int userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_ID.query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by id: " + e.getMessage());
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL.query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all users: " + e.getMessage());
        }
        return users;
    }

    public boolean create(User user) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT.query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordPlain());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getTeacherId());
            stmt.setString(5, user.getStudentId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    public boolean update(User user) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE.query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordPlain());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getTeacherId());
            stmt.setString(5, user.getStudentId());
            stmt.setInt(6, user.getUserId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.DELETE.query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}

