package com.studentmanagement.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class Enrollment - Đăng ký học phần và điểm số
 * Có quan hệ với Student và Homeroom
 */
public class Enrollment {
    // Encapsulation: private fields
    private Student student; // Quan hệ với Student
    private Homeroom homeroom; // Quan hệ với Homeroom
    private BigDecimal attendance; // Điểm chuyên cần (0-10)
    private BigDecimal homework; // Điểm bài tập (0-10)
    private BigDecimal midTerm; // Điểm giữa kỳ (0-10)
    private BigDecimal endTerm; // Điểm cuối kỳ (0-10)
    
    // Constructor
    public Enrollment() {
        this.attendance = BigDecimal.ZERO;
        this.homework = BigDecimal.ZERO;
        this.midTerm = BigDecimal.ZERO;
        this.endTerm = BigDecimal.ZERO;
    }
    
    public Enrollment(Student student, Homeroom homeroom) {
        this();
        this.student = student;
        this.homeroom = homeroom;
        if (student != null) {
            student.addEnrollment(this);
        }
        if (homeroom != null) {
            homeroom.addEnrollment(this);
        }
    }
    
    // Getters and Setters
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
        if (student != null && !student.getEnrollments().contains(this)) {
            student.addEnrollment(this);
        }
    }
    
    public Homeroom getHomeroom() {
        return homeroom;
    }
    
    public void setHomeroom(Homeroom homeroom) {
        this.homeroom = homeroom;
        if (homeroom != null && !homeroom.getEnrollments().contains(this)) {
            homeroom.addEnrollment(this);
        }
    }
    
    public BigDecimal getAttendance() {
        return attendance;
    }
    
    public void setAttendance(BigDecimal attendance) {
        if (attendance != null && (attendance.compareTo(BigDecimal.ZERO) < 0 || 
                attendance.compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("Attendance score must be between 0 and 10");
        }
        this.attendance = attendance != null ? attendance.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
    
    public BigDecimal getHomework() {
        return homework;
    }
    
    public void setHomework(BigDecimal homework) {
        if (homework != null && (homework.compareTo(BigDecimal.ZERO) < 0 || 
                homework.compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("Homework score must be between 0 and 10");
        }
        this.homework = homework != null ? homework.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
    
    public BigDecimal getMidTerm() {
        return midTerm;
    }
    
    public void setMidTerm(BigDecimal midTerm) {
        if (midTerm != null && (midTerm.compareTo(BigDecimal.ZERO) < 0 || 
                midTerm.compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("Mid-term score must be between 0 and 10");
        }
        this.midTerm = midTerm != null ? midTerm.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
    
    public BigDecimal getEndTerm() {
        return endTerm;
    }
    
    public void setEndTerm(BigDecimal endTerm) {
        if (endTerm != null && (endTerm.compareTo(BigDecimal.ZERO) < 0 || 
                endTerm.compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("End-term score must be between 0 and 10");
        }
        this.endTerm = endTerm != null ? endTerm.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
    
    /**
     * Tính điểm tổng kết
     * Công thức: final_score = 0.1*attendance + 0.2*homework + 0.3*mid_term + 0.4*end_term
     * @return điểm tổng kết (làm tròn 2 chữ số)
     */
    public BigDecimal getFinalScore() {
        BigDecimal finalScore = attendance.multiply(new BigDecimal("0.1"))
                .add(homework.multiply(new BigDecimal("0.2")))
                .add(midTerm.multiply(new BigDecimal("0.3")))
                .add(endTerm.multiply(new BigDecimal("0.4")));
        
        return finalScore.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Xác định kết quả (Passed/Failed)
     * @return "Passed" nếu điểm >= 5.0, "Failed" nếu < 5.0
     */
    public String getResult() {
        BigDecimal finalScore = getFinalScore();
        return finalScore.compareTo(new BigDecimal("5.0")) >= 0 ? "Passed" : "Failed";
    }
    
    @Override
    public String toString() {
        return "Enrollment{" +
                "student=" + (student != null ? student.getStudentId() : null) +
                ", homeroom=" + (homeroom != null ? homeroom.getClassId() : null) +
                ", attendance=" + attendance +
                ", homework=" + homework +
                ", midTerm=" + midTerm +
                ", endTerm=" + endTerm +
                ", finalScore=" + getFinalScore() +
                ", result=" + getResult() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enrollment that = (Enrollment) obj;
        if (student == null || that.student == null || homeroom == null || that.homeroom == null) {
            return false;
        }
        return student.getStudentId().equals(that.student.getStudentId()) &&
               homeroom.getClassId().equals(that.homeroom.getClassId());
    }
    
    @Override
    public int hashCode() {
        int result = student != null ? student.getStudentId().hashCode() : 0;
        result = 31 * result + (homeroom != null ? homeroom.getClassId().hashCode() : 0);
        return result;
    }
}

