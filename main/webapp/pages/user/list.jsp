<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Người dùng</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Danh sách Người dùng</h1>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ${successMessage}
                <c:remove var="successMessage" scope="session" />
            </div>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                ${errorMessage}
                <c:remove var="errorMessage" scope="session" />
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ${error}
            </div>
        </c:if>
        
        <div class="action-buttons">
            <c:if test="${user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/users?action=new" class="btn btn-success">Thêm người dùng mới</a>
            </c:if>
        </div>
        
        <div class="table-container">
            <c:choose>
                <c:when test="${not empty users}">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Vai trò</th>
                                <th>Mã sinh viên</th>
                                <th>Mã giáo viên</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="usr" items="${users}">
                                <tr>
                                    <td>${usr.userId}</td>
                                    <td>${usr.username}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${usr.role == 'ADMIN'}">
                                                <span style="color: #e74c3c; font-weight: bold;">ADMIN</span>
                                            </c:when>
                                            <c:when test="${usr.role == 'TEACHER'}">
                                                <span style="color: #3498db; font-weight: bold;">GIÁO VIÊN</span>
                                            </c:when>
                                            <c:when test="${usr.role == 'STUDENT'}">
                                                <span style="color: #27ae60; font-weight: bold;">SINH VIÊN</span>
                                            </c:when>
                                            <c:otherwise>${usr.role}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${usr.role == 'STUDENT' && not empty usr.studentIdValue}">
                                                ${usr.studentIdValue}
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #95a5a6;">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${usr.role == 'TEACHER' && not empty usr.teacherIdValue}">
                                                ${usr.teacherIdValue}
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #95a5a6;">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/users/${usr.userId}" 
                                           class="btn btn-primary btn-sm">Chi tiết</a>
                                        <c:if test="${user.role == 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/users/${usr.userId}?action=edit" 
                                               class="btn btn-warning btn-sm">Sửa</a>
                                            <c:if test="${usr.userId != user.userId}">
                                                <a href="${pageContext.request.contextPath}/users/${usr.userId}?action=delete" 
                                                   class="btn btn-danger btn-sm"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này?');">Xóa</a>
                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>Không có người dùng nào trong hệ thống.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

