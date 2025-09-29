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
        
        String requestPath = request.getRequestURI();

        // Allow unauthenticated access to login endpoints
        if (requestPath.equals("/v1/auth/login") ||
            requestPath.equals("/v1/auth/loginPage") ||
            requestPath.equals("/v1/center/login") ||
            requestPath.equals("/v1/teacher/login")) {
            return true;
        }

        // Center area routing
        if (requestPath.startsWith("/v1/center/")) {
            // If SUPER_ADMIN is logged in, allow all center endpoints
            Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
            String userRole = (String) session.getAttribute("role");
            if (Boolean.TRUE.equals(isLoggedIn) && "SUPER_ADMIN".equals(userRole)) {
                return true;
            }

            // Admin-only center management endpoints
            boolean adminOnly = requestPath.startsWith("/v1/center/management")
                    || requestPath.startsWith("/v1/center/register")
                    || (requestPath.matches("^/v1/center/\\d+$") && "DELETE".equalsIgnoreCase(request.getMethod()))
                    || (requestPath.matches("^/v1/center/\\d+$") && "PUT".equalsIgnoreCase(request.getMethod()));

            if (adminOnly) {
                if (Boolean.TRUE.equals(isLoggedIn) && "SUPER_ADMIN".equals(userRole)) {
                    return true;
                }
                response.sendRedirect("/v1/auth/loginPage");
                return false;
            }

            // Center self area (dashboard, read endpoints, etc.) requires center session
            Boolean centerLoggedIn = (Boolean) session.getAttribute("centerLoggedIn");
            if (Boolean.TRUE.equals(centerLoggedIn)) {
                return true;
            }
            response.sendRedirect("/v1/center/login");
            return false;
        }

        // System routes require SUPER_ADMIN
        boolean isSystemRoute = requestPath.startsWith("/v1/student/") ||
                requestPath.startsWith("/v1/group/") ||
                requestPath.startsWith("/v1/pay/") ||
                requestPath.startsWith("/v1/stats/");

        if (isSystemRoute) {
            Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
            String userRole = (String) session.getAttribute("role");

            if (Boolean.TRUE.equals(isLoggedIn) && "SUPER_ADMIN".equals(userRole)) {
                return true;
            }
            response.sendRedirect("/v1/auth/loginPage");
            return false;
        }

        // Teacher area routing
        if (requestPath.startsWith("/v1/teacher/")) {
            // SUPER_ADMIN can access all teacher endpoints
            Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
            String userRole = (String) session.getAttribute("role");
            if (Boolean.TRUE.equals(isLoggedIn) && "SUPER_ADMIN".equals(userRole)) {
                return true;
            }

            Boolean teacherLoggedIn = (Boolean) session.getAttribute("teacherLoggedIn");
            if (Boolean.TRUE.equals(teacherLoggedIn)) {
                return true;
            }
            response.sendRedirect("/v1/teacher/login");
            return false;
        }

        // Default allow
        return true;
    }
}
