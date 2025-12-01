package main.java.service.impl;

import main.java.model.Enrollment;
import main.java.repository.EnrollmentRepository;
import main.java.service.EnrollmentService;

import java.math.BigDecimal;
import java.util.List;

public class EnrollmentServiceImpl implements EnrollmentService {
    private static EnrollmentServiceImpl instance;
    private EnrollmentRepository enrollmentRepository;

    private EnrollmentServiceImpl() {
        this.enrollmentRepository = new EnrollmentRepository();
    }

    public static EnrollmentServiceImpl getInstance() {
        if (instance == null) {
            instance = new EnrollmentServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public List<Enrollment> getEnrollmentsByClassId(String classId) {
        if (classId == null || classId.trim().isEmpty()) {
            return getAllEnrollments();
        }
        return enrollmentRepository.findByClassId(classId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return getAllEnrollments();
        }
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Override
    public Enrollment getEnrollmentById(String classId, String studentId) {
        if (classId == null || classId.trim().isEmpty() || 
            studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        return enrollmentRepository.findById(classId, studentId);
    }

    @Override
    public boolean createEnrollment(Enrollment enrollment) {
        if (enrollment == null || enrollment.getClassId() == null || enrollment.getStudentId() == null) {
            return false;
        }

        // Check if enrollment already exists
        if (enrollmentRepository.findById(enrollment.getClassId(), enrollment.getStudentId()) != null) {
            System.err.println("Enrollment already exists for class: " + enrollment.getClassId() + 
                             ", student: " + enrollment.getStudentId());
            return false;
        }

        // Validate scores (0-10)
        if (!validateScore(enrollment.getAttendance()) || 
            !validateScore(enrollment.getHomework()) ||
            !validateScore(enrollment.getMidTerm()) ||
            !validateScore(enrollment.getEndTerm())) {
            System.err.println("Scores must be between 0 and 10");
            return false;
        }

        // Calculate final score and result
        enrollment.setAttendance(enrollment.getAttendance());
        enrollment.setHomework(enrollment.getHomework());
        enrollment.setMidTerm(enrollment.getMidTerm());
        enrollment.setEndTerm(enrollment.getEndTerm());

        return enrollmentRepository.create(enrollment);
    }

    @Override
    public boolean updateEnrollment(Enrollment enrollment) {
        if (enrollment == null || enrollment.getClassId() == null || enrollment.getStudentId() == null) {
            return false;
        }

        // Validate scores (0-10)
        if (!validateScore(enrollment.getAttendance()) || 
            !validateScore(enrollment.getHomework()) ||
            !validateScore(enrollment.getMidTerm()) ||
            !validateScore(enrollment.getEndTerm())) {
            System.err.println("Scores must be between 0 and 10");
            return false;
        }

        // Calculate final score and result
        enrollment.setAttendance(enrollment.getAttendance());
        enrollment.setHomework(enrollment.getHomework());
        enrollment.setMidTerm(enrollment.getMidTerm());
        enrollment.setEndTerm(enrollment.getEndTerm());

        return enrollmentRepository.update(enrollment);
    }

    @Override
    public boolean deleteEnrollment(String classId, String studentId) {
        if (classId == null || classId.trim().isEmpty() || 
            studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        return enrollmentRepository.delete(classId, studentId);
    }

    private boolean validateScore(BigDecimal score) {
        if (score == null) {
            return false;
        }
        double value = score.doubleValue();
        return value >= 0.0 && value <= 10.0;
    }
}

