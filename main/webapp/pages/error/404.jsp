<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Trang không tìm thấy</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div style="text-align: center; margin-top: 100px;">
        <h1>404</h1>
        <h2>Trang không tìm thấy</h2>
        <p>Trang bạn đang tìm kiếm không tồn tại.</p>
        <a href="${pageContext.request.contextPath}/pages/login.jsp" class="btn btn-primary">Về trang đăng nhập</a>
    </div>
</body>
</html>

