package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.exception.ValidationException;
import com.studentmanagement.model.Student;
import java.util.List;

/**
 * StudentService - Implement IStudentService
 * Thể hiện Polymorphism và xử lý business logic
 */
public class StudentService implements IStudentService {
    private StudentDAO studentDAO;
    
    public StudentService() {
        this.studentDAO = new StudentDAO();
    }
    
    @Override
    public List<Student> getAllStudents() throws DatabaseException {
        return studentDAO.findAll();
    }
    
    @Override
    public Student getStudentById(String studentId) throws DatabaseException, ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be null or empty");
        }
        return studentDAO.findById(studentId.trim());
    }
    
    @Override
    public boolean createStudent(Student student) throws DatabaseException, ValidationException {
        validateStudent(student);
        
        // Kiểm tra xem student ID đã tồn tại chưa
        Student existing = studentDAO.findById(student.getStudentId());
        if (existing != null) {
            throw new ValidationException("Student with ID " + student.getStudentId() + " already exists");
        }
        
        return studentDAO.save(student);
    }
    
    @Override
    public boolean updateStudent(Student student) throws DatabaseException, ValidationException {
        validateStudent(student);
        
        // Kiểm tra xem student có tồn tại không
        Student existing = studentDAO.findById(student.getStudentId());
        if (existing == null) {
            throw new ValidationException("Student with ID " + student.getStudentId() + " does not exist");
        }
        
        return studentDAO.update(student);
    }
    
    @Override
    public boolean deleteStudent(String studentId) throws DatabaseException, ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be null or empty");
        }
        
        // Kiểm tra xem student có tồn tại không
        Student existing = studentDAO.findById(studentId.trim());
        if (existing == null) {
            throw new ValidationException("Student with ID " + studentId + " does not exist");
        }
        
        return studentDAO.delete(studentId.trim());
    }
    
    @Override
    public List<Student> searchStudentsByName(String name) throws DatabaseException {
        if (name == null || name.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentDAO.searchByName(name.trim());
    }
    
    /**
     * Validate student object
     * @param student Student object
     * @throws ValidationException nếu không hợp lệ
     */
    private void validateStudent(Student student) throws ValidationException {
        if (student == null) {
            throw new ValidationException("Student cannot be null");
        }
        
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new ValidationException("Student ID is required");
        }
        
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new ValidationException("Student name is required");
        }
        
        if (student.getPhone() != null && !student.getPhone().matches("^[0-9]{10,11}$")) {
            throw new ValidationException("Invalid phone number format");
        }
    }
}

