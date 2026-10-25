package com.learn.security.shiro;

import com.learn.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        if("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())){
            return true;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String jwt = extractToken(request);
        if(jwt == null || !JwtUtil.validate(jwt)){
            return false;
        }
        try{
            getSubject(request,response).login(new JwtToken(jwt));
            return true;
        }catch(AuthenticationException e){
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("{\"code\":401,\"msg\":\"未认证或Token已过期\"}");
        return false;
    }

    private String extractToken(ServletRequest request){
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String header = httpRequest.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}
