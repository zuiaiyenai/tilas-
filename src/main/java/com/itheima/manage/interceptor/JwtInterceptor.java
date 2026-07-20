package com.itheima.manage.interceptor;

import com.itheima.manage.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String uri = request.getRequestURI();
        
        if ("POST".equals(request.getMethod()) && "/login".equals(uri)) {
            return true;
        }
        
        String token = request.getHeader("token");
        
        if (token == null || token.isEmpty()) {
            log.warn("用户未登录，请求路径: {}", uri);
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":0,\"msg\":\"未登录\"}");
            return false;
        }
        
        try {
            Claims claims = JwtUtil.parseToken(token);
            
            String userId = claims.get("userId").toString();
            String username = claims.get("username").toString();
            
            log.info("用户验证成功，userId: {}, username: {}", userId, username);
            
            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            
            return true;
        } catch (Exception e) {
            log.warn("令牌验证失败: {}", e.getMessage());
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":0,\"msg\":\"登录已过期，请重新登录\"}");
            return false;
        }
    }
}
