package com.learn.interceptor;

import com.learn.utils.JwtUtil;
import com.learn.utils.UserContext;
import com.learn.vo.UserVO;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
        HttpSession session = request.getSession(false);
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Claims claims = JwtUtil.parseToken(token);
        if(session == null || session.getAttribute("LOGIN_USER")==null){
            response.setStatus(401);
            return false;
        }
        Long userId = claims.get("userId",Long.class);
        UserVO userVO = (UserVO) session.getAttribute("LOGIN_USER");
        UserContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
