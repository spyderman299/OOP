package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectRepository {
    private enum SQL {
        FIND_ALL("SELECT * FROM subjects"),
        FIND_BY_ID("SELECT * FROM subjects WHERE subject_id = ?"),
        INSERT("INSERT INTO subjects (subject_id, name, total_sessions) VALUES (?, ?, ?)"),
        UPDATE("UPDATE subjects SET name = ?, total_sessions = ? WHERE subject_id = ?"),
        DELETE("DELETE FROM subjects WHERE subject_id = ?");

        final String query;

        SQL(String q) {
            this.query = q;
        }
    }

    private Subject map(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setSubjectId(rs.getString("subject_id"));
        subject.setName(rs.getString("name"));
        subject.setTotalSessions(rs.getInt("total_sessions"));
        return subject;
    }

    public List<Subject> findAll() {
        List<Subject> subjects = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL.query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                subjects.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all subjects: " + e.getMessage());
        }
        return subjects;
    }

    public Subject findById(String subjectId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_ID.query)) {
            stmt.setString(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding subject by id: " + e.getMessage());
        }
        return null;
    }

    public boolean create(Subject subject) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT.query)) {
            stmt.setString(1, subject.getSubjectId());
            stmt.setString(2, subject.getName());
            stmt.setInt(3, subject.getTotalSessions());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating subject: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Subject subject) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE.query)) {
            stmt.setString(1, subject.getName());
            stmt.setInt(2, subject.getTotalSessions());
            stmt.setString(3, subject.getSubjectId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating subject: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String subjectId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.DELETE.query)) {
            stmt.setString(1, subjectId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting subject: " + e.getMessage());
            return false;
        }
    }
}

