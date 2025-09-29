package com.gaipov.talim_crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private RoleBasedAuthInterceptor roleBasedAuthInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleBasedAuthInterceptor)
                .addPathPatterns("/v1/student/**", 
                                 "/v1/group/**", 
                                 "/v1/teacher/**", 
                                 "/v1/pay/**", 
                                 "/v1/stats/**",
                                 "/v1/center/**",
                                 "/v1/auth/**")
                .excludePathPatterns(
                        "/v1/auth/login",
                        "/v1/auth/loginPage",
                        "/v1/center/login",
                        "/v1/teacher/login"
                );
    }
}
