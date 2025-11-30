<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Sinh viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h2>Chi tiết Sinh viên</h2>
        
        <c:if test="${empty student}">
            <div class="alert alert-error">
                Không tìm thấy sinh viên.
            </div>
            <a href="${pageContext.request.contextPath}/students" class="btn">Quay lại danh sách</a>
        </c:if>
        
        <c:if test="${not empty student}">
            <div class="form-container">
                <div class="form-group">
                    <label>Mã sinh viên:</label>
                    <p><strong>${student.studentId}</strong></p>
                </div>
                
                <div class="form-group">
                    <label>Họ và tên:</label>
                    <p><strong>${student.name}</strong></p>
                </div>
                
                <div class="form-group">
                    <label>Ngày sinh:</label>
                    <p>${student.dob != null ? student.dob : 'N/A'}</p>
                </div>
                
                <div class="form-group">
                    <label>Tuổi:</label>
                    <p>${student.age} tuổi</p>
                </div>
                
                <div class="form-group">
                    <label>Số điện thoại:</label>
                    <p>${student.phone != null ? student.phone : 'N/A'}</p>
                </div>
                
                <div class="action-buttons">
                    <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                        <a href="${pageContext.request.contextPath}/students/${student.studentId}?action=edit" 
                           class="btn btn-warning">Sửa</a>
                        <a href="${pageContext.request.contextPath}/students/${student.studentId}?action=delete" 
                           class="btn btn-danger"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa sinh viên này?');">Xóa</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/students" class="btn">Quay lại</a>
                </div>
            </div>
        </c:if>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

