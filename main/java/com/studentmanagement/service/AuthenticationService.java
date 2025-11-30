package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.dao.TeacherDAO;
import com.studentmanagement.dao.UserDAO;
import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.Teacher;
import com.studentmanagement.model.User;

/**
 * AuthenticationService - Xử lý authentication và authorization
 */
public class AuthenticationService {
    private UserDAO userDAO;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    
    public AuthenticationService() {
        this.userDAO = new UserDAO();
        this.studentDAO = new StudentDAO();
        this.teacherDAO = new TeacherDAO();
    }
    
    /**
     * Xác thực user và load thông tin đầy đủ
     * @param username Username
     * @param password Password
     * @return User object nếu đăng nhập thành công, null nếu thất bại
     * @throws DatabaseException nếu có lỗi database
     */
    public User authenticate(String username, String password) throws DatabaseException {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        
        User user = userDAO.findByUsername(username.trim());
        if (user == null) {
            return null;
        }
        
        // Kiểm tra password
        if (!user.authenticate(password)) {
            return null;
        }
        
        // Load thông tin đầy đủ dựa trên role
        loadUserDetails(user);
        
        return user;
    }
    
    /**
     * Load thông tin đầy đủ cho user (Student hoặc Teacher)
     * @param user User object
     * @throws DatabaseException nếu có lỗi database
     */
    private void loadUserDetails(User user) throws DatabaseException {
        if (user.isStudent()) {
            // Load student info từ users table
            User fullUser = userDAO.findByUsername(user.getUsername());
            if (fullUser != null) {
                // Note: Trong thực tế, cần load từ bảng users với teacher_id/student_id
                // Ở đây ta sẽ cần query lại để lấy student_id
                // Tạm thời giữ nguyên logic hiện tại
            }
        } else if (user.isTeacher()) {
            // Load teacher info
            // Tương tự như trên
        }
    }
    
    /**
     * Load Student object vào User
     * @param user User object
     * @throws DatabaseException nếu có lỗi database
     */
    public void loadStudentForUser(User user) throws DatabaseException {
        if (user.isStudent()) {
            String studentId = getUserStudentId(user.getUserId());
            if (studentId != null) {
                Student student = studentDAO.findById(studentId);
                user.setStudent(student);
            }
        }
    }
    
    /**
     * Load Teacher object vào User
     * @param user User object
     * @throws DatabaseException nếu có lỗi database
     */
    public void loadTeacherForUser(User user) throws DatabaseException {
        if (user.isTeacher()) {
            String teacherId = getUserTeacherId(user.getUserId());
            if (teacherId != null) {
                Teacher teacher = teacherDAO.findById(teacherId);
                user.setTeacher(teacher);
            }
        }
    }
    
    /**
     * Lấy student_id từ user_id
     */
    private String getUserStudentId(int userId) throws DatabaseException {
        User user = userDAO.findById(String.valueOf(userId));
        if (user != null && user.isStudent()) {
            // Cần query trực tiếp từ database
            return getUserStudentIdFromDB(userId);
        }
        return null;
    }
    
    /**
     * Lấy teacher_id từ user_id
     */
    private String getUserTeacherId(int userId) throws DatabaseException {
        User user = userDAO.findById(String.valueOf(userId));
        if (user != null && user.isTeacher()) {
            return getUserTeacherIdFromDB(userId);
        }
        return null;
    }
    
    /**
     * Query student_id từ database
     */
    private String getUserStudentIdFromDB(int userId) throws DatabaseException {
        return userDAO.getStudentIdByUserId(userId);
    }
    
    /**
     * Query teacher_id từ database
     */
    private String getUserTeacherIdFromDB(int userId) throws DatabaseException {
        return userDAO.getTeacherIdByUserId(userId);
    }
}

