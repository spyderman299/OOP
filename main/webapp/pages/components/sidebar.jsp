<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="sidebar">
    <div class="sidebar-header">
        <h2>Admin</h2>
        
        <c:if test="${not empty user}">
            <div class="user-profile">
                <div class="user-avatar">
                    <c:set var="firstChar" value="${fn:substring(user.username, 0, 1)}" />
                    ${fn:toUpperCase(firstChar)}
                </div>
                <div class="user-info">
                    <div class="user-name">
                        ${user.username}
                        <span class="user-status"></span>
                    </div>
                    <div class="user-role">${user.role}</div>
                </div>
            </div>
        </c:if>
    </div>
    
    <nav class="sidebar-menu">
        <ul>
            <li>
                <a href="${pageContext.request.contextPath}/dashboard" 
                   class="${pageContext.request.requestURI.contains('/dashboard') || pageContext.request.requestURI.endsWith('/pages/dashboard.jsp') ? 'active' : ''}">
                    HOME
                </a>
            </li>
            
            <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                <li>
                    <a href="${pageContext.request.contextPath}/students" 
                       class="${pageContext.request.requestURI.contains('/students') ? 'active' : ''}">
                        Quản lí Sinh viên
                    </a>
                </li>
            </c:if>
            
            <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                <li>
                    <a href="${pageContext.request.contextPath}/classes" 
                       class="${pageContext.request.requestURI.contains('/classes') ? 'active' : ''}">
                        Quản lí Lớp học
                    </a>
                </li>
            </c:if>
            
            <c:if test="${user.role == 'ADMIN'}">
                <li>
                    <a href="${pageContext.request.contextPath}/teachers" 
                       class="${pageContext.request.requestURI.contains('/teachers') ? 'active' : ''}">
                        Quản lí Giáo viên
                    </a>
                </li>
            </c:if>
            
            <c:if test="${user.role == 'ADMIN'}">
                <li>
                    <a href="${pageContext.request.contextPath}/users" 
                       class="${pageContext.request.requestURI.contains('/users') ? 'active' : ''}">
                        Quản lí Người dùng
                    </a>
                </li>
            </c:if>
            
            <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                <li>
                    <a href="${pageContext.request.contextPath}/subjects" 
                       class="${pageContext.request.requestURI.contains('/subjects') ? 'active' : ''}">
                        Quản lí Môn học
                    </a>
                </li>
            </c:if>
            
            <c:if test="${user.role == 'ADMIN' || user.role == 'TEACHER'}">
                <li>
                    <a href="${pageContext.request.contextPath}/enrollments" 
                       class="${pageContext.request.requestURI.contains('/enrollments') ? 'active' : ''}">
                        Quản lí Điểm
                    </a>
                </li>
            </c:if>
            
            <c:if test="${user.role == 'STUDENT'}">
                <li>
                    <a href="${pageContext.request.contextPath}/enrollments">
                        Điểm số của tôi
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</div>
