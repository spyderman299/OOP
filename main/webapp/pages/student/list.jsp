<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Sinh viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h2>Danh sách Sinh viên</h2>
        
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
            <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                <a href="${pageContext.request.contextPath}/students?action=new" class="btn btn-success">Thêm sinh viên mới</a>
            </c:if>
        </div>
        
        <div class="search-box">
            <form method="GET" action="${pageContext.request.contextPath}/students" style="display: flex; width: 100%; gap: 0.5rem;">
                <input type="hidden" name="action" value="search">
                <input type="text" name="searchTerm" placeholder="Tìm kiếm theo tên..." 
                       value="${searchTerm}" style="flex: 1;">
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                <a href="${pageContext.request.contextPath}/students" class="btn">Xóa bộ lọc</a>
            </form>
        </div>
        
        <div class="table-container">
            <c:choose>
                <c:when test="${not empty students}">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã SV</th>
                                <th>Họ và tên</th>
                                <th>Ngày sinh</th>
                                <th>Số điện thoại</th>
                                <th>Tuổi</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="student" items="${students}">
                                <tr>
                                    <td>${student.studentId}</td>
                                    <td>${student.name}</td>
                                    <td>${student.dob != null ? student.dob : 'N/A'}</td>
                                    <td>${student.phone != null ? student.phone : 'N/A'}</td>
                                    <td>${student.age}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/students/${student.studentId}" 
                                           class="btn btn-primary" style="padding: 0.5rem 1rem;">Chi tiết</a>
                                        <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                                            <a href="${pageContext.request.contextPath}/students/${student.studentId}?action=edit" 
                                               class="btn btn-warning" style="padding: 0.5rem 1rem;">Sửa</a>
                                            <a href="${pageContext.request.contextPath}/students/${student.studentId}?action=delete" 
                                               class="btn btn-danger" 
                                               style="padding: 0.5rem 1rem;"
                                               onclick="return confirm('Bạn có chắc chắn muốn xóa sinh viên này?');">Xóa</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>Không có sinh viên nào trong hệ thống.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

