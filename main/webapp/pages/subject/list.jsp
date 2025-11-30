<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Môn học</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Danh sách Môn học</h1>
        
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
                <a href="${pageContext.request.contextPath}/subjects?action=new" class="btn btn-success">Thêm môn học mới</a>
            </c:if>
        </div>
        
        <div class="table-container">
            <c:choose>
                <c:when test="${not empty subjects}">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã môn học</th>
                                <th>Tên môn học</th>
                                <th>Số buổi học</th>
                                <th>Số lớp học</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="subject" items="${subjects}">
                                <tr>
                                    <td><strong>${subject.subjectId}</strong></td>
                                    <td>${subject.name}</td>
                                    <td>${subject.totalSessions}</td>
                                    <td>${subject.homerooms != null ? subject.homerooms.size() : 0}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/subjects/${subject.subjectId}" 
                                           class="btn btn-primary btn-sm">Chi tiết</a>
                                        <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                                            <a href="${pageContext.request.contextPath}/subjects/${subject.subjectId}?action=edit" 
                                               class="btn btn-warning btn-sm">Sửa</a>
                                            <a href="${pageContext.request.contextPath}/subjects/${subject.subjectId}?action=delete" 
                                               class="btn btn-danger btn-sm"
                                               onclick="return confirm('Bạn có chắc chắn muốn xóa môn học này?');">Xóa</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>Không có môn học nào trong hệ thống.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

