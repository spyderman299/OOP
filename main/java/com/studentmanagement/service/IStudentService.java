package com.studentmanagement.service;

import com.studentmanagement.exception.DatabaseException;
import com.studentmanagement.exception.ValidationException;
import com.studentmanagement.model.Student;
import java.util.List;

/**
 * Interface IStudentService - Thể hiện Abstraction
 * Định nghĩa các method cho Student service
 */
public interface IStudentService {
    /**
     * Lấy tất cả sinh viên
     * @return List sinh viên
     * @throws DatabaseException nếu có lỗi database
     */
    List<Student> getAllStudents() throws DatabaseException;
    
    /**
     * Lấy sinh viên theo ID
     * @param studentId ID sinh viên
     * @return Student object hoặc null
     * @throws DatabaseException nếu có lỗi database
     * @throws ValidationException nếu dữ liệu không hợp lệ
     */
    Student getStudentById(String studentId) throws DatabaseException, ValidationException;
    
    /**
     * Tạo sinh viên mới
     * @param student Student object
     * @return true nếu thành công
     * @throws DatabaseException nếu có lỗi database
     * @throws ValidationException nếu dữ liệu không hợp lệ
     */
    boolean createStudent(Student student) throws DatabaseException, ValidationException;
    
    /**
     * Cập nhật sinh viên
     * @param student Student object
     * @return true nếu thành công
     * @throws DatabaseException nếu có lỗi database
     * @throws ValidationException nếu dữ liệu không hợp lệ
     */
    boolean updateStudent(Student student) throws DatabaseException, ValidationException;
    
    /**
     * Xóa sinh viên
     * @param studentId ID sinh viên
     * @return true nếu thành công
     * @throws DatabaseException nếu có lỗi database
     * @throws ValidationException nếu dữ liệu không hợp lệ
     */
    boolean deleteStudent(String studentId) throws DatabaseException, ValidationException;
    
    /**
     * Tìm kiếm sinh viên theo tên
     * @param name Tên hoặc một phần tên
     * @return List sinh viên
     * @throws DatabaseException nếu có lỗi database
     */
    List<Student> searchStudentsByName(String name) throws DatabaseException;
}

