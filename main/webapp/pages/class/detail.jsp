<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Lớp học</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Chi tiết Lớp học</h1>
        
        <c:if test="${empty homeroom}">
            <div class="alert alert-error">
                Không tìm thấy lớp học.
            </div>
            <a href="${pageContext.request.contextPath}/classes" class="btn">Quay lại danh sách</a>
        </c:if>
        
        <c:if test="${not empty homeroom}">
            <div class="form-container">
                <div class="form-group">
                    <label>Mã lớp học:</label>
                    <p><strong>${homeroom.classId}</strong></p>
                </div>
                
                <div class="form-group">
                    <label>Môn học:</label>
                    <p><strong>${homeroom.subject != null ? homeroom.subject.name : 'N/A'}</strong></p>
                    <c:if test="${homeroom.subject != null}">
                        <p style="color: #666; font-size: 0.9rem;">
                            Mã môn: ${homeroom.subject.subjectId} | 
                            Số buổi: ${homeroom.subject.totalSessions}
                        </p>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label>Giáo viên:</label>
                    <p><strong>${homeroom.teacher != null ? homeroom.teacher.name : 'N/A'}</strong></p>
                    <c:if test="${homeroom.teacher != null}">
                        <p style="color: #666; font-size: 0.9rem;">
                            Mã GV: ${homeroom.teacher.teacherId}
                            <c:if test="${homeroom.teacher.email != null}">
                                | Email: ${homeroom.teacher.email}
                            </c:if>
                        </p>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label>Ngày bắt đầu:</label>
                    <p>
                        <c:choose>
                            <c:when test="${homeroom.startDate != null}">
                                ${homeroom.startDate.dayOfMonth}/${homeroom.startDate.monthValue}/${homeroom.startDate.year}
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                
                <div class="form-group">
                    <label>Ngày kết thúc:</label>
                    <p>
                        <c:choose>
                            <c:when test="${homeroom.endDate != null}">
                                ${homeroom.endDate.dayOfMonth}/${homeroom.endDate.monthValue}/${homeroom.endDate.year}
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                
                <div class="form-group">
                    <label>Trạng thái:</label>
                    <p>
                        <c:set var="today" value="<%=java.time.LocalDate.now()%>" />
                        <c:choose>
                            <c:when test="${homeroom.ongoing}">
                                <span style="color: green; font-weight: bold;">✓ Đang diễn ra</span>
                            </c:when>
                            <c:when test="${homeroom.endDate != null && !homeroom.endDate.isAfter(today)}">
                                <span style="color: gray;">Đã kết thúc</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: orange;">Sắp bắt đầu</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                
                <div class="form-group">
                    <label>Số sinh viên đã đăng ký:</label>
                    <p><strong>${homeroom.enrollments != null ? homeroom.enrollments.size() : 0}</strong></p>
                </div>
                
                <c:if test="${homeroom.enrollments != null && homeroom.enrollments.size() > 0}">
                    <div class="form-group">
                        <label>Danh sách sinh viên:</label>
                        <a href="${pageContext.request.contextPath}/enrollments?classId=${homeroom.classId}" 
                           class="btn btn-primary btn-sm">Xem danh sách đăng ký</a>
                    </div>
                </c:if>
                
                <div class="action-buttons">
                    <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                        <a href="${pageContext.request.contextPath}/classes/${homeroom.classId}?action=edit" 
                           class="btn btn-warning">Sửa</a>
                        <a href="${pageContext.request.contextPath}/classes/${homeroom.classId}?action=delete" 
                           class="btn btn-danger"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa lớp học này?');">Xóa</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/classes" class="btn">Quay lại</a>
                </div>
            </div>
        </c:if>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

