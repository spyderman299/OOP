<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Sửa' : 'Thêm'} Lớp học</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">${isEdit ? 'Sửa thông tin Lớp học' : 'Thêm Lớp học mới'}</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ${error}
            </div>
        </c:if>
        
        <div class="form-container">
            <form method="POST" action="${pageContext.request.contextPath}/classes">
                <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                
                <div class="form-group">
                    <label for="classId">Mã lớp học <span style="color: red;">*</span></label>
                    <input type="text" id="classId" name="classId" 
                           value="${homeroom.classId != null ? homeroom.classId : ''}" 
                           required ${isEdit ? 'readonly' : ''}
                           placeholder="VD: MH001-1">
                </div>
                
                <div class="form-group">
                    <label for="subjectId">Môn học <span style="color: red;">*</span></label>
                    <select id="subjectId" name="subjectId" required>
                        <option value="">-- Chọn môn học --</option>
                        <c:forEach var="subject" items="${subjects}">
                            <option value="${subject.subjectId}" 
                                    ${homeroom.subject != null && homeroom.subject.subjectId == subject.subjectId ? 'selected' : ''}>
                                ${subject.name} (${subject.subjectId})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="teacherId">Giáo viên <span style="color: red;">*</span></label>
                    <select id="teacherId" name="teacherId" required>
                        <option value="">-- Chọn giáo viên --</option>
                        <c:forEach var="teacher" items="${teachers}">
                            <option value="${teacher.teacherId}" 
                                    ${homeroom.teacher != null && homeroom.teacher.teacherId == teacher.teacherId ? 'selected' : ''}>
                                ${teacher.name} (${teacher.teacherId})
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="startDate">Ngày bắt đầu <span style="color: red;">*</span></label>
                    <input type="date" id="startDate" name="startDate" 
                           value="${homeroom.startDate != null ? homeroom.startDate : ''}" 
                           required>
                    <p style="font-size: 0.85rem; color: #666; margin-top: 0.25rem;">
                        Format: YYYY-MM-DD (VD: 2025-11-10 cho ngày 10/11/2025)
                    </p>
                </div>
                
                <div class="form-group">
                    <label for="endDate">Ngày kết thúc <span style="color: red;">*</span></label>
                    <input type="date" id="endDate" name="endDate" 
                           value="${homeroom.endDate != null ? homeroom.endDate : ''}" 
                           required>
                    <p style="font-size: 0.85rem; color: #666; margin-top: 0.25rem;">
                        Format: YYYY-MM-DD (Phải sau ngày bắt đầu)
                    </p>
                </div>
                
                <div class="action-buttons">
                    <button type="submit" class="btn btn-success">${isEdit ? 'Cập nhật' : 'Tạo mới'}</button>
                    <a href="${pageContext.request.contextPath}/classes" class="btn">Hủy</a>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
    
    <script>
        // Validate: end date must be AFTER start date (not equal)
        function validateDates() {
            const startDateInput = document.getElementById('startDate');
            const endDateInput = document.getElementById('endDate');
            
            const startDateValue = startDateInput.value;
            const endDateValue = endDateInput.value;
            
            if (startDateValue && endDateValue) {
                const startDate = new Date(startDateValue);
                const endDate = new Date(endDateValue);
                
                // End date must be AFTER start date (endDate > startDate)
                if (endDate <= startDate) {
                    alert('Ngày kết thúc phải sau ngày bắt đầu!');
                    endDateInput.value = '';
                    endDateInput.focus();
                    return false;
                }
            }
            return true;
        }
        
        document.getElementById('startDate').addEventListener('change', function() {
            validateDates();
        });
        
        document.getElementById('endDate').addEventListener('change', function() {
            validateDates();
        });
        
        // Validate on form submit
        document.querySelector('form').addEventListener('submit', function(e) {
            if (!validateDates()) {
                e.preventDefault();
                return false;
            }
        });
    </script>
</body>
</html>

