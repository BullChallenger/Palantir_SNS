package com.palantir.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    public static String generateToken(String accountId, String secretKey, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("accountId", accountId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String extractAccountId(String token, String secretKey) {
        return extractClams(token, secretKey).get("accountId", String.class);
    }

    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClams(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    private static Claims extractClams(String token, String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
