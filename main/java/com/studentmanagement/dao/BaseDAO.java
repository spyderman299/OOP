package com.studentmanagement.dao;

import com.studentmanagement.config.DatabaseConnection;
import com.studentmanagement.exception.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Abstract BaseDAO với generic type
 * Thể hiện Abstraction và Polymorphism
 * @param <T> Kiểu entity
 */
public abstract class BaseDAO<T> {
    protected DatabaseConnection dbConnection;
    
    public BaseDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Lấy tất cả records
     * @return List các entity
     * @throws DatabaseException nếu có lỗi
     */
    public abstract List<T> findAll() throws DatabaseException;
    
    /**
     * Tìm entity theo ID
     * @param id ID của entity
     * @return Entity hoặc null nếu không tìm thấy
     * @throws DatabaseException nếu có lỗi
     */
    public abstract T findById(String id) throws DatabaseException;
    
    /**
     * Lưu entity mới
     * @param entity Entity cần lưu
     * @return true nếu thành công
     * @throws DatabaseException nếu có lỗi
     */
    public abstract boolean save(T entity) throws DatabaseException;
    
    /**
     * Cập nhật entity
     * @param entity Entity cần cập nhật
     * @return true nếu thành công
     * @throws DatabaseException nếu có lỗi
     */
    public abstract boolean update(T entity) throws DatabaseException;
    
    /**
     * Xóa entity theo ID
     * @param id ID của entity cần xóa
     * @return true nếu thành công
     * @throws DatabaseException nếu có lỗi
     */
    public abstract boolean delete(String id) throws DatabaseException;
    
    /**
     * Lấy connection từ database
     * @return Connection object
     * @throws SQLException nếu có lỗi
     */
    protected Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }
    
    /**
     * Đóng connection một cách an toàn
     * @param conn Connection cần đóng
     */
    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}

