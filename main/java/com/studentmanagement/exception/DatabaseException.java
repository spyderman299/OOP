package com.studentmanagement.exception;

/**
 * Custom exception cho các lỗi database
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

