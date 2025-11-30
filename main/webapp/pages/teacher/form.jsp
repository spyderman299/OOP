<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Sửa' : 'Thêm'} Giáo viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">${isEdit ? 'Sửa thông tin Giáo viên' : 'Thêm Giáo viên mới'}</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ${error}
            </div>
        </c:if>
        
        <div class="form-container">
            <form method="POST" action="${pageContext.request.contextPath}/teachers">
                <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                
                <div class="form-group">
                    <label for="teacherId">Mã giáo viên <span style="color: red;">*</span></label>
                    <input type="text" id="teacherId" name="teacherId" 
                           value="${teacher.teacherId != null ? teacher.teacherId : ''}" 
                           required ${isEdit ? 'readonly' : ''}
                           placeholder="VD: GV001">
                </div>
                
                <div class="form-group">
                    <label for="name">Họ và tên <span style="color: red;">*</span></label>
                    <input type="text" id="name" name="name" 
                           value="${teacher.name != null ? teacher.name : ''}" 
                           required placeholder="Nhập họ và tên">
                </div>
                
                <div class="form-group">
                    <label for="dob">Ngày sinh</label>
                    <input type="date" id="dob" name="dob" 
                           value="${teacher.dob != null ? teacher.dob : ''}">
                </div>
                
                <div class="form-group">
                    <label for="phone">Số điện thoại</label>
                    <input type="text" id="phone" name="phone" 
                           value="${teacher.phone != null ? teacher.phone : ''}" 
                           placeholder="VD: 0987000001" pattern="[0-9]{10,11}">
                </div>
                
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" 
                           value="${teacher.email != null ? teacher.email : ''}" 
                           placeholder="VD: teacher@example.com">
                </div>
                
                <div class="action-buttons">
                    <button type="submit" class="btn btn-success">${isEdit ? 'Cập nhật' : 'Tạo mới'}</button>
                    <a href="${pageContext.request.contextPath}/teachers" class="btn">Hủy</a>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

