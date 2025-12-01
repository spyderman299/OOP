package main.java.service;

import main.java.model.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
    List<Enrollment> getEnrollmentsByClassId(String classId);
    List<Enrollment> getEnrollmentsByStudentId(String studentId);
    Enrollment getEnrollmentById(String classId, String studentId);
    boolean createEnrollment(Enrollment enrollment);
    boolean updateEnrollment(Enrollment enrollment);
    boolean deleteEnrollment(String classId, String studentId);
}

