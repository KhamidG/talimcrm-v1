package com.gaipov.talim_crm.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleBasedAuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String userRole = (String) session.getAttribute("role");
        
        if (isLoggedIn == null || !isLoggedIn) {
            // Redirect to login page
            response.sendRedirect("/v1/auth/reception");
            return false;
        }
        
        String requestPath = request.getRequestURI();
        
        // Role-based access control
        if (userRole != null) {
            switch (userRole) {
                case "admin":
                    // Admin has access to everything
                    return true;
                    
                case "manager":
                    // Manager can access most features except sensitive admin functions
                    if (requestPath.contains("/v1/auth/") && !requestPath.contains("/logout")) {
                        response.sendRedirect("/v1/student/listPage");
                        return false;
                    }
                    return true;
                    
                case "receptionist":
                    // Receptionist can only access student registration and basic info
                    if (requestPath.contains("/v1/pay/") || 
                        requestPath.contains("/v1/stats/") || 
                        requestPath.contains("/v1/teacher/") ||
                        requestPath.contains("/v1/group/")) {
                        response.sendRedirect("/v1/student/register");
                        return false;
                    }
                    return true;
                    
                case "accountant":
                    // Accountant can access payments and statistics
                    if (requestPath.contains("/v1/teacher/") || 
                        requestPath.contains("/v1/group/") ||
                        requestPath.contains("/v1/student/register")) {
                        response.sendRedirect("/v1/pay/listPage");
                        return false;
                    }
                    return true;
                    
                default:
                    // Unknown role, redirect to reception
                    response.sendRedirect("/v1/auth/reception");
                    return false;
            }
        }
        
        return true;
    }
}
