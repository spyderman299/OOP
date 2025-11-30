package com.studentmanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection - Singleton pattern để quản lý kết nối database
 * Thể hiện Encapsulation với private constructor và static instance
 */
public class DatabaseConnection {
    // Singleton instance
    private static DatabaseConnection instance;
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC&characterEncoding=utf8&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Password để trống
    
    // Private constructor - Encapsulation
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    // Get singleton instance
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Lấy kết nối database mới
     * @return Connection object
     * @throws SQLException nếu không kết nối được
     */
    public Connection getConnection() throws SQLException {
        // Nếu password rỗng, không truyền password parameter
        if (DB_PASSWORD == null || DB_PASSWORD.isEmpty()) {
            return DriverManager.getConnection(DB_URL, DB_USER, null);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Test kết nối database
     * @return true nếu kết nối thành công
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection successful!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}

