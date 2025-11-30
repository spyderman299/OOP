package com.studentmanagement.util;

import com.studentmanagement.exception.FileException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * FileUtil - Xử lý file operations
 * Thể hiện xử lý file và exception handling
 */
public class FileUtil {
    private static final String LOG_DIR = "logs";
    private static final String EXPORT_DIR = "exports";
    
    /**
     * Tạo thư mục nếu chưa tồn tại
     */
    public static void createDirectoryIfNotExists(String dirPath) throws FileException {
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new FileException("Không thể tạo thư mục: " + dirPath, e);
        }
    }
    
    /**
     * Ghi log vào file
     */
    public static void writeLog(String message) throws FileException {
        try {
            createDirectoryIfNotExists(LOG_DIR);
            
            String logFile = LOG_DIR + File.separator + "application.log";
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
                writer.println(java.time.LocalDateTime.now() + " - " + message);
            }
        } catch (IOException e) {
            throw new FileException("Không thể ghi log: " + e.getMessage(), e);
        }
    }
    
    /**
     * Đọc file text
     */
    public static String readTextFile(String filePath) throws FileException {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new FileException("File không tồn tại: " + filePath);
            }
            return new String(Files.readAllBytes(path), "UTF-8");
        } catch (IOException e) {
            throw new FileException("Không thể đọc file: " + filePath, e);
        }
    }
    
    /**
     * Ghi file text
     */
    public static void writeTextFile(String filePath, String content) throws FileException {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.write(path, content.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new FileException("Không thể ghi file: " + filePath, e);
        }
    }
    
    /**
     * Export danh sách ra CSV
     */
    public static void exportToCSV(String fileName, List<String[]> data, String[] headers) throws FileException {
        try {
            createDirectoryIfNotExists(EXPORT_DIR);
            
            String filePath = EXPORT_DIR + File.separator + fileName;
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false), true)) {
                // Ghi headers
                if (headers != null && headers.length > 0) {
                    writer.println(String.join(",", headers));
                }
                
                // Ghi data
                for (String[] row : data) {
                    writer.println(String.join(",", row));
                }
            }
        } catch (IOException e) {
            throw new FileException("Không thể export CSV: " + e.getMessage(), e);
        }
    }
    
    /**
     * Kiểm tra file có tồn tại không
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Xóa file
     */
    public static boolean deleteFile(String filePath) throws FileException {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new FileException("Không thể xóa file: " + filePath, e);
        }
    }
}

