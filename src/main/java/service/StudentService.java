package main.java.service;

import main.java.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(String studentId);
    List<Student> searchStudentsByName(String name);
    List<Student> getStudentsByIds(List<String> studentIds);
    boolean createStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(String studentId);
}

