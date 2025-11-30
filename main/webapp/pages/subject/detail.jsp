<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Môn học</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Chi tiết Môn học</h1>
        
        <c:if test="${empty subject}">
            <div class="alert alert-error">
                Không tìm thấy môn học.
            </div>
            <a href="${pageContext.request.contextPath}/subjects" class="btn">Quay lại danh sách</a>
        </c:if>
        
        <c:if test="${not empty subject}">
            <div class="form-container">
                <div class="form-group">
                    <label>Mã môn học:</label>
                    <p><strong>${subject.subjectId}</strong></p>
                </div>
                
                <div class="form-group">
                    <label>Tên môn học:</label>
                    <p><strong>${subject.name}</strong></p>
                </div>
                
                <div class="form-group">
                    <label>Số buổi học:</label>
                    <p><strong>${subject.totalSessions}</strong></p>
                </div>
                
                <div class="form-group">
                    <label>Số lớp học:</label>
                    <p><strong>${subject.homerooms != null ? subject.homerooms.size() : 0}</strong></p>
                </div>
                
                <c:if test="${subject.homerooms != null && subject.homerooms.size() > 0}">
                    <div class="form-group">
                        <label>Danh sách lớp học:</label>
                        <a href="${pageContext.request.contextPath}/classes?subjectId=${subject.subjectId}" 
                           class="btn btn-primary btn-sm">Xem các lớp học</a>
                    </div>
                </c:if>
                
                <div class="action-buttons">
                    <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                        <a href="${pageContext.request.contextPath}/subjects/${subject.subjectId}?action=edit" 
                           class="btn btn-warning">Sửa</a>
                        <a href="${pageContext.request.contextPath}/subjects/${subject.subjectId}?action=delete" 
                           class="btn btn-danger"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa môn học này?');">Xóa</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/subjects" class="btn">Quay lại</a>
                </div>
            </div>
        </c:if>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

