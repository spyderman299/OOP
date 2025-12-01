package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Enrollment;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepository {
    private enum SQL {
        FIND_ALL("SELECT * FROM enrollments"),
        FIND_BY_CLASS_ID("SELECT * FROM enrollments WHERE class_id = ?"),
        FIND_BY_STUDENT_ID("SELECT * FROM enrollments WHERE student_id = ?"),
        FIND_BY_ID("SELECT * FROM enrollments WHERE class_id = ? AND student_id = ?"),
        INSERT("INSERT INTO enrollments (class_id, student_id, attendance, homework, mid_term, end_term) VALUES (?, ?, ?, ?, ?, ?)"),
        UPDATE("UPDATE enrollments SET attendance = ?, homework = ?, mid_term = ?, end_term = ? WHERE class_id = ? AND student_id = ?"),
        DELETE("DELETE FROM enrollments WHERE class_id = ? AND student_id = ?");

        final String query;

        SQL(String q) {
            this.query = q;
        }
    }

    private Enrollment map(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setClassId(rs.getString("class_id"));
        enrollment.setStudentId(rs.getString("student_id"));
        enrollment.setAttendance(rs.getBigDecimal("attendance"));
        enrollment.setHomework(rs.getBigDecimal("homework"));
        enrollment.setMidTerm(rs.getBigDecimal("mid_term"));
        enrollment.setEndTerm(rs.getBigDecimal("end_term"));
        enrollment.setFinalScore(rs.getBigDecimal("final_score"));
        enrollment.setResult(rs.getString("result"));
        return enrollment;
    }

    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL.query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                enrollments.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all enrollments: " + e.getMessage());
        }
        return enrollments;
    }

    public List<Enrollment> findByClassId(String classId) {
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_CLASS_ID.query)) {
            stmt.setString(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding enrollments by class id: " + e.getMessage());
        }
        return enrollments;
    }

    public List<Enrollment> findByStudentId(String studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_STUDENT_ID.query)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding enrollments by student id: " + e.getMessage());
        }
        return enrollments;
    }

    public Enrollment findById(String classId, String studentId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_ID.query)) {
            stmt.setString(1, classId);
            stmt.setString(2, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding enrollment by id: " + e.getMessage());
        }
        return null;
    }

    public boolean create(Enrollment enrollment) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT.query)) {
            stmt.setString(1, enrollment.getClassId());
            stmt.setString(2, enrollment.getStudentId());
            stmt.setBigDecimal(3, enrollment.getAttendance());
            stmt.setBigDecimal(4, enrollment.getHomework());
            stmt.setBigDecimal(5, enrollment.getMidTerm());
            stmt.setBigDecimal(6, enrollment.getEndTerm());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating enrollment: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Enrollment enrollment) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE.query)) {
            stmt.setBigDecimal(1, enrollment.getAttendance());
            stmt.setBigDecimal(2, enrollment.getHomework());
            stmt.setBigDecimal(3, enrollment.getMidTerm());
            stmt.setBigDecimal(4, enrollment.getEndTerm());
            stmt.setString(5, enrollment.getClassId());
            stmt.setString(6, enrollment.getStudentId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating enrollment: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String classId, String studentId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.DELETE.query)) {
            stmt.setString(1, classId);
            stmt.setString(2, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting enrollment: " + e.getMessage());
            return false;
        }
    }
}

