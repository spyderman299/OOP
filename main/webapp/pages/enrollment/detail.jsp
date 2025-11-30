<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Điểm số</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Chi tiết Điểm số</h1>
        
        <c:if test="${empty enrollment}">
            <div class="alert alert-error">
                Không tìm thấy đăng ký học phần.
            </div>
            <a href="${pageContext.request.contextPath}/enrollments" class="btn">Quay lại danh sách</a>
        </c:if>
        
        <c:if test="${not empty enrollment}">
            <div class="form-container">
                <div class="form-group">
                    <label>Lớp học:</label>
                    <p><strong>${enrollment.homeroom.classId}</strong> - ${enrollment.homeroom.subject.name}</p>
                    <p style="color: #666; font-size: 0.9rem;">
                        Giáo viên: ${enrollment.homeroom.teacher.name}
                    </p>
                </div>
                
                <div class="form-group">
                    <label>Sinh viên:</label>
                    <p><strong>${enrollment.student.studentId}</strong> - ${enrollment.student.name}</p>
                </div>
                
                <div class="form-group">
                    <label>Điểm chuyên cần:</label>
                    <p><strong>${enrollment.attendance}</strong> / 10</p>
                </div>
                
                <div class="form-group">
                    <label>Điểm bài tập:</label>
                    <p><strong>${enrollment.homework}</strong> / 10</p>
                </div>
                
                <div class="form-group">
                    <label>Điểm giữa kỳ:</label>
                    <p><strong>${enrollment.midTerm}</strong> / 10</p>
                </div>
                
                <div class="form-group">
                    <label>Điểm cuối kỳ:</label>
                    <p><strong>${enrollment.endTerm}</strong> / 10</p>
                </div>
                
                <div class="form-group" style="background-color: #f0f0f0; padding: 1rem; border-radius: 5px;">
                    <label style="font-size: 1.1rem;">Điểm tổng kết:</label>
                    <p style="font-size: 1.5rem; font-weight: bold; color: #2c3e50;">
                        ${enrollment.finalScore} / 10
                    </p>
                    <p style="color: #666; font-size: 0.9rem; margin-top: 0.5rem;">
                        Công thức: 0.1×${enrollment.attendance} + 0.2×${enrollment.homework} + 0.3×${enrollment.midTerm} + 0.4×${enrollment.endTerm}
                    </p>
                </div>
                
                <div class="form-group">
                    <label>Kết quả:</label>
                    <p>
                        <c:choose>
                            <c:when test="${enrollment.result == 'Passed'}">
                                <span style="color: green; font-weight: bold; font-size: 1.2rem;">✓ ĐẠT</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: red; font-weight: bold; font-size: 1.2rem;">✗ KHÔNG ĐẠT</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                
                <div class="action-buttons">
                    <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                        <a href="${pageContext.request.contextPath}/enrollments/${enrollment.homeroom.classId}/${enrollment.student.studentId}?action=edit" 
                           class="btn btn-warning">Sửa điểm</a>
                        <a href="${pageContext.request.contextPath}/enrollments/${enrollment.homeroom.classId}/${enrollment.student.studentId}?action=delete" 
                           class="btn btn-danger"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa đăng ký này?');">Xóa</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/enrollments" class="btn">Quay lại</a>
                </div>
            </div>
        </c:if>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

