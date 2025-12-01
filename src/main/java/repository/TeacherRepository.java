package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {
    private enum SQL {
        FIND_ALL("SELECT * FROM teachers"),
        FIND_BY_ID("SELECT * FROM teachers WHERE teacher_id = ?"),
        INSERT("INSERT INTO teachers (teacher_id, name, dob, phone, email) VALUES (?, ?, ?, ?, ?)"),
        UPDATE("UPDATE teachers SET name = ?, dob = ?, phone = ?, email = ? WHERE teacher_id = ?"),
        DELETE("DELETE FROM teachers WHERE teacher_id = ?");

        final String query;

        SQL(String q) {
            this.query = q;
        }
    }

    private Teacher map(ResultSet rs) throws SQLException {
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

    public List<Teacher> findAll() {
        List<Teacher> teachers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL.query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                teachers.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all teachers: " + e.getMessage());
        }
        return teachers;
    }

    public Teacher findById(String teacherId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_ID.query)) {
            stmt.setString(1, teacherId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding teacher by id: " + e.getMessage());
        }
        return null;
    }

    public boolean create(Teacher teacher) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT.query)) {
            stmt.setString(1, teacher.getTeacherId());
            stmt.setString(2, teacher.getName());
            if (teacher.getDob() != null) {
                stmt.setDate(3, Date.valueOf(teacher.getDob()));
            } else {
                stmt.setDate(3, null);
            }
            stmt.setString(4, teacher.getPhone());
            stmt.setString(5, teacher.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating teacher: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Teacher teacher) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE.query)) {
            stmt.setString(1, teacher.getName());
            if (teacher.getDob() != null) {
                stmt.setDate(2, Date.valueOf(teacher.getDob()));
            } else {
                stmt.setDate(2, null);
            }
            stmt.setString(3, teacher.getPhone());
            stmt.setString(4, teacher.getEmail());
            stmt.setString(5, teacher.getTeacherId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating teacher: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String teacherId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.DELETE.query)) {
            stmt.setString(1, teacherId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting teacher: " + e.getMessage());
            return false;
        }
    }
}

