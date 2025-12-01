package main.java.model;

public class User {
    private int userId;
    private String username;
    private String passwordPlain;
    private String role; // ADMIN, TEACHER, STUDENT
    private String teacherId;
    private String studentId;

    public User() {
    }

    public User(int userId, String username, String passwordPlain, String role, String teacherId, String studentId) {
        this.userId = userId;
        this.username = username;
        this.passwordPlain = passwordPlain;
        this.role = role;
        this.teacherId = teacherId;
        this.studentId = studentId;
    }

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
        this.username = username;
    }

    public String getPasswordPlain() {
        return passwordPlain;
    }

    public void setPasswordPlain(String passwordPlain) {
        this.passwordPlain = passwordPlain;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}

