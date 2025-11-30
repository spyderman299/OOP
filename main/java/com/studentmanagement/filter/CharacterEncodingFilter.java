package com.studentmanagement.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter để đặt encoding UTF-8 cho tất cả request/response
 */
public class CharacterEncodingFilter implements Filter {
    private String encoding = "UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.isEmpty()) {
            encoding = encodingParam;
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        
        // Set character encoding
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        
        // Chỉ set content type là text/html nếu:
        // 1. Chưa có content type được set
        // 2. Không phải là static resource (CSS, JS, images)
        if (!requestURI.endsWith(".css") && 
            !requestURI.endsWith(".js") && 
            !requestURI.endsWith(".jpg") && 
            !requestURI.endsWith(".jpeg") && 
            !requestURI.endsWith(".png") && 
            !requestURI.endsWith(".gif") &&
            !requestURI.endsWith(".ico")) {
            // Chỉ set nếu response chưa có content type
            if (response.getContentType() == null) {
                response.setContentType("text/html; charset=" + encoding);
            }
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup code nếu cần
    }
}

