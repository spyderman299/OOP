<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Lỗi Server</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div style="text-align: center; margin-top: 100px;">
        <h1>500</h1>
        <h2>Lỗi Server</h2>
        <p>Đã xảy ra lỗi trong quá trình xử lý yêu cầu.</p>
        <% if (exception != null) { %>
            <p style="color: red;">Chi tiết: <%= exception.getMessage() %></p>
        <% } %>
        <a href="${pageContext.request.contextPath}/pages/login.jsp" class="btn btn-primary">Về trang đăng nhập</a>
    </div>
</body>
</html>

