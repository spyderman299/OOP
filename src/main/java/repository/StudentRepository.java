package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private enum SQL {
        FIND_ALL("SELECT * FROM students"),
        FIND_BY_ID("SELECT * FROM students WHERE student_id = ?"),
        FIND_BY_NAME("SELECT * FROM students WHERE name LIKE ?"),
        INSERT("INSERT INTO students (student_id, name, dob, phone) VALUES (?, ?, ?, ?)"),
        UPDATE("UPDATE students SET name = ?, dob = ?, phone = ? WHERE student_id = ?"),
        DELETE("DELETE FROM students WHERE student_id = ?");

        final String query;

        SQL(String q) {
            this.query = q;
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setName(rs.getString("name"));
        Date dob = rs.getDate("dob");
        if (dob != null) {
            student.setDob(dob.toLocalDate());
        }
        student.setPhone(rs.getString("phone"));
        return student;
    }

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL.query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all students: " + e.getMessage());
        }
        return students;
    }

    public Student findById(String studentId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_ID.query)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding student by id: " + e.getMessage());
        }
        return null;
    }

    public List<Student> findByName(String name) {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_NAME.query)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding students by name: " + e.getMessage());
        }
        return students;
    }

    public boolean create(Student student) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT.query)) {
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getName());
            if (student.getDob() != null) {
                stmt.setDate(3, Date.valueOf(student.getDob()));
            } else {
                stmt.setDate(3, null);
            }
            stmt.setString(4, student.getPhone());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating student: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Student student) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE.query)) {
            stmt.setString(1, student.getName());
            if (student.getDob() != null) {
                stmt.setDate(2, Date.valueOf(student.getDob()));
            } else {
                stmt.setDate(2, null);
            }
            stmt.setString(3, student.getPhone());
            stmt.setString(4, student.getStudentId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String studentId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.DELETE.query)) {
            stmt.setString(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find students by a list of student IDs
     */
    public List<Student> findByIds(List<String> studentIds) {
        List<Student> students = new ArrayList<>();
        if (studentIds == null || studentIds.isEmpty()) {
            return students;
        }

        // Build query with IN clause
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM students WHERE student_id IN (");
        for (int i = 0; i < studentIds.size(); i++) {
            queryBuilder.append("?");
            if (i < studentIds.size() - 1) {
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < studentIds.size(); i++) {
                stmt.setString(i + 1, studentIds.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding students by ids: " + e.getMessage());
        }
        return students;
    }
}

