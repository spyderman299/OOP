package com.studentmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Student - Kế thừa từ Person
 * Thể hiện Inheritance và Override method
 */
public class Student extends Person {
    // Encapsulation: private field
    private String studentId;
    private List<Enrollment> enrollments; // Quan hệ 1-n với Enrollment
    
    // Constructor
    public Student() {
        super();
        this.enrollments = new ArrayList<>();
    }
    
    public Student(String studentId, String name, LocalDate dob, String phone) {
        super(name, dob, phone);
        this.studentId = studentId;
        this.enrollments = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        this.studentId = studentId.trim().toUpperCase();
    }
    
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments != null ? enrollments : new ArrayList<>();
    }
    
    /**
     * Thêm enrollment cho sinh viên
     * @param enrollment đối tượng enrollment
     */
    public void addEnrollment(Enrollment enrollment) {
        if (enrollment != null && !enrollments.contains(enrollment)) {
            enrollments.add(enrollment);
            enrollment.setStudent(this);
        }
    }
    
    /**
     * Xóa enrollment
     * @param enrollment đối tượng enrollment cần xóa
     */
    public void removeEnrollment(Enrollment enrollment) {
        if (enrollment != null) {
            enrollments.remove(enrollment);
        }
    }
    
    // Override abstract method - Polymorphism
    @Override
    public String getInfo() {
        return String.format("Student[ID: %s, Name: %s, Age: %d, Phone: %s]", 
                studentId, getName(), getAge(), getPhone());
    }
    
    /**
     * Tính điểm trung bình tổng của tất cả các lớp
     * @return điểm trung bình
     */
    public double getAverageScore() {
        if (enrollments == null || enrollments.isEmpty()) {
            return 0.0;
        }
        
        double total = 0.0;
        int count = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getFinalScore() != null) {
                total += enrollment.getFinalScore().doubleValue();
                count++;
            }
        }
        
        return count > 0 ? total / count : 0.0;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + getName() + '\'' +
                ", dob=" + getDob() +
                ", phone='" + getPhone() + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId != null ? studentId.equals(student.studentId) : student.studentId == null;
    }
    
    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
}

