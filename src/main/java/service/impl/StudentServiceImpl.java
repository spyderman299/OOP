package main.java.service.impl;

import main.java.model.Student;
import main.java.repository.StudentRepository;
import main.java.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private static StudentServiceImpl instance;
    private StudentRepository studentRepository;

    private StudentServiceImpl() {
        this.studentRepository = new StudentRepository();
    }

    public static StudentServiceImpl getInstance() {
        if (instance == null) {
            instance = new StudentServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        return studentRepository.findById(studentId);
    }

    @Override
    public List<Student> searchStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.findByName(name);
    }

    @Override
    public List<Student> getStudentsByIds(List<String> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return studentRepository.findByIds(studentIds);
    }

    @Override
    public boolean createStudent(Student student) {
        if (student == null || student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            return false;
        }

        // Check if student already exists
        if (studentRepository.findById(student.getStudentId()) != null) {
            System.err.println("Student ID already exists: " + student.getStudentId());
            return false;
        }

        return studentRepository.create(student);
    }

    @Override
    public boolean updateStudent(Student student) {
        if (student == null || student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            return false;
        }
        return studentRepository.update(student);
    }

    @Override
    public boolean deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        return studentRepository.delete(studentId);
    }
}

