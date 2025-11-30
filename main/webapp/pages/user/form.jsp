<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Sửa' : 'Thêm'} Người dùng</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script>
        function toggleLinkedFields() {
            const role = document.getElementById('role').value;
            const studentField = document.getElementById('studentField');
            const teacherField = document.getElementById('teacherField');
            
            if (role === 'STUDENT') {
                studentField.style.display = 'block';
                teacherField.style.display = 'none';
                document.getElementById('teacherId').value = '';
            } else if (role === 'TEACHER') {
                studentField.style.display = 'none';
                teacherField.style.display = 'block';
                document.getElementById('studentId').value = '';
            } else {
                studentField.style.display = 'none';
                teacherField.style.display = 'none';
                document.getElementById('studentId').value = '';
                document.getElementById('teacherId').value = '';
            }
        }
        
        window.onload = function() {
            toggleLinkedFields();
            // Nếu đang edit và có role, đảm bảo field được hiển thị
            const role = document.getElementById('role').value;
            if (role === 'STUDENT') {
                document.getElementById('studentField').style.display = 'block';
            } else if (role === 'TEACHER') {
                document.getElementById('teacherField').style.display = 'block';
            }
        };
    </script>
</head>
<body>
    <jsp:include page="../components/header.jsp" />
    
    <jsp:include page="../components/sidebar.jsp" />
    
    <div class="main-content">
        <h2>${isEdit ? 'Sửa thông tin Người dùng' : 'Thêm Người dùng mới'}</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ${error}
            </div>
        </c:if>
        
        <div class="form-container">
            <form method="POST" action="${pageContext.request.contextPath}/users">
                <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                <c:if test="${isEdit && userDetail != null}">
                    <input type="hidden" name="userId" value="${userDetail.userId}">
                </c:if>
                
                <div class="form-group">
                    <label for="username">Username <span style="color: red;">*</span></label>
                    <input type="text" id="username" name="username" 
                           value="${userDetail != null ? userDetail.username : ''}" 
                           required 
                           placeholder="Nhập username"
                           ${isEdit ? '' : 'autofocus'}>
                </div>
                
                <div class="form-group">
                    <label for="password">Password <span style="color: red;">${isEdit ? '' : '*'}</span></label>
                    <input type="password" id="password" name="password" 
                           ${isEdit ? '' : 'required'}
                           placeholder="${isEdit ? 'Để trống nếu không đổi password' : 'Nhập password'}"
                           ${isEdit ? '' : 'minlength="3"'}>
                    <c:if test="${isEdit}">
                        <small style="color: #7f8c8d; display: block; margin-top: 0.25rem;">Để trống nếu không muốn đổi password</small>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="role">Vai trò <span style="color: red;">*</span></label>
                    <select id="role" name="role" required onchange="toggleLinkedFields()">
                        <option value="">-- Chọn vai trò --</option>
                        <option value="ADMIN" ${userDetail != null && userDetail.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                        <option value="TEACHER" ${userDetail != null && userDetail.role == 'TEACHER' ? 'selected' : ''}>TEACHER</option>
                        <option value="STUDENT" ${userDetail != null && userDetail.role == 'STUDENT' ? 'selected' : ''}>STUDENT</option>
                    </select>
                </div>
                
                <div class="form-group" id="studentField" style="display: ${userDetail != null && userDetail.role == 'STUDENT' ? 'block' : 'none'};">
                    <label for="studentId">Mã sinh viên</label>
                    <select id="studentId" name="studentId">
                        <option value="">-- Chọn sinh viên --</option>
                        <c:forEach var="student" items="${students}">
                            <option value="${student.studentId}" 
                                    ${userDetail != null && userDetail.role == 'STUDENT' && not empty userDetail.studentIdValue && userDetail.studentIdValue == student.studentId ? 'selected' : ''}>
                                ${student.studentId} - ${student.name}
                            </option>
                        </c:forEach>
                    </select>
                    <small style="color: #7f8c8d;">Chọn sinh viên để liên kết với tài khoản</small>
                </div>
                
                <div class="form-group" id="teacherField" style="display: ${userDetail != null && userDetail.role == 'TEACHER' ? 'block' : 'none'};">
                    <label for="teacherId">Mã giáo viên</label>
                    <select id="teacherId" name="teacherId">
                        <option value="">-- Chọn giáo viên --</option>
                        <c:forEach var="teacher" items="${teachers}">
                            <option value="${teacher.teacherId}" 
                                    ${userDetail != null && userDetail.role == 'TEACHER' && not empty userDetail.teacherIdValue && userDetail.teacherIdValue == teacher.teacherId ? 'selected' : ''}>
                                ${teacher.teacherId} - ${teacher.name}
                            </option>
                        </c:forEach>
                    </select>
                    <small style="color: #7f8c8d;">Chọn giáo viên để liên kết với tài khoản</small>
                </div>
                
                <div class="action-buttons">
                    <button type="submit" class="btn btn-success">${isEdit ? 'Cập nhật' : 'Tạo mới'}</button>
                    <a href="${pageContext.request.contextPath}/users" class="btn">Hủy</a>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="../components/footer.jsp" />
</body>
</html>

