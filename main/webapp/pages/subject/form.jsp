<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Sửa' : 'Thêm'} Môn học</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">${isEdit ? 'Sửa thông tin Môn học' : 'Thêm Môn học mới'}</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ${error}
            </div>
        </c:if>
        
        <div class="form-container">
            <form method="POST" action="${pageContext.request.contextPath}/subjects">
                <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                
                <div class="form-group">
                    <label for="subjectId">Mã môn học <span style="color: red;">*</span></label>
                    <input type="text" id="subjectId" name="subjectId" 
                           value="${subject.subjectId != null ? subject.subjectId : ''}" 
                           required ${isEdit ? 'readonly' : ''}
                           placeholder="VD: MH001">
                </div>
                
                <div class="form-group">
                    <label for="name">Tên môn học <span style="color: red;">*</span></label>
                    <input type="text" id="name" name="name" 
                           value="${subject.name != null ? subject.name : ''}" 
                           required placeholder="Nhập tên môn học">
                </div>
                
                <div class="form-group">
                    <label for="totalSessions">Số buổi học <span style="color: red;">*</span></label>
                    <input type="number" id="totalSessions" name="totalSessions" 
                           value="${subject.totalSessions != null ? subject.totalSessions : ''}" 
                           required min="0" placeholder="VD: 45">
                </div>
                
                <div class="action-buttons">
                    <button type="submit" class="btn btn-success">${isEdit ? 'Cập nhật' : 'Tạo mới'}</button>
                    <a href="${pageContext.request.contextPath}/subjects" class="btn">Hủy</a>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

