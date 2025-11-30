<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Lớp học</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Danh sách Lớp học</h1>
        
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
                <a href="${pageContext.request.contextPath}/classes?action=new" class="btn btn-success">Thêm lớp học mới</a>
            </c:if>
        </div>
        
        <div class="table-container">
            <c:choose>
                <c:when test="${not empty classes}">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã lớp</th>
                                <th>Môn học</th>
                                <th>Giáo viên</th>
                                <th>Ngày bắt đầu</th>
                                <th>Ngày kết thúc</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="classItem" items="${classes}">
                                <tr>
                                    <td><strong>${classItem.classId}</strong></td>
                                    <td>${classItem.subject != null ? classItem.subject.name : 'N/A'}</td>
                                    <td>${classItem.teacher != null ? classItem.teacher.name : 'N/A'}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${classItem.startDate != null}">
                                                ${classItem.startDate.dayOfMonth}/${classItem.startDate.monthValue}/${classItem.startDate.year}
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${classItem.endDate != null}">
                                                ${classItem.endDate.dayOfMonth}/${classItem.endDate.monthValue}/${classItem.endDate.year}
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:set var="today" value="<%=java.time.LocalDate.now()%>" />
                                        <c:choose>
                                            <c:when test="${classItem.ongoing}">
                                                <span style="color: green; font-weight: bold;">Đang diễn ra</span>
                                            </c:when>
                                            <c:when test="${classItem.endDate != null && !classItem.endDate.isAfter(today)}">
                                                <span style="color: gray;">Đã kết thúc</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: orange;">Sắp bắt đầu</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/classes/${classItem.classId}" 
                                           class="btn btn-primary btn-sm">Chi tiết</a>
                                        <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                                            <a href="${pageContext.request.contextPath}/classes/${classItem.classId}?action=edit" 
                                               class="btn btn-warning btn-sm">Sửa</a>
                                            <a href="${pageContext.request.contextPath}/classes/${classItem.classId}?action=delete" 
                                               class="btn btn-danger btn-sm"
                                               onclick="return confirm('Bạn có chắc chắn muốn xóa lớp học này?');">Xóa</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>Không có lớp học nào trong hệ thống.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

