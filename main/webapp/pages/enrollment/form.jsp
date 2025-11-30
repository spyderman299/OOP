<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Sửa' : 'Thêm'} Đăng ký học phần</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">${isEdit ? 'Sửa điểm số' : 'Thêm đăng ký học phần'}</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ${error}
            </div>
        </c:if>
        
        <div class="form-container">
            <form method="POST" action="${pageContext.request.contextPath}/enrollments">
                <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                
                <div class="form-group">
                    <label for="classId">Lớp học <span style="color: red;">*</span></label>
                    <select id="classId" name="classId" required ${isEdit ? 'disabled' : ''}>
                        <option value="">-- Chọn lớp học --</option>
                        <c:forEach var="h" items="${homerooms}">
                            <option value="${h.classId}" 
                                    ${(enrollment.homeroom != null && enrollment.homeroom.classId == h.classId) || presetClassId == h.classId ? 'selected' : ''}>
                                ${h.classId} - ${h.subject.name} (GV: ${h.teacher.name})
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${isEdit}">
                        <input type="hidden" name="classId" value="${enrollment.homeroom.classId}">
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="studentId">Sinh viên <span style="color: red;">*</span></label>
                    <select id="studentId" name="studentId" required ${isEdit ? 'disabled' : ''}>
                        <option value="">-- Chọn sinh viên --</option>
                        <c:forEach var="s" items="${students}">
                            <option value="${s.studentId}" 
                                    ${(enrollment.student != null && enrollment.student.studentId == s.studentId) || presetStudentId == s.studentId ? 'selected' : ''}>
                                ${s.studentId} - ${s.name}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${isEdit}">
                        <input type="hidden" name="studentId" value="${enrollment.student.studentId}">
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="attendance">Điểm chuyên cần (0-10)</label>
                    <input type="number" id="attendance" name="attendance" 
                           value="${enrollment.attendance != null ? enrollment.attendance : ''}" 
                           min="0" max="10" step="0.1" placeholder="VD: 8.5">
                </div>
                
                <div class="form-group">
                    <label for="homework">Điểm bài tập (0-10)</label>
                    <input type="number" id="homework" name="homework" 
                           value="${enrollment.homework != null ? enrollment.homework : ''}" 
                           min="0" max="10" step="0.1" placeholder="VD: 8.0">
                </div>
                
                <div class="form-group">
                    <label for="midTerm">Điểm giữa kỳ (0-10)</label>
                    <input type="number" id="midTerm" name="midTerm" 
                           value="${enrollment.midTerm != null ? enrollment.midTerm : ''}" 
                           min="0" max="10" step="0.1" placeholder="VD: 7.5">
                </div>
                
                <div class="form-group">
                    <label for="endTerm">Điểm cuối kỳ (0-10)</label>
                    <input type="number" id="endTerm" name="endTerm" 
                           value="${enrollment.endTerm != null ? enrollment.endTerm : ''}" 
                           min="0" max="10" step="0.1" placeholder="VD: 8.0">
                    <p style="font-size: 0.9rem; color: #666; margin-top: 0.5rem;">
                        <strong>Công thức tính điểm:</strong> Điểm TK = 0.1×Chuyên cần + 0.2×Bài tập + 0.3×Giữa kỳ + 0.4×Cuối kỳ
                    </p>
                </div>
                
                <div class="action-buttons">
                    <button type="submit" class="btn btn-success">${isEdit ? 'Cập nhật điểm' : 'Tạo mới'}</button>
                    <a href="${pageContext.request.contextPath}/enrollments" class="btn">Hủy</a>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

