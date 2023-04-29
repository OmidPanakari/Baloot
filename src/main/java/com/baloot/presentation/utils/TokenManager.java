package com.baloot.presentation.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TokenManager {
    private static final String SECRET = "357538782F413F4428472B4B6250655368566D59713374367639792442264529";
    public static String generateToken(String username, Map<String, Object> claims) {
        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getKey())
            .compact();
    }

    public static String generateToken(String username) {
        return generateToken(username, new HashMap<>());
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static boolean isTokenValid(String token) {
        var dt = extractExpirationDate(token);
        var dt2 = new Date(System.currentTimeMillis());
        return (token != null && extractExpirationDate(token).after(new Date(System.currentTimeMillis())));
    }

    private static Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private static Key getKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }
}
