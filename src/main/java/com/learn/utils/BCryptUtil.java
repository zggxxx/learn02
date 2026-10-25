package com.learn.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(12);

    public static String hash(String rawPassword){
        return ENCODER.encode(rawPassword);
    }

    public static boolean verify(String rawPassword,String hashedPassword){
        return ENCODER.matches(rawPassword,hashedPassword);
    }
}
