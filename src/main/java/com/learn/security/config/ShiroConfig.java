package com.learn.security.config;

import com.learn.security.shiro.JwtFilter;
import com.learn.security.shiro.JwtRealm;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisClusterManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.Filter;
import java.time.Duration;
import java.util.*;

@Configuration
public class ShiroConfig {

    public static class StatelessSubjectFactory extends DefaultWebSubjectFactory{
        @Override
        public Subject createSubject(SubjectContext context){
            context.setSessionCreationEnabled(false);
            return super.createSubject(context);
        }
    }
    @Value("${redis.cluster.nodes}")
    private String clusterNodes;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.cluster.max-redirects}")
    private int maxRedirects;
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration(){
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        List<RedisNode> nodes = new ArrayList<>();
        for(String node : clusterNodes.split(",")){
            String[] parts = node.split(":");
            nodes.add(new RedisNode(parts[0],Integer.parseInt(parts[1])));
        }
        config.setClusterNodes(nodes);
        config.setMaxRedirects(maxRedirects);
        if(password != null && !password.isEmpty()){
            config.setPassword(password);
        }
        return config;
    }
    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration(){
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofMillis(5000))
                .keepAlive(SocketOptions.KeepAliveOptions.builder()
                        .idle(Duration.ofSeconds(30))
                        .interval(Duration.ofSeconds(10))
                        .count(3)
                        .enable()
                        .build())
                .build();
        ClusterTopologyRefreshOptions refreshOptions = ClusterTopologyRefreshOptions.builder()
                .enableAllAdaptiveRefreshTriggers()
                .enablePeriodicRefresh(Duration.ofSeconds(60))
                .build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                .socketOptions(socketOptions)
                .topologyRefreshOptions(refreshOptions)
                .autoReconnect(true)
                .build();
        return LettuceClientConfiguration.builder()
                .clientOptions(clusterClientOptions)
                .build();
    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisClusterConfiguration redisClusterConfiguration,
                                                         LettuceClientConfiguration lettuceClientConfiguration) {
        return new LettuceConnectionFactory(redisClusterConfiguration,lettuceClientConfiguration);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }
    @Bean
    public RedisClusterManager redisClusterManager(){
        RedisClusterManager manager = new RedisClusterManager();
        manager.setHost(clusterNodes);
        if(password != null && !password.isEmpty()){
            manager.setPassword(password);
        }
        manager.setTimeout(5000);
        manager.setMaxAttempts(maxRedirects);
        return manager;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisClusterManager redisClusterManager) {          // ← 去掉 RedisConnectionFactory 参数
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisClusterManager);
        return cacheManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(JwtRealm jwtRealm,
                                                     RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager sm = new DefaultWebSecurityManager();
        sm.setCacheManager(redisCacheManager);
        sm.setRealm(jwtRealm);

        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(evaluator);
        sm.setSubjectDAO(subjectDAO);

        sm.setSubjectFactory(new StatelessSubjectFactory());
        return sm;
    }

    @Bean
    public JwtFilter jwtFilter() {                          // ← 确保 JwtFilter 是 Bean
        return new JwtFilter();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            DefaultWebSecurityManager securityManager,
            JwtFilter jwtFilter) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);
        bean.setFilters(filters);

        Map<String, String> chain = new LinkedHashMap<>();
        chain.put("/login", "anon");
        chain.put("/swagger-ui/**", "anon");
        chain.put("/swagger-resources/**", "anon");
        chain.put("/v3/api-docs/**", "anon");
        chain.put("/webjars/**", "anon");
        chain.put("/**", "jwt");
        bean.setFilterChainDefinitionMap(chain);
        return bean;
    }
}