package ru.HealthApp.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.HealthApp.utils.JwtUtil;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /*private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);*/

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String ipAddress = request.getRemoteAddr();
        log.info("Входящий запрос: Метод={}, URL={}, IP={}",
                request.getMethod(), request.getRequestURI(), ipAddress);

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String userEmail = JwtUtil.extractEmail(token);
                Long userId = JwtUtil.extractUserId(token);
                String accountRole = JwtUtil.extractRole(token);

                if (userEmail != null && JwtUtil.validateToken(token, userEmail)) {
                    UsernamePasswordAuthenticationToken authToken = createAuthToken(userEmail, userId, accountRole, request);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Успешная аутентификация. User: {}, Role: {}, IP: {}", userEmail, accountRole, ipAddress);
                } else {
                    log.warn("Токен не прошел валидацию для email: {}. IP: {}", userEmail, ipAddress);
                }

            } catch (ExpiredJwtException e) {
                log.info("Сессия пользователя истекла (Expired JWT). IP: {}", ipAddress);
            } catch (JwtException e) {
                log.warn("Подозрительный или невалидный JWT токен! Ошибка: {}. IP: {}", e.getMessage(), ipAddress);
            } catch (Exception e) {
                log.error("Критическая ошибка внутри JwtAuthenticationFilter! IP: {}", ipAddress, e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createAuthToken(String userEmail, long userId, String accountRole, HttpServletRequest request) {

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(accountRole)
        );


        UserPrincipal userPrincipal = new UserPrincipal(userEmail, userId, authorities);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        authorities
                );

        authToken.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request));

        return authToken;
    }
}
