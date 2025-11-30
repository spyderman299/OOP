package com.studentmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Homeroom - Lớp học
 * Có quan hệ với Subject, Teacher và Enrollment
 */
public class Homeroom {
    // Encapsulation: private fields
    private String classId;
    private Subject subject; // Quan hệ với Subject
    private Teacher teacher; // Quan hệ với Teacher
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Enrollment> enrollments; // Quan hệ 1-n với Enrollment
    
    // Constructor
    public Homeroom() {
        this.enrollments = new ArrayList<>();
    }
    
    public Homeroom(String classId, Subject subject, Teacher teacher, LocalDate startDate, LocalDate endDate) {
        this.classId = classId;
        this.subject = subject;
        this.teacher = teacher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enrollments = new ArrayList<>();
        
        // Thêm vào danh sách của subject và teacher
        if (subject != null) {
            subject.addHomeroom(this);
        }
        if (teacher != null) {
            teacher.addHomeroom(this);
        }
    }
    
    // Getters and Setters
    public String getClassId() {
        return classId;
    }
    
    public void setClassId(String classId) {
        if (classId == null || classId.trim().isEmpty()) {
            throw new IllegalArgumentException("Class ID cannot be null or empty");
        }
        this.classId = classId.trim();
    }
    
    public Subject getSubject() {
        return subject;
    }
    
    public void setSubject(Subject subject) {
        this.subject = subject;
        if (subject != null && !subject.getHomerooms().contains(this)) {
            subject.addHomeroom(this);
        }
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        if (teacher != null && !teacher.getHomerooms().contains(this)) {
            teacher.addHomeroom(this);
        }
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate;
    }
    
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments != null ? enrollments : new ArrayList<>();
    }
    
    /**
     * Thêm enrollment cho lớp học
     * @param enrollment đối tượng enrollment
     */
    public void addEnrollment(Enrollment enrollment) {
        if (enrollment != null && !enrollments.contains(enrollment)) {
            enrollments.add(enrollment);
            enrollment.setHomeroom(this);
        }
    }
    
    /**
     * Lấy số sinh viên đã đăng ký
     * @return số lượng sinh viên
     */
    public int getNumberOfStudents() {
        return enrollments != null ? enrollments.size() : 0;
    }
    
    /**
     * Kiểm tra lớp học có đang diễn ra không
     * @return true nếu lớp đang diễn ra
     */
    public boolean isOngoing() {
        LocalDate today = LocalDate.now();
        return startDate != null && endDate != null 
                && !today.isBefore(startDate) && !today.isAfter(endDate);
    }
    
    @Override
    public String toString() {
        return "Homeroom{" +
                "classId='" + classId + '\'' +
                ", subject=" + (subject != null ? subject.getName() : null) +
                ", teacher=" + (teacher != null ? teacher.getName() : null) +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Homeroom homeroom = (Homeroom) obj;
        return classId != null ? classId.equals(homeroom.classId) : homeroom.classId == null;
    }
    
    @Override
    public int hashCode() {
        return classId != null ? classId.hashCode() : 0;
    }
}

