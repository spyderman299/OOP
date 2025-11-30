<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Hệ thống Quản lý Sinh viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="login-container">
        <h2>Đăng nhập hệ thống</h2>
        
        <%-- Hiển thị lỗi nếu có --%>
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null && !error.isEmpty()) { %>
            <div class="alert alert-error">
                <%= error %>
            </div>
        <% } %>
        
        <form method="POST" action="${pageContext.request.contextPath}/login">
            <div class="form-group">
                <label for="username">Tên đăng nhập:</label>
                <input type="text" id="username" name="username" required 
                       placeholder="Nhập tên đăng nhập" autofocus>
            </div>
            
            <div class="form-group">
                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" required 
                       placeholder="Nhập mật khẩu">
            </div>
            
            <button type="submit" class="btn btn-primary btn-block">Đăng nhập</button>
        </form>
        
        <div style="margin-top: 1rem; text-align: center; color: #666; font-size: 0.9rem;">
            <p>Demo accounts:</p>
            <p>Admin: admin / admin123</p>
            <p>Teacher: gv001 / 123456</p>
            <p>Student: b23dccn001 / 123456</p>
        </div>
    </div>
</body>
</html>

