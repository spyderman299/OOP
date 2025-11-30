package com.studentmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Teacher - Kế thừa từ Person
 * Thể hiện Inheritance và Override method
 */
public class Teacher extends Person {
    // Encapsulation: private fields
    private String teacherId;
    private String email;
    private List<Homeroom> homerooms; // Quan hệ 1-n với Homeroom
    
    // Constructor
    public Teacher() {
        super();
        this.homerooms = new ArrayList<>();
    }
    
    public Teacher(String teacherId, String name, LocalDate dob, String phone, String email) {
        super(name, dob, phone);
        this.teacherId = teacherId;
        this.email = email;
        this.homerooms = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(String teacherId) {
        if (teacherId == null || teacherId.trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher ID cannot be null or empty");
        }
        this.teacherId = teacherId.trim().toUpperCase();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }
    
    public List<Homeroom> getHomerooms() {
        return homerooms;
    }
    
    public void setHomerooms(List<Homeroom> homerooms) {
        this.homerooms = homerooms != null ? homerooms : new ArrayList<>();
    }
    
    /**
     * Thêm lớp học cho giáo viên
     * @param homeroom đối tượng homeroom
     */
    public void addHomeroom(Homeroom homeroom) {
        if (homeroom != null && !homerooms.contains(homeroom)) {
            homerooms.add(homeroom);
            homeroom.setTeacher(this);
        }
    }
    
    /**
     * Xóa lớp học
     * @param homeroom đối tượng homeroom cần xóa
     */
    public void removeHomeroom(Homeroom homeroom) {
        if (homeroom != null) {
            homerooms.remove(homeroom);
        }
    }
    
    // Override abstract method - Polymorphism
    @Override
    public String getInfo() {
        return String.format("Teacher[ID: %s, Name: %s, Age: %d, Email: %s, Phone: %s]", 
                teacherId, getName(), getAge(), email, getPhone());
    }
    
    /**
     * Lấy số lớp đang dạy
     * @return số lớp
     */
    public int getNumberOfClasses() {
        return homerooms != null ? homerooms.size() : 0;
    }
    
    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", name='" + getName() + '\'' +
                ", dob=" + getDob() +
                ", phone='" + getPhone() + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Teacher teacher = (Teacher) obj;
        return teacherId != null ? teacherId.equals(teacher.teacherId) : teacher.teacherId == null;
    }
    
    @Override
    public int hashCode() {
        return teacherId != null ? teacherId.hashCode() : 0;
    }
}

