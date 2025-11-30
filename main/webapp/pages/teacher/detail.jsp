<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Giáo viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Chi tiết Giáo viên</h1>
        
        <c:if test="${empty teacher}">
            <div class="alert alert-error">
                Không tìm thấy giáo viên.
            </div>
            <a href="${pageContext.request.contextPath}/teachers" class="btn">Quay lại danh sách</a>
        </c:if>
        
        <c:if test="${not empty teacher}">
            <div class="form-container">
                <div class="form-group">
                    <label>Mã giáo viên:</label>
                    <p><strong>${teacher.teacherId}</strong></p>
                </div>
                
                <div class="form-group">
                    <label>Họ và tên:</label>
                    <p><strong>${teacher.name}</strong></p>
                </div>
                
                <div class="form-group">
                    <label>Ngày sinh:</label>
                    <p>${teacher.dob != null ? teacher.dob : 'N/A'}</p>
                </div>
                
                <div class="form-group">
                    <label>Tuổi:</label>
                    <p>${teacher.age} tuổi</p>
                </div>
                
                <div class="form-group">
                    <label>Số điện thoại:</label>
                    <p>${teacher.phone != null ? teacher.phone : 'N/A'}</p>
                </div>
                
                <div class="form-group">
                    <label>Email:</label>
                    <p>${teacher.email != null ? teacher.email : 'N/A'}</p>
                </div>
                
                <div class="form-group">
                    <label>Số lớp đang dạy:</label>
                    <p><strong>${teacher.homerooms != null ? teacher.homerooms.size() : 0}</strong></p>
                </div>
                
                <c:if test="${teacher.homerooms != null && teacher.homerooms.size() > 0}">
                    <div class="form-group">
                        <label>Danh sách lớp học:</label>
                        <a href="${pageContext.request.contextPath}/classes?teacherId=${teacher.teacherId}" 
                           class="btn btn-primary btn-sm">Xem các lớp học</a>
                    </div>
                </c:if>
                
                <div class="action-buttons">
                    <c:if test="${user.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/teachers/${teacher.teacherId}?action=edit" 
                           class="btn btn-warning">Sửa</a>
                        <a href="${pageContext.request.contextPath}/teachers/${teacher.teacherId}?action=delete" 
                           class="btn btn-danger"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa giáo viên này?');">Xóa</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/teachers" class="btn">Quay lại</a>
                </div>
            </div>
        </c:if>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

