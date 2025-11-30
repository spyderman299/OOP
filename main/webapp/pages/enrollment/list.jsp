<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${user.role == 'STUDENT'}">Điểm số của tôi</c:when><c:otherwise>Danh sách Điểm số</c:otherwise></c:choose></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">
            <c:choose>
                <c:when test="${user.role == 'STUDENT'}">Điểm số của tôi</c:when>
                <c:otherwise>Danh sách Điểm số</c:otherwise>
            </c:choose>
        </h1>
        
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
        
        <c:if test="${not empty homeroom}">
            <div class="alert alert-info">
                <strong>Lớp học:</strong> ${homeroom.classId} - ${homeroom.subject.name} 
                (GV: ${homeroom.teacher.name})
            </div>
        </c:if>
        
        <c:if test="${not empty student}">
            <div class="alert alert-info">
                <strong>Sinh viên:</strong> ${student.studentId} - ${student.name}
            </div>
        </c:if>
        
        <div class="action-buttons">
            <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                <a href="${pageContext.request.contextPath}/enrollments?action=new" class="btn btn-success">Thêm đăng ký học phần</a>
            </c:if>
        </div>
        
        <!-- Filter form - Chỉ hiển thị cho ADMIN và TEACHER -->
        <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
            <div class="search-box" style="margin-bottom: 1rem;">
                <form method="GET" action="${pageContext.request.contextPath}/enrollments" style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
                    <select name="classId" style="flex: 1; min-width: 200px;">
                        <option value="">-- Chọn lớp học --</option>
                        <c:forEach var="h" items="${homerooms}">
                            <option value="${h.classId}" ${selectedClassId == h.classId ? 'selected' : ''}>
                                ${h.classId} - ${h.subject.name}
                            </option>
                        </c:forEach>
                    </select>
                    <select name="studentId" style="flex: 1; min-width: 200px;">
                        <option value="">-- Chọn sinh viên --</option>
                        <c:forEach var="s" items="${students}">
                            <option value="${s.studentId}" ${selectedStudentId == s.studentId ? 'selected' : ''}>
                                ${s.studentId} - ${s.name}
                            </option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary">Lọc</button>
                    <a href="${pageContext.request.contextPath}/enrollments" class="btn">Xóa bộ lọc</a>
                </form>
            </div>
        </c:if>
        
        <div class="table-container">
            <c:choose>
                <c:when test="${not empty enrollments}">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã lớp</th>
                                <th>Môn học</th>
                                <th>Mã SV</th>
                                <th>Tên SV</th>
                                <th>Chuyên cần</th>
                                <th>Bài tập</th>
                                <th>Giữa kỳ</th>
                                <th>Cuối kỳ</th>
                                <th>Điểm TK</th>
                                <th>Kết quả</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="enrollment" items="${enrollments}">
                                <tr>
                                    <td>${enrollment.homeroom.classId}</td>
                                    <td>${enrollment.homeroom.subject.name}</td>
                                    <td>${enrollment.student.studentId}</td>
                                    <td>${enrollment.student.name}</td>
                                    <td>${enrollment.attendance}</td>
                                    <td>${enrollment.homework}</td>
                                    <td>${enrollment.midTerm}</td>
                                    <td>${enrollment.endTerm}</td>
                                    <td><strong>${enrollment.finalScore}</strong></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${enrollment.result == 'Passed'}">
                                                <span style="color: green; font-weight: bold;">Đạt</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: red; font-weight: bold;">Không đạt</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/enrollments/${enrollment.homeroom.classId}/${enrollment.student.studentId}" 
                                           class="btn btn-primary btn-sm">Chi tiết</a>
                                        <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                                            <a href="${pageContext.request.contextPath}/enrollments/${enrollment.homeroom.classId}/${enrollment.student.studentId}?action=edit" 
                                               class="btn btn-warning btn-sm">Sửa điểm</a>
                                            <a href="${pageContext.request.contextPath}/enrollments/${enrollment.homeroom.classId}/${enrollment.student.studentId}?action=delete" 
                                               class="btn btn-danger btn-sm"
                                               onclick="return confirm('Bạn có chắc chắn muốn xóa đăng ký này?');">Xóa</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>Không có đăng ký học phần nào.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

