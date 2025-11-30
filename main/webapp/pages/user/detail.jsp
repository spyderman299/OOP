<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Người dùng</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Chi tiết Người dùng</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ${error}
            </div>
        </c:if>
        
        <c:if test="${not empty userDetail}">
            <div class="form-container">
                <div class="form-group">
                    <label>User ID:</label>
                    <div style="padding: 0.5rem 0; font-weight: 500;">${userDetail.userId}</div>
                </div>
                
                <div class="form-group">
                    <label>Username:</label>
                    <div style="padding: 0.5rem 0; font-weight: 500;">${userDetail.username}</div>
                </div>
                
                <div class="form-group">
                    <label>Vai trò:</label>
                    <div style="padding: 0.5rem 0;">
                        <c:choose>
                            <c:when test="${userDetail.role == 'ADMIN'}">
                                <span style="color: #e74c3c; font-weight: bold; font-size: 1.1rem;">ADMIN</span>
                            </c:when>
                            <c:when test="${userDetail.role == 'TEACHER'}">
                                <span style="color: #3498db; font-weight: bold; font-size: 1.1rem;">GIÁO VIÊN</span>
                            </c:when>
                            <c:when test="${userDetail.role == 'STUDENT'}">
                                <span style="color: #27ae60; font-weight: bold; font-size: 1.1rem;">SINH VIÊN</span>
                            </c:when>
                            <c:otherwise>${userDetail.role}</c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <c:if test="${userDetail.role == 'STUDENT' && not empty userDetail.studentIdValue}">
                    <div class="form-group">
                        <label>Thông tin Sinh viên liên kết:</label>
                        <div style="padding: 0.5rem 0;">
                            <p><strong>Mã sinh viên:</strong> ${userDetail.studentIdValue}</p>
                            <a href="${pageContext.request.contextPath}/students/${userDetail.studentIdValue}" 
                               class="btn btn-primary btn-sm" style="margin-top: 0.5rem;">Xem chi tiết sinh viên</a>
                        </div>
                    </div>
                </c:if>
                
                <c:if test="${userDetail.role == 'TEACHER' && not empty userDetail.teacherIdValue}">
                    <div class="form-group">
                        <label>Thông tin Giáo viên liên kết:</label>
                        <div style="padding: 0.5rem 0;">
                            <p><strong>Mã giáo viên:</strong> ${userDetail.teacherIdValue}</p>
                            <a href="${pageContext.request.contextPath}/teachers/${userDetail.teacherIdValue}" 
                               class="btn btn-primary btn-sm" style="margin-top: 0.5rem;">Xem chi tiết giáo viên</a>
                        </div>
                    </div>
                </c:if>
                
                <c:if test="${userDetail.role == 'ADMIN'}">
                    <div class="form-group">
                        <label>Thông tin liên kết:</label>
                        <div style="padding: 0.5rem 0; color: #7f8c8d;">
                            Tài khoản ADMIN không liên kết với sinh viên hay giáo viên nào.
                        </div>
                    </div>
                </c:if>
                
                <div class="action-buttons">
                    <c:if test="${user.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/users/${userDetail.userId}?action=edit" 
                           class="btn btn-warning">Sửa</a>
                        <c:if test="${userDetail.userId != user.userId}">
                            <a href="${pageContext.request.contextPath}/users/${userDetail.userId}?action=delete" 
                               class="btn btn-danger"
                               onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này?');">Xóa</a>
                        </c:if>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/users" class="btn">Quay lại</a>
                </div>
            </div>
        </c:if>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

