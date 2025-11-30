package com.studentmanagement.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter để kiểm tra authentication trước khi truy cập các trang
 */
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String loginURI = contextPath + "/pages/login.jsp";
        String loginAction = contextPath + "/login";
        String dashboardURI = contextPath + "/dashboard";
        
        // Cho phép truy cập trang login, action login, và dashboard servlet
        boolean isLoginPage = requestURI.equals(loginURI) || requestURI.equals(loginAction);
        boolean isDashboard = requestURI.equals(dashboardURI);
        boolean isLoggedIn = session != null && session.getAttribute("user") != null;
        
        // Cho phép truy cập các static resources (CSS, JS, images)
        boolean isStaticResource = requestURI.startsWith(contextPath + "/css/") ||
                                   requestURI.startsWith(contextPath + "/js/") ||
                                   requestURI.startsWith(contextPath + "/images/");
        
        // Nếu là static resource, cho phép truy cập
        if (isStaticResource) {
            chain.doFilter(request, response);
            return;
        }
        
        // Nếu là dashboard và chưa login, redirect đến login
        if (isDashboard && !isLoggedIn) {
            httpResponse.sendRedirect(loginURI);
            return;
        }
        
        // Nếu đã login hoặc là trang login, cho phép truy cập
        if (isLoggedIn || isLoginPage || isDashboard) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(loginURI);
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup code
    }
}

