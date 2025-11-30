package com.studentmanagement.util;

import com.studentmanagement.exception.FileException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LoggerUtil - Utility class cho logging
 */
public class LoggerUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Log info message
     */
    public static void info(String message) {
        log("INFO", message);
    }
    
    /**
     * Log error message
     */
    public static void error(String message) {
        log("ERROR", message);
    }
    
    /**
     * Log error với exception
     */
    public static void error(String message, Exception e) {
        log("ERROR", message + " - " + e.getMessage());
        e.printStackTrace();
    }
    
    /**
     * Log warning message
     */
    public static void warning(String message) {
        log("WARNING", message);
    }
    
    /**
     * Log debug message
     */
    public static void debug(String message) {
        log("DEBUG", message);
    }
    
    /**
     * Ghi log vào console và file
     */
    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // In ra console
        System.out.println(logMessage);
        
        // Ghi vào file
        try {
            FileUtil.writeLog(logMessage);
        } catch (FileException e) {
            System.err.println("Không thể ghi log vào file: " + e.getMessage());
        }
    }
}

