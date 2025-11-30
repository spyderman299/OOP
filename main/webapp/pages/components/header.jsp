<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="header">
    <h1>Hệ thống Quản lý Sinh viên</h1>
    <div class="header-nav">
        <c:if test="${not empty user}">
            <span>Xin chào, ${user.username} (${user.role})</span>
            <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
        </c:if>
    </div>
</div>
