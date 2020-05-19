package com.mars.fw.security.tool;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @description:
 * @author: aron
 * @date: 2019-07-19 14:41
 */
public class TokenUtils {

    public static final long TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 1000;

    public static String createToken(String userName, Long time) {
        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + (null == time ? TOKEN_EXPIRATION_TIME : time)))
                .signWith(SignatureAlgorithm.HS512, "HxJwtSecret")
                .compact();
        return token;
    }
}
