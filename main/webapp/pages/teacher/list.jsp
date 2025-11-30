<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Giáo viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Danh sách Giáo viên</h1>
        
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
                <a href="${pageContext.request.contextPath}/teachers?action=new" class="btn btn-success">Thêm giáo viên mới</a>
            </c:if>
        </div>
        
        <div class="table-container">
            <c:choose>
                <c:when test="${not empty teachers}">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã giáo viên</th>
                                <th>Họ và tên</th>
                                <th>Ngày sinh</th>
                                <th>Số điện thoại</th>
                                <th>Email</th>
                                <th>Số lớp dạy</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="teacher" items="${teachers}">
                                <tr>
                                    <td>${teacher.teacherId}</td>
                                    <td>${teacher.name}</td>
                                    <td>${teacher.dob != null ? teacher.dob : 'N/A'}</td>
                                    <td>${teacher.phone != null ? teacher.phone : 'N/A'}</td>
                                    <td>${teacher.email != null ? teacher.email : 'N/A'}</td>
                                    <td>${teacher.homerooms != null ? teacher.homerooms.size() : 0}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/teachers/${teacher.teacherId}" 
                                           class="btn btn-primary btn-sm">Chi tiết</a>
                                        <c:if test="${user.role == 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/teachers/${teacher.teacherId}?action=edit" 
                                               class="btn btn-warning btn-sm">Sửa</a>
                                            <a href="${pageContext.request.contextPath}/teachers/${teacher.teacherId}?action=delete" 
                                               class="btn btn-danger btn-sm"
                                               onclick="return confirm('Bạn có chắc chắn muốn xóa giáo viên này?');">Xóa</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>Không có giáo viên nào trong hệ thống.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

