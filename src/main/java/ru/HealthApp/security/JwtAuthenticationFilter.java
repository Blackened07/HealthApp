package ru.HealthApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

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
                } else {
                    System.out.println("BURN IN HELL");
                }

            } catch (Exception e) {
                // Токен невалидный - просто игнорируем
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

        authToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request));

        return authToken;
    }
}
