package com.studentmanagement.model;

/**
 * Class User - Người dùng hệ thống
 * Có quan hệ với Student hoặc Teacher
 */
public class User {
    // Encapsulation: private fields
    private int userId;
    private String username;
    private String passwordPlain; // Lưu password dạng plain text (có thể hash sau)
    private String role; // ADMIN, TEACHER, STUDENT
    private Teacher teacher; // Quan hệ 1-1 với Teacher (nếu role là TEACHER)
    private Student student; // Quan hệ 1-1 với Student (nếu role là STUDENT)
    
    // Constructor
    public User() {
    }
    
    public User(String username, String passwordPlain, String role) {
        this.username = username;
        this.passwordPlain = passwordPlain;
        this.role = role;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username.trim();
    }
    
    public String getPasswordPlain() {
        return passwordPlain;
    }
    
    public void setPasswordPlain(String passwordPlain) {
        if (passwordPlain == null || passwordPlain.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.passwordPlain = passwordPlain;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        if (role == null || (!role.equals("ADMIN") && !role.equals("TEACHER") && !role.equals("STUDENT"))) {
            throw new IllegalArgumentException("Invalid role. Must be ADMIN, TEACHER, or STUDENT");
        }
        this.role = role;
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    /**
     * Kiểm tra user có phải admin không
     * @return true nếu là admin
     */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    /**
     * Kiểm tra user có phải giáo viên không
     * @return true nếu là giáo viên
     */
    public boolean isTeacher() {
        return "TEACHER".equals(role);
    }
    
    /**
     * Kiểm tra user có phải sinh viên không
     * @return true nếu là sinh viên
     */
    public boolean isStudent() {
        return "STUDENT".equals(role);
    }
    
    /**
     * Xác thực password
     * @param password password cần kiểm tra
     * @return true nếu password đúng
     */
    public boolean authenticate(String password) {
        return passwordPlain != null && passwordPlain.equals(password);
    }
    
    /**
     * Lấy student ID (helper method để tránh xung đột với isStudent() trong EL)
     * @return student ID hoặc null
     */
    public String getStudentIdValue() {
        return student != null ? student.getStudentId() : null;
    }
    
    /**
     * Lấy teacher ID (helper method để tránh xung đột với isTeacher() trong EL)
     * @return teacher ID hoặc null
     */
    public String getTeacherIdValue() {
        return teacher != null ? teacher.getTeacherId() : null;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", teacher=" + (teacher != null ? teacher.getTeacherId() : null) +
                ", student=" + (student != null ? student.getStudentId() : null) +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId == user.userId || 
               (username != null && username.equals(user.username));
    }
    
    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}

