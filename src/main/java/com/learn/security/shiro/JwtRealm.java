package com.learn.security.shiro;

import com.learn.entity.UserPrincipal;
import com.learn.service.MenuService;
import com.learn.service.RoleService;
import com.learn.utils.JwtUtil;
import com.learn.utils.UserContext;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Set;

@Component
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @PostConstruct
    public void configure(){
        setCachingEnabled(true);
        setAuthenticationCachingEnabled(false);
        setAuthorizationCachingEnabled(true);
    }
    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof JwtToken;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        UserPrincipal userPrincipal = (UserPrincipal)principals.getPrimaryPrincipal();
        Long userId = userPrincipal.getId();
        Set<String> permissions = menuService.findPermsByUserId(userId);
        Set<String>roles = roleService.findRoleCodesByUserId(userId);
        info.addRoles(roles);
        info.addStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String) token.getCredentials();
        if(!JwtUtil.validate(jwt)){
            throw new AuthenticationException("token无效或过期");
        }
        Long userId = JwtUtil.parseToken(jwt).get("userId",Long.class);
        String redisKey = "jwt:access:" + userId;
        String cachedToken = redisTemplate.opsForValue().get(redisKey);
        if(cachedToken == null || !cachedToken.equals(jwt)){
            throw new AuthenticationException("token无效，请重新登录");
        }
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(userId);
        return new SimpleAuthenticationInfo(userPrincipal,jwt,getName());
    }
}
