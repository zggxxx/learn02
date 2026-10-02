package com.learn.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {
    /** 密钥：HS256 要求至少 32字节（256bit） **/
    private static final String SECRET = "test-old-desktop-20261002-5522-qazwsxedcrfvtgbyhnujm-key";
    private static final SecretKey KEY =  Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    /** 过期时间 24小时 **/
    private static final Long EXP_TIME = 24 * 60 * 60 * 1000L;
    /**
     * 生成token
     */
    public static String createToken(Long userId ,String username){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXP_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();

    }
    /**
     * 解析token
      * @param token
     * @return
     */
    public static Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证token有效性
     * @param token
     * @return
     */
    public static boolean validate(String token){
         try{
             parseToken(token);
             return true;
         }catch(JwtException | IllegalArgumentException e){
             return false;
         }
    }


}
