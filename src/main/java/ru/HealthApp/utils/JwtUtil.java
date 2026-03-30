package ru.HealthApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import ru.HealthApp.repository.entities.Account;

import java.security.Key;
import java.util.Date;

@Component
public final class JwtUtil {

    private static final String SECRET_KEY = "my-super-secret-key-for-health-app-jwt-token-generation";
    private static final long EXPIRATION_TIME = 86400000; // 24 часа

    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    private JwtUtil() {
    }

    public static String generateToken(String email, Long userId, Account.SystemRole role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("ROLE_", role)
                // create claim with ClassName?
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String extractEmail(String token) {

        return extractClaims(token).getSubject();
    }

    public static Long extractUserId(String token) {

        return extractClaims(token).get("userId", Long.class);
    }

    public static String extractRole(String token) {

        return extractClaims(token).get("ROLE_", String.class);
    }


    public static boolean validateToken(String token, String email) {

        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    // Парсим JWT токен
    private static Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
