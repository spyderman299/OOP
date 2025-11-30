package com.studentmanagement.dao;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.model.Student;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO - Xử lý database cho Student
 * Kế thừa từ BaseDAO<Student>
 */
public class StudentDAO extends BaseDAO<Student> {
    
    @Override
    public List<Student> findAll() throws DatabaseException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all students: " + e.getMessage(), e);
        }
        
        return students;
    }
    
    @Override
    public Student findById(String id) throws DatabaseException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching student by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public boolean save(Student student) throws DatabaseException {
        String sql = "INSERT INTO students (student_id, name, dob, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getName());
            
            if (student.getDob() != null) {
                stmt.setDate(3, Date.valueOf(student.getDob()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            
            stmt.setString(4, student.getPhone());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving student: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Student student) throws DatabaseException {
        String sql = "UPDATE students SET name = ?, dob = ?, phone = ? WHERE student_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getName());
            
            if (student.getDob() != null) {
                stmt.setDate(2, Date.valueOf(student.getDob()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            
            stmt.setString(3, student.getPhone());
            stmt.setString(4, student.getStudentId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating student: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String id) throws DatabaseException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting student: " + e.getMessage(), e);
        }
    }
    
    /**
     * Tìm kiếm sinh viên theo tên
     * @param name tên hoặc một phần tên
     * @return List sinh viên
     * @throws DatabaseException nếu có lỗi
     */
    public List<Student> searchByName(String name) throws DatabaseException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ? ORDER BY student_id";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching students by name: " + e.getMessage(), e);
        }
        
        return students;
    }
    
    /**
     * Map ResultSet thành Student object
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
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
}

