package com.learn.interceptor;

import com.learn.utils.UserContext;
import com.learn.vo.UserVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("LOGIN_USER")==null){
            response.setStatus(401);
            return false;
        }
        UserVO userVO = (UserVO) session.getAttribute("LOGIN_USER");
        UserContext.setUserId(userVO.getUserId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
