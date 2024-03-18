package com.marco.loginjwt.config.auth;

import com.marco.loginjwt.domain.auth.JwtService;
import com.marco.loginjwt.domain.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class FilterChain extends OncePerRequestFilter {
    private final JwtService tokenService;
    private final UserService usersService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = recoverToken(request);
            if (token != null) {
                String username = tokenService.validateToken(token);
                UserDetails userDetails = usersService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException authEx) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
